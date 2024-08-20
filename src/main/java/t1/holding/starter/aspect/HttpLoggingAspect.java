package t1.holding.starter.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Enumeration;

@Aspect
@Component
@Slf4j
public class HttpLoggingAspect {

    @Around("execution(* org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(..)) && args(request, response, ..)")
    public Object logHttpRequestResponse(ProceedingJoinPoint joinPoint, HttpServletRequest request, HttpServletResponse response) throws Throwable {

        long startTime = System.currentTimeMillis();

        logRequest(request);

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - startTime;

        logResponse(response, duration);

        return result;
    }

    private void logRequest(HttpServletRequest request) {
        log.info("Incoming Request: method={}, uri={}, headers={}",
                request.getMethod(), request.getRequestURI(), getHeaders(request));
    }

    private void logResponse(HttpServletResponse response, long duration) {
        log.info("Outgoing Response: status={}, headers={}, duration={}ms",
                response.getStatus(), getHeaders(response), duration);
    }

    private String getHeaders(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.append(headerName).append("=").append(request.getHeader(headerName)).append("; ");
        }
        return headers.toString();
    }

    private String getHeaders(HttpServletResponse response) {
        StringBuilder headers = new StringBuilder();
        for (String headerName : response.getHeaderNames()) {
            headers.append(headerName).append("=").append(response.getHeader(headerName)).append("; ");
        }
        return headers.toString();
    }
}

