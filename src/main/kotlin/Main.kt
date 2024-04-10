package org.example

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.logs.AWSLogsClientBuilder
import com.amazonaws.services.logs.model.DescribeLogStreamsRequest
import com.amazonaws.services.logs.model.GetLogEventsRequest


fun main() {

    val logGroupName = "/aws/lambda/Hello_World"
    val clientConfig = ClientConfiguration()

    val builder = AWSLogsClientBuilder.standard()

    val logsClient = builder.withCredentials(AWSStaticCredentialsProvider(ProfileCredentialsProvider().credentials))
        .withRegion(Regions.EU_CENTRAL_1).withClientConfiguration(clientConfig).build()

    val describeLogStreamsRequest = DescribeLogStreamsRequest().withLogGroupName(logGroupName)
    val describeLogStreamsResult = logsClient.describeLogStreams(describeLogStreamsRequest)

    println(describeLogStreamsResult.logStreams.first())
    println(describeLogStreamsResult.logStreams.size)

    describeLogStreamsResult.logStreams.forEach {
        println(it.logStreamName)
        val getLogEventsRequest =
            GetLogEventsRequest().withLogGroupName(logGroupName).withLogStreamName(it.logStreamName)
        val result = logsClient.getLogEvents(getLogEventsRequest)
        result.events.forEach { outputLogEvent ->
            println(it.logStreamName)
            println("message= " + outputLogEvent.message)
        }
    }
}
