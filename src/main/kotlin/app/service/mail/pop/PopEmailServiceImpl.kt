package app.service.mail.pop

import io.reactivex.rxjava3.core.Flowable
import jakarta.inject.Singleton
import javax.mail.*
import io.micronaut.email.javamail.sender.SessionProvider
import io.reactivex.rxjava3.core.Single
import java.util.Properties
import javax.mail.search.SubjectTerm

@Singleton
class PopEmailServiceImpl(
    sessionProvider: SessionProvider,
): PopEmailService {
    private val session: Session = sessionProvider.session()
    private val properties: Properties = session.properties

    private fun getConnectedStore(): Single<Store> {
        val store = session.getStore("pop3")
        val host: String = properties["host"] as String? ?: return Single.error(Exception("pop3 host not found!"))
        val username: String = properties["username"] as String? ?: return Single.error(Exception("pop3 username not found!"))
        val password: String = properties["password"] as String? ?: return Single.error(Exception("pop3 password not found!"))
        store.connect(host, username, password)
        return Single.just(store)
    }
    private fun Single<Store>.getReadOnlyFolder(folder: String?): Single<Folder> =
        map {
            store -> run {
                val storeFolder: Folder = if (folder != null) store.getFolder(folder) else store.defaultFolder
                storeFolder.open(Folder.READ_ONLY)
                storeFolder
            }
        }

    override fun searchMessages(folder: String?, subject: String): Flowable<Message> =
        getConnectedStore()
        .getReadOnlyFolder(folder)
            .flatMapPublisher {
                storeFolder -> Flowable.fromIterable(storeFolder.search(SubjectTerm(subject)).asIterable())
            }

    override fun receiveMessages(folder: String?): Flowable<Message> =
        getConnectedStore()
        .getReadOnlyFolder(folder)
        .flatMapPublisher {
            storeFolder -> Flowable.fromIterable(storeFolder.messages.asIterable())
        }

    override fun receiveMessages(folder: String?, msgnums: IntArray): Flowable<Message> =
        getConnectedStore()
            .getReadOnlyFolder(folder)
            .flatMapPublisher {
                storeFolder -> Flowable.fromIterable(storeFolder.getMessages(msgnums).asIterable())
            }

    override fun receiveMessages(folder: String?, start: Int, end: Int): Flowable<Message> =
        getConnectedStore()
        .getReadOnlyFolder(folder)
        .flatMapPublisher {
            storeFolder -> Flowable.fromIterable(storeFolder.getMessages(start, end).asIterable())
        }

    override fun receiveMessage(folder: String?, msgnum: Int): Single<Message> {
        return getConnectedStore()
            .getReadOnlyFolder(folder)
            .flatMap {
                storeFolder -> Single.just(storeFolder.getMessage(msgnum))
            }
    }
}
