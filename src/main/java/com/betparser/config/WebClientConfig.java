package com.betparser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import javax.net.ssl.SSLException;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient leonWebClient() throws SSLException {
        return WebClient.builder()
                .baseUrl("https://leonbets.com")
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0")
                .exchangeStrategies(
                        ExchangeStrategies.builder()
                                .codecs(config ->
                                        config.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)
                                )
                                .build()
                )
                .build();
    }
}
