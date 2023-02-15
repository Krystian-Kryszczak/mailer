package app.endpoints

import app.*
import app.service.mail.smtp.SmtpEmailService
import io.grpc.stub.StreamObserver
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import jakarta.inject.Singleton

@Singleton
class GrpcSmtpMailerEndpoint(private val smtpEmailService: SmtpEmailService): SmtpMailerServiceGrpc.SmtpMailerServiceImplBase() {
    override fun sendMail(request: SendMailRequest, responseObserver: StreamObserver<MailerReply>) {
        Observable.fromCallable { smtpEmailService.send(request.address, request.subject, request.content) }
            .mapToTrueOrFalseOnError()
                .mapToGrpcMailerReply()
                    .subscribeToResponseObserver(responseObserver)
    }
    override fun sendUserActivationCode(request: MailerRequest, responseObserver: StreamObserver<MailerReply>) {
        Observable.fromCallable { smtpEmailService.sendUserActivationCode(request.address, request.content) }
            .mapToTrueOrFalseOnError()
                .mapToGrpcMailerReply()
                    .subscribeToResponseObserver(responseObserver)
    }
    override fun sendUserResetPasswordCode(request: MailerRequest, responseObserver: StreamObserver<MailerReply>) {
        Observable.fromCallable { smtpEmailService.sendResetPasswordCode(request.address, request.content) }
            .mapToTrueOrFalseOnError()
                .mapToGrpcMailerReply()
                    .subscribeToResponseObserver(responseObserver)
    }
    override fun sendNewVideoNotification(request: NotificationRequest, responseObserver: StreamObserver<MailerReply>) {
        Observable.fromCallable { smtpEmailService.sendNewVideoNotification(request.address, request.author, request.avatarUrl, request.title, request.link) }
            .mapToTrueOrFalseOnError()
                .mapToGrpcMailerReply()
                    .subscribeToResponseObserver(responseObserver)
    }
    private fun Observable<Unit>.mapToTrueOrFalseOnError(): Observable<Boolean> =
        map {
            true
        }.onErrorReturnItem(false)
    private fun Observable<Boolean>.mapToGrpcMailerReply(): Observable<MailerReply> =
        map { boolValue ->
            MailerReply.newBuilder()
                .setSuccessful(boolValue) // <---
                    .build()
        }
    private fun Observable<MailerReply>.subscribeToResponseObserver(responseObserver: StreamObserver<MailerReply>): Disposable =
        subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted)
}
