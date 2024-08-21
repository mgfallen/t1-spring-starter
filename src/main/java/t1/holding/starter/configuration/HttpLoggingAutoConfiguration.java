package t1.holding.starter.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import t1.holding.starter.aspect.HttpLoggingAspect;
import t1.holding.starter.filter.HttpLoggingFilter;
import t1.holding.starter.interceptor.HttpLoggingInterceptor;

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
