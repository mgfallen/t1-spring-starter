package t1.holding.starter.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import t1.holding.starter.configuration.LoggingProperties;

import java.io.IOException;
import java.util.function.Supplier;


@Slf4j
@Component
public class HttpLoggingFilter implements Filter {

    private final LoggingProperties loggingProperties;

    @Autowired
    public HttpLoggingFilter(LoggingProperties loggingProperties) {
        this.loggingProperties = loggingProperties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        long startTime = System.currentTimeMillis();

        logRequest(() -> httpRequest.getMethod(),
                () -> httpRequest.getRequestURI(),
                () -> getHeaders(httpRequest),
                () -> httpRequest.getQueryString());

        chain.doFilter(request, response);

        logResponse(() -> String.valueOf(httpResponse.getStatus()),
                () -> getHeaders(httpResponse),
                () -> (System.currentTimeMillis() - startTime) + "ms");
    }

    private void logRequest(Supplier<String> methodSupplier, Supplier<String> uriSupplier, Supplier<String> headersSupplier, Supplier<String> paramsSupplier) {
        StringBuilder logMessage = new StringBuilder("Incoming Request:");
        appendIfEnabled(loggingProperties.isLogRequestMethod(), logMessage, " method=", methodSupplier);
        appendIfEnabled(loggingProperties.isLogRequestUrl(), logMessage, " uri=", uriSupplier);
        appendIfEnabled(loggingProperties.isLogRequestHeaders(), logMessage, " headers=", headersSupplier);
        appendIfEnabled(loggingProperties.isLogRequestParams(), logMessage, " queryParams=", paramsSupplier);

        log.info(logMessage.toString());
    }

    private void logResponse(Supplier<String> statusSupplier, Supplier<String> headersSupplier, Supplier<String> durationSupplier) {
        StringBuilder logMessage = new StringBuilder("Outgoing Response:");
        appendIfEnabled(loggingProperties.isLogResponseStatus(), logMessage, " status=", statusSupplier);
        appendIfEnabled(loggingProperties.isLogResponseHeaders(), logMessage, " headers=", headersSupplier);
        appendIfEnabled(loggingProperties.isLogDuration(), logMessage, " duration=", durationSupplier);

        log.info(logMessage.toString());
    }

    private void appendIfEnabled(boolean isEnabled, StringBuilder builder, String label, Supplier<String> valueSupplier) {
        if (isEnabled) {
            builder.append(label).append(valueSupplier.get());
        }
    }

    private static String getHeaders(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames().asIterator().forEachRemaining(headerName ->
                headers.append(headerName).append("=").append(request.getHeader(headerName)).append("; ")
        );
        return headers.toString();
    }

    private String getHeaders(HttpServletResponse response) {
        StringBuilder headers = new StringBuilder();
        response.getHeaderNames().forEach(headerName ->
                headers.append(headerName).append("=").append(response.getHeader(headerName)).append("; ")
        );
        return headers.toString();
    }
}