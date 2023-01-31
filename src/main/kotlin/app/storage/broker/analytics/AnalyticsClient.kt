package app.storage.broker.analytics

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface AnalyticsClient {
    @Topic("sent_mails")
    fun sentMails(@KafkaKey datetime: String, value: String)
}
