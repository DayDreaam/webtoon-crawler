import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.Executor

@Configuration
@EnableScheduling  // @Scheduled 기능 활성화
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
    fun webClient(): WebClient {
        return WebClient.builder()
            .exchangeStrategies(
                ExchangeStrategies.builder()
                    .codecs { configure -> configure.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) }
                    .build())
            .build()
    }
}
