package app.endpoints

import app.SendMailRequest
import app.SmtpMailerServiceGrpc
import app.security.GrpcMailerInterceptor
import io.grpc.*
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.grpc.testing.GrpcCleanupRule
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.mockk.mockk

@MicronautTest
class GrpcSmtpMailerEndpointTest(
    private val grpcMailerInterceptor: GrpcMailerInterceptor,
    private val grpcSmtpMailerEndpoint: GrpcSmtpMailerEndpoint,
) : StringSpec({
    val receiverAddress = "" // TODO change it

    // This rule manages automatic graceful shutdown for the registered servers and channels at the end of test.
    val grpcCleanup: GrpcCleanupRule = GrpcCleanupRule()

    // Generate a unique in-process server name.
    val serverName: String = InProcessServerBuilder.generateName()

    // Create a server, add service, start, and register for automatic graceful shutdown.
    grpcCleanup.register(
        InProcessServerBuilder.forName(serverName).directExecutor()
            .addService(ServerInterceptors.intercept(grpcSmtpMailerEndpoint, grpcMailerInterceptor))
            .build().start()
    )

    val channel = grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build())


    "gRPC Mailer Interceptor Test" {
        class SpyingClientInterceptor : ClientInterceptor {
            var spyListener: ClientCall.Listener<*>? = null

            override fun <ReqT, RespT> interceptCall(
                method: MethodDescriptor<ReqT, RespT>,
                callOptions: CallOptions,
                next: Channel
            ): ClientCall<ReqT, RespT> {

                return object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
                    override fun start(responseListener: Listener<RespT>, headers: Metadata) {
                        var responseListener: Listener<RespT>? = responseListener
                        responseListener = mockk()

                        spyListener = responseListener
                        super.start(responseListener, headers)
                    }
                }
            }
        }

        val clientInterceptor = SpyingClientInterceptor()
        val blockingStub: SmtpMailerServiceGrpc.SmtpMailerServiceBlockingStub = SmtpMailerServiceGrpc.newBlockingStub(channel)
            .withInterceptors(clientInterceptor)
        val metadataMockk = mockk<Metadata>()

        val request = SendMailRequest.newBuilder()
            .setAddress(receiverAddress)
            .setSubject("gRPC Mailer Endpoint Test")
            .setContent("Hello world!")
            .build()
        blockingStub.sendMail(request)

        clientInterceptor.spyListener shouldNotBe null
        // TODO refactoring
    }
})
