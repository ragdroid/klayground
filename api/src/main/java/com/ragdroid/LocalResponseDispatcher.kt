package com.ragdroid.api

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.QueueDispatcher
import okhttp3.mockwebserver.RecordedRequest
import okio.Buffer
import java.io.IOException
/**
 * Created by garima-fueled on 24/02/17.
 */

class LocalResponseDispatcher : QueueDispatcher() {

    @Throws(InterruptedException::class)
    override fun dispatch(request: RecordedRequest): MockResponse {
        val mockResponse = MockResponse()
        val scenario = getScenario(request)
        if (scenario != null) {
            try {
                mockResponse.setBody(readFile(scenario))
                mockResponse.setResponseCode(200)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return mockResponse
    }

    private fun getScenario(request: RecordedRequest): String? {

        val segements = request.requestUrl.pathSegments()
        val requestedMethod = request.method.toLowerCase()

        var scenario = "${requestedMethod}_"

        for ((index, segment) in segements.withIndex()) {
            if (index != segements.size -1) {
                scenario += "${segment}_"
            } else
                scenario += segment
        }
        scenario += ".json"
        return scenario
    }

    @Throws(IOException::class)
    private fun readFile(jsonFileName: String): String {
        val inputStream = LocalResponseDispatcher::class.java.getResourceAsStream("/" + jsonFileName) ?: throw NullPointerException("Have you added the local resource correctly?, "
                + "Hint: name it as: " + jsonFileName)

        val buffer = Buffer()
        buffer.readFrom(inputStream)

        return buffer.readUtf8()
    }
}
