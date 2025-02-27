import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.client.RestTemplate
import java.util.concurrent.Executor

@Configuration
@EnableAsync  // @Async 비동기 기능 활성화
@EnableScheduling  // @Scheduled 기능 활성화 (필요할 경우)
class AppConfig {

    @Bean
    fun taskExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 10
        executor.maxPoolSize = 20
        executor.queueCapacity = 50
        executor.setThreadNamePrefix("Async-")
        executor.initialize()
        return executor
    }

    @Bean
    fun restTemplate(): RestTemplate {
        val factory = SimpleClientHttpRequestFactory()
        factory.setConnectTimeout(3000) // 연결 대기 시간 (3초)
        factory.setReadTimeout(20000)   // 응답 대기 시간 (20초)
        return RestTemplate(factory)
    }
}
