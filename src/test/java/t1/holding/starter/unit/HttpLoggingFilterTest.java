package t1.holding.starter.unit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import t1.holding.starter.configuration.LoggingProperties;
import t1.holding.starter.filter.HttpLoggingFilter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class HttpLoggingFilterTest {

    private LoggingProperties loggingProperties;
    private HttpLoggingFilter loggingFilter;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalSystemOut = System.out;


    @BeforeEach
    public void setUp() {
        loggingProperties = new LoggingProperties();
        loggingFilter = new HttpLoggingFilter(loggingProperties);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalSystemOut);
    }


    private void configureLoggingProperties(boolean requestMethod, boolean requestUrl, boolean requestHeaders,
                                            boolean requestParams, boolean responseStatus, boolean responseHeaders,
                                            boolean duration) {
        loggingProperties.setLogRequestMethod(requestMethod);
        loggingProperties.setLogRequestUrl(requestUrl);
        loggingProperties.setLogRequestHeaders(requestHeaders);
        loggingProperties.setLogRequestParams(requestParams);
        loggingProperties.setLogResponseStatus(responseStatus);
        loggingProperties.setLogResponseHeaders(responseHeaders);
        loggingProperties.setLogDuration(duration);
    }

    private void verifyLogContains(String... expectedContents) {
        String logs = outputStreamCaptor.toString();
        for (String content : expectedContents) {
            assertThat(logs).contains(content);
        }
    }

    private void executeFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        loggingFilter.doFilter(request, response, chain);
    }

    @Test
    public void testLogRequestMethod() throws ServletException, IOException {
        configureLoggingProperties(true, false, false, false, false, false, false);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/test");

        executeFilter(request, response, chain);

        verifyLogContains("Incoming Request: method=GET");
    }

    @Test
    public void testLogRequestUrl() throws ServletException, IOException {
        configureLoggingProperties(false, true, false, false, false, false, false);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/test");

        executeFilter(request, response, chain);

        verifyLogContains("Incoming Request: uri=/test");
    }

    @Test
    public void testLogRequestHeaders() throws ServletException, IOException {
        configureLoggingProperties(false, false, true, false, false, false, false);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeaderNames()).thenReturn(createMockEnumerationRequest("Header1"));
        when(request.getHeader("Header1")).thenReturn("Value1");

        executeFilter(request, response, chain);

        verifyLogContains("Incoming Request: headers=Header1=Value1");
    }

    @Test
    public void testLogRequestParams() throws ServletException, IOException {
        configureLoggingProperties(false, false, false, true, false, false, false);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getQueryString()).thenReturn("param1=value1");

        executeFilter(request, response, chain);

        verifyLogContains("Incoming Request: queryParams=param1=value1");
    }

    @Test
    public void testLogResponseStatus() throws ServletException, IOException {
        configureLoggingProperties(false, false, false, false, true, false, false);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(response.getStatus()).thenReturn(200);

        executeFilter(request, response, chain);

        verifyLogContains("Outgoing Response: status=200");
    }

    @Test
    public void testLogResponseHeaders() throws ServletException, IOException {
        configureLoggingProperties(false, false, false, false, false, true, false);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(response.getHeaderNames()).thenReturn(createMockEnumerationResponse("ResponseHeader1", "ResponseHeader2"));
        when(response.getHeader("ResponseHeader1")).thenReturn("ResponseValue1");
        when(response.getHeader("ResponseHeader2")).thenReturn("ResponseValue2");

        executeFilter(request, response, chain);

        verifyLogContains("Outgoing Response: headers=ResponseHeader1=ResponseValue1; ResponseHeader2=ResponseValue2");
    }

    @Test
    public void testLogDuration() throws ServletException, IOException {
        configureLoggingProperties(false, false, false, false, false, false, true);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(response.getStatus()).thenReturn(200);

        executeFilter(request, response, chain);

        verifyLogContains("Outgoing Response: duration=");
    }

    private Enumeration<String> createMockEnumerationRequest(String... elements) {
        List<String> list = Arrays.asList(elements);
        return Collections.enumeration(list);
    }

    private Collection<String> createMockEnumerationResponse(String... elements) {
        return Arrays.stream(elements).toList();
    }
}
