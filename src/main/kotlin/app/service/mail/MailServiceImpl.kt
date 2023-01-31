package app.service.mail

import io.micronaut.email.BodyType
import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.MultipartBody
import io.micronaut.email.template.TemplateBody
import io.micronaut.views.ModelAndView
import jakarta.inject.Singleton

@Singleton
class MailServiceImpl(private val emailSender: EmailSender<Any, Any>): MailService {
    private val fromEmail: String = System.getenv("MAILER_ADDRESS")
    override fun send(to: String, subject: String, content: String) {
        emailSender.send(Email.builder().from(fromEmail).to(to).subject(subject).body(content))
    }
    override fun send(to: String, subject: String, content: MultipartBody) {
        emailSender.send(Email.builder().from(fromEmail).to(to).subject(subject).body(content))
    }
    override fun sendUsingTemplate(to: String, subject: String, templateName: String, model: Map<String, String>) {
        send(to, subject,
            MultipartBody(
                TemplateBody(BodyType.HTML, ModelAndView("template/$templateName/html.vm", model)),
                TemplateBody(BodyType.TEXT, ModelAndView("template/$templateName/text.vm", model))
            )
        )
    }
    override fun sendUserActivationCode(to: String, activateCode: String) = sendUsingTemplate(
        to, "Account activation code", "activate-account",
        mapOf("code" to activateCode)
    )
    override fun sendResetPasswordCode(to: String, activateCode: String) = sendUsingTemplate(
        to, "Code for reset your account password", "reset-password",
        mapOf("code" to activateCode)
    )
    override fun sendNewVideoNotification(to: String, author: String, avatarUrl: String, title: String, videoUrl: String) = sendUsingTemplate(
        to, "$author uploaded new video...", "new-video",
        mapOf(
            "author" to author,
            "avatarUrl" to avatarUrl,
            "title" to title,
            "videoUrl" to videoUrl
        )
    )
}
