package app.endpoints

import app.PopMailerServiceGrpc
import app.SmtpMailerServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel

@Factory
class GrpcTestClients {
    @Bean
    fun smtpMailerBlockingStub(@GrpcChannel("mailer") channel: ManagedChannel): SmtpMailerServiceGrpc.SmtpMailerServiceBlockingStub =
        SmtpMailerServiceGrpc.newBlockingStub(channel)

    @Bean
    fun popMailerBlockingStub(@GrpcChannel("mailer") channel: ManagedChannel): PopMailerServiceGrpc.PopMailerServiceBlockingStub =
        PopMailerServiceGrpc.newBlockingStub(channel)
}
