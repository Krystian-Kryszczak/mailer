package app.endpoints

import app.*
import app.service.mail.pop.PopEmailService
import io.grpc.stub.StreamObserver
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import jakarta.inject.Singleton
import javax.mail.Message

@Singleton
class GrpcPopMailerEndpoint(private val popEmailService: PopEmailService): PopMailerServiceGrpc.PopMailerServiceImplBase() {
    override fun searchMessages(request: FolderAndSubject, responseObserver: StreamObserver<EmailMessage>) {
        popEmailService.searchMessages(request.folder, request.subject)
            .mapMessageToGrpcEmailMessage()
            .subscribeToResponseObserver(responseObserver)
    }

    override fun receiveMessages(request: Folder, responseObserver: StreamObserver<EmailMessage>) {
        popEmailService.receiveMessages(request.folder)
            .mapMessageToGrpcEmailMessage()
            .subscribeToResponseObserver(responseObserver)
    }

    override fun receiveMessagesNums(request: FolderAndMsgNums, responseObserver: StreamObserver<EmailMessage>) {
        popEmailService.receiveMessages(request.folder, request.msgnumsList.toIntArray())
            .mapMessageToGrpcEmailMessage()
            .subscribeToResponseObserver(responseObserver)
    }

    override fun receiveMessagesStartEnd(request: FolderAndStartAndEnd, responseObserver: StreamObserver<EmailMessage>) {
        popEmailService.receiveMessages(request.folder, request.start, request.end)
            .mapMessageToGrpcEmailMessage()
            .subscribeToResponseObserver(responseObserver)
    }

    override fun receiveMessage(request: FolderAndMsgNum, responseObserver: StreamObserver<EmailMessage>) {
        popEmailService.receiveMessage(request.folder, request.msgnums)
            .mapMessageToGrpcEmailMessage()
            .toObservable()
            .subscribeToResponseObserver(responseObserver)
    }

    private fun Flowable<Message>.mapMessageToGrpcEmailMessage(): Flowable<EmailMessage> =
        map {  message ->
            EmailMessage.newBuilder()
                .setSubject(message.subject)
                .setFrom(message.from.contentToString())
                .setContent(message.content as String? ?: "")
                .setSentDate(message.sentDate.time)
                .setReceivedDate(message.receivedDate.time)
                .build()
        }

    private fun Flowable<EmailMessage>.subscribeToResponseObserver(responseObserver: StreamObserver<EmailMessage>): Disposable =
        subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted)

    private fun Single<Message>.mapMessageToGrpcEmailMessage(): Single<EmailMessage> =
        map {  message ->
            EmailMessage.newBuilder()
                .setSubject(message.subject)
                .setFrom(message.from.contentToString())
                .setContent(message.content as String? ?: "")
                .setSentDate(message.sentDate.time)
                .setReceivedDate(message.receivedDate.time)
                .build()
        }

    private fun Observable<EmailMessage>.subscribeToResponseObserver(responseObserver: StreamObserver<EmailMessage>): Disposable =
        subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted)
}
