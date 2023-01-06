package qwee.zique.minioservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
@ConditionalOnProperty(
    value = ["spring.mail.notification.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
class JavaMailSenderConfig {
    @Bean
    fun javaMailSender(
        @Value("\${EMAIL_USER}")
        USERNAME: String,
        @Value("\${EMAIL_PASSWORD}")
        PASSWORD: String,
        @Value("\${EMAIL_HOST:smtp.gmail.com}")
        HOST: String
    ): JavaMailSender? = JavaMailSenderImpl()
        .apply {
            host = HOST
            port = 587
            username = USERNAME
            password = PASSWORD
            javaMailProperties = Properties().apply {
                this["mail.smtp.auth"] = true
                this["mail.smtp.starttls.enable"] = true
            }
        }
}