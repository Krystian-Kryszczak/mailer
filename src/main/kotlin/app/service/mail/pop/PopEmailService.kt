package app.service.mail.pop

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.mail.Message

interface PopEmailService {
    fun searchMessages(folder: String? = null, subject: String): Flowable<Message>
    fun receiveMessages(folder: String? = null): Flowable<Message>
    fun receiveMessages(folder: String? = null, msgnums: IntArray): Flowable<Message>
    fun receiveMessages(folder: String? = null, start: Int, end: Int): Flowable<Message>
    fun receiveMessage(folder: String? = null, msgnum: Int): Single<Message>
}
