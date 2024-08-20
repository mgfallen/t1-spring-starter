package t1.holding.starter.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import t1.holding.starter.aspect.HttpLoggingAspect;
import t1.holding.starter.filter.HttpLoggingFilter;
import t1.holding.starter.interceptor.HttpLoggingInterceptor;

@Configuration
public class HttpLoggingAutoConfiguration {

//    @Bean
//    @ConditionalOnProperty(name = "logging.strategy", havingValue = "filter", matchIfMissing = true)
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public HttpLoggingFilter loggingFilter() {
//        return new HttpLoggingFilter();
//    }

    @Bean
    @ConditionalOnProperty(name = "logging.strategy", havingValue = "interceptor")
    public WebMvcConfigurer loggingInterceptorConfigurer(HttpLoggingInterceptor loggingInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(loggingInterceptor);
            }
        };
    }

    @Bean
    @ConditionalOnProperty(name = "logging.strategy", havingValue = "aop")
    public HttpLoggingAspect loggingAspect() {
        return new HttpLoggingAspect();
    }
}
