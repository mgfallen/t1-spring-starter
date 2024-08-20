//package t1.holding.starter;
//
//import jakarta.servlet.FilterChain;
//import org.junit.jupiter.api.Test;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import t1.holding.starter.filter.HttpLoggingFilter;
//
//import static org.mockito.Mockito.*;
//
//class HttpRequestReponseLoggingFilterTest {
//
//    @Test
//    void testLogging() throws Exception {
//        HttpLoggingFilter filter = new HttpLoggingFilter();
//
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        FilterChain chain = mock(FilterChain.class);
//
//        filter.doFilter(request, response, chain);
//
//        verify(chain, times(1)).doFilter(request, response);
//    }
//}
