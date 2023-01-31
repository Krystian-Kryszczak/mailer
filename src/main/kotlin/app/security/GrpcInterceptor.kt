package app.security

import io.grpc.*
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class GrpcInterceptor: ServerInterceptor {
    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val inetSocketString: String = call.attributes.get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR).toString()
        logger.info("Service: ${call.methodDescriptor.serviceName}, Method: ${call.methodDescriptor.bareMethodName}, Headers: $headers, Socket: $inetSocketString")
        return next.startCall(call, headers);
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(GrpcInterceptor::class.java)
    }
}
