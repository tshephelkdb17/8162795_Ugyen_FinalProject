package com.example.s8162795assignment2.data

import com.example.s8162795assignment2.data.remote.AssignmentApi
import com.example.s8162795assignment2.data.repository.AuthRepositoryImpl
import com.example.s8162795assignment2.data.repository.DashboardRepositoryImpl
import com.example.s8162795assignment2.domain.model.AppError
import com.example.s8162795assignment2.domain.model.AppResult
import com.google.gson.JsonParser
import com.example.s8162795assignment2.data.remote.LoginRequest
import com.example.s8162795assignment2.data.remote.LoginResponse
import com.example.s8162795assignment2.data.remote.DashboardResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/** Real Retrofit + Gson against a local HTTP server; no university API needed. */
class RepositoryTest {
    private lateinit var server: MockWebServer
    private lateinit var api: AssignmentApi
    @Before fun setup() {
        server = MockWebServer(); server.start()
        api = Retrofit.Builder().baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build().create(AssignmentApi::class.java)
    }
    @After fun teardown() { server.shutdown() }
    private fun failingApi(failure: Exception): AssignmentApi = object : AssignmentApi {
        override suspend fun login(request: LoginRequest): LoginResponse = throw failure
        override suspend fun getDashboard(keypass: String): DashboardResponse = throw failure
    }
    private fun reply(body: String, status: Int = 200) {
        server.enqueue(MockResponse().setResponseCode(status).setHeader("Content-Type", "application/json").setBody(body))
    }
    @Test fun `auth posts correct path and JSON`() = runTest {
        reply("""{"keypass":"topic"}""")
        val result = AuthRepositoryImpl(api, StandardTestDispatcher(testScheduler)).login("8162795", "Example")
        assertEquals(AppResult.Success("topic"), result)
        val request = requireNotNull(server.takeRequest(1, TimeUnit.SECONDS))
        assertEquals("POST", request.method)
        assertEquals("/br/auth", request.path)
        val body = JsonParser.parseString(request.body.readUtf8()).asJsonObject
        assertEquals("8162795", body["username"].asString)
        assertEquals("Example", body["password"].asString)
    }
    @Test fun `401 maps to unauthorized`() = runTest {
        reply("{}", 401)
        assertEquals(AppResult.Failure(AppError.Unauthorized),
            AuthRepositoryImpl(api, StandardTestDispatcher(testScheduler)).login("8162795", "Example"))
    }
    @Test fun `missing keypass is invalid response`() = runTest {
        reply("{}")
        assertEquals(AppResult.Failure(AppError.InvalidResponse),
            AuthRepositoryImpl(api, StandardTestDispatcher(testScheduler)).login("8162795", "Example"))
    }
    @Test fun `malformed JSON is invalid response`() = runTest {
        reply("not-json")
        assertEquals(AppResult.Failure(AppError.InvalidResponse),
            AuthRepositoryImpl(api, StandardTestDispatcher(testScheduler)).login("8162795", "Example"))
    }
    @Test fun `dashboard uses keypass and preserves full fields`() = runTest {
        reply("""{"entities":[{"name":"Sample","year":2026,"description":"Full details"}],"entityTotal":1}""")
        val result = DashboardRepositoryImpl(api, StandardTestDispatcher(testScheduler)).getDashboard("topic")
        check(result is AppResult.Success)
        assertEquals(1, result.data.entityTotal)
        val entity = result.data.entities.single()
        assertEquals("Full details", entity.fields.single { it.name == "description" }.value)
        assertEquals("2026", entity.fields.single { it.name == "year" }.value)
        assertFalse(entity.summaryFields.any { it.name == "description" })
        val request = requireNotNull(server.takeRequest(1, TimeUnit.SECONDS))
        assertEquals("GET", request.method)
        assertEquals("/dashboard/topic", request.path)
    }
    @Test fun `missing entities fails validation`() = runTest {
        reply("""{"entityTotal":0}""")
        assertEquals(AppResult.Failure(AppError.InvalidResponse),
            DashboardRepositoryImpl(api, StandardTestDispatcher(testScheduler)).getDashboard("topic"))
    }
    @Test fun `IO failure maps to network error`() = runTest {
        val fake = failingApi(IOException("offline"))
        assertEquals(AppResult.Failure(AppError.Network),
            AuthRepositoryImpl(fake, StandardTestDispatcher(testScheduler)).login("1", "Example"))
    }
    @Test fun `socket timeout has distinct error`() = runTest {
        val fake = failingApi(SocketTimeoutException())
        assertEquals(AppResult.Failure(AppError.Timeout),
            AuthRepositoryImpl(fake, StandardTestDispatcher(testScheduler)).login("1", "Example"))
    }
    @Test fun `cancellation propagates instead of becoming UI error`() = runTest {
        val fake = failingApi(CancellationException("cancelled"))
        try {
            AuthRepositoryImpl(fake, StandardTestDispatcher(testScheduler)).login("1", "Example")
            fail("Cancellation must propagate")
        } catch (_: CancellationException) {
            // Expected structured-concurrency behaviour.
        }
    }
}
