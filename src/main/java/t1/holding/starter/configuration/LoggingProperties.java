package t1.holding.starter.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "http.logging")
@Data
@Configuration
public class LoggingProperties {
    private StrategyLogging strategy = StrategyLogging.ASPECT;

    private boolean logRequestMethod = true;
    private boolean logRequestUrl = true;
    private boolean logRequestHeaders = true;
    private boolean logRequestParams = true;
    private boolean logResponseHeaders = true;
    private boolean logResponseStatus = true;
    private boolean logResponseBody = true;
    private boolean logDuration = true;
}
