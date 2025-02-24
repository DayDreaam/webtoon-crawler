
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@EnableAsync  // @Async 비동기 기능 활성화
@EnableScheduling  // @Scheduled 기능 활성화 (필요할 경우)
class AppConfig {

    @Bean
    fun taskExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 5  // 동시에 실행할 최대 스레드 개수
        executor.maxPoolSize = 10  // 최대 스레드 개수
        executor.queueCapacity = 20  // 대기열 크기
        executor.setThreadNamePrefix("Async-")
        executor.initialize()
        return executor
    }
}
