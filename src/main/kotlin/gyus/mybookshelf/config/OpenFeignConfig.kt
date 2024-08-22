package gyus.mybookshelf.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackages = ["gyus.mybookshelf.request"])
class OpenFeignConfig