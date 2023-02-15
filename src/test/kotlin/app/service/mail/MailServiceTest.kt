package app.service.mail

import app.service.mail.smtp.SmtpEmailService
import io.kotest.core.spec.style.StringSpec
import io.micronaut.email.BodyType
import io.micronaut.email.MultipartBody
import io.micronaut.email.template.TemplateBody
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.micronaut.views.ModelAndView

@MicronautTest
class MailServiceTest(private val smtpEmailService: SmtpEmailService) : StringSpec({
    val receiver: String = ""; // TODO change it
    val testingTemplateName = "testing"

    fun String.withTestSuffix(): String = "$this - Mailer service test"

    "send (custom) using String content" {
        smtpEmailService.send(receiver, "Custom email using String content".withTestSuffix(), "Hello world!")
    }

    "send (custom) using Multipart content" {
        val model = mapOf<String, String>()
        val multipartContent = MultipartBody(
            TemplateBody(BodyType.HTML, ModelAndView("template/$testingTemplateName/html.vm", model)),
            TemplateBody(BodyType.TEXT, ModelAndView("template/$testingTemplateName/text.vm", model))
        )
        smtpEmailService.send(receiver, "Custom email using Multipart content".withTestSuffix(), multipartContent)
    }

    "send using template" {
        smtpEmailService.sendUsingTemplate(receiver, "Custom email using template".withTestSuffix(), testingTemplateName, mapOf())
    }

    "send user activation code" {
        smtpEmailService.sendUserActivationCode(receiver, "`test activation code`")
    }

    "send reset password code" {
        smtpEmailService.sendResetPasswordCode(receiver, "`test reset password code`")
    }

    "send new video notification" {
        smtpEmailService.sendNewVideoNotification(
            receiver,
            "unknown - testing",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ee/%28at%29.svg/1024px-%28at%29.svg.png", // https://en.wikipedia.org/wiki/Email
            "send new video notification".withTestSuffix(),
            "https://www.google.com/search?q=mailer+service+testing"
        )
    }
})
