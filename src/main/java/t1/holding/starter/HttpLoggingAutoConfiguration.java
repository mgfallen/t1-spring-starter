package t1.holding.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpLoggingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "http.logging.enabled", havingValue = "true", matchIfMissing = true)
    public HttpRequestResponseLoggingFilter httpRequestResponseLoggingFilter() {
        return new HttpRequestResponseLoggingFilter();
    }
}
