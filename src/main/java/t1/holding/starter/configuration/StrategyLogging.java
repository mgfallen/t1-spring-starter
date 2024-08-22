package t1.holding.starter.configuration;

import lombok.Getter;

@Getter
public enum StrategyLogging {
    FILTER("filter"),
    ASPECT("aspect"),
    INTERCEPTOR("interceptor");

    private final String filter;

    StrategyLogging(String filter) {
        this.filter = filter;
    }
}
