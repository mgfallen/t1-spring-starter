package t1.holding.starter.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import t1.holding.starter.filter.HttpLoggingFilter;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
public class HttpLoggingAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "http.logging.strategy", havingValue = "filter", matchIfMissing = true)
    public FilterRegistrationBean<HttpLoggingFilter> loggingFilter(LoggingProperties loggingProperties) {
        FilterRegistrationBean<HttpLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        HttpLoggingFilter filter = new HttpLoggingFilter(loggingProperties);
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
