package qwee.zique.minioservice.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("minio adapter service")
                    .version("demo")
                    .contact(
                        Contact()
                            .name("qweezique")
                            .url("https://t.me/qweezique")
                    )
            )
    }
}