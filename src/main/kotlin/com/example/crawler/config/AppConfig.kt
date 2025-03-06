import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableScheduling  // @Scheduled 기능 활성화
class AppConfig {

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .exchangeStrategies(
                ExchangeStrategies.builder()
                    .codecs { configure -> configure.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) }
                    .build())
            .build()
    }

//    @Bean
//    fun failedWebtoonIds(): ConcurrentLinkedQueue<Long> {
//        return ConcurrentLinkedQueue()
//    }
}
