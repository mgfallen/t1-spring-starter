package t1.holding.starter.unit;

import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import t1.holding.starter.interceptor.HttpLoggingInterceptor;

public class HttpLoggingInterceptorTest {
    private HttpLoggingInterceptor httpLoggingInterceptor;
    private final Logger logger = LoggerFactory.getLogger(HttpLoggingInterceptor.class);

    @BeforeEach
    public void setUp() {
        httpLoggingInterceptor = new HttpLoggingInterceptor();
    }

}
