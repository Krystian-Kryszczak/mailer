package app.endpoints

import app.*
import app.service.mail.MailService
import io.grpc.stub.StreamObserver
import io.reactivex.rxjava3.core.Observable
import jakarta.inject.Singleton

@Singleton
class GrpcMailerEndpoint(private val mailService: MailService): MailerServiceGrpc.MailerServiceImplBase() {
    override fun sendMail(request: SendMailRequest, responseObserver: StreamObserver<MailerReply>) {
        Observable.fromCallable { mailService.send(request.address, request.subject, request.content) }
            .map { true }.onErrorReturnItem(false).map { MailerReply.newBuilder().setSuccessful(it).build() }
            .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted)
    }
    override fun sendUserActivationCode(request: MailerRequest, responseObserver: StreamObserver<MailerReply>) {
        Observable.fromCallable { mailService.sendUserActivationCode(request.address, request.content) }
            .map { true }.onErrorReturnItem(false).map { MailerReply.newBuilder().setSuccessful(it).build() }
            .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted)
    }
    override fun sendUserResetPasswordCode(request: MailerRequest, responseObserver: StreamObserver<MailerReply>) {
        Observable.fromCallable { mailService.sendResetPasswordCode(request.address, request.content) }
            .map { true }.onErrorReturnItem(false).map { MailerReply.newBuilder().setSuccessful(it).build() }
            .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted)
    }
    override fun sendNewVideoNotification(request: NotificationRequest, responseObserver: StreamObserver<MailerReply>) {
        Observable.fromCallable { mailService.sendNewVideoNotification(request.address, request.author, request.avatarUrl, request.title, request.link) }
            .map { true }.onErrorReturnItem(false).map { MailerReply.newBuilder().setSuccessful(it).build() }
            .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted)
    }
}
