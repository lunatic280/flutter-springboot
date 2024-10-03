package com.hagsig.Hagsig;

import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import io.netty.handler.ssl.SslContextBuilder;

@Service
public class HagsigService {

    private final WebClient webClient;

    public HagsigService(WebClient.Builder webClientBuilder) {
        // SSL 인증서를 검증하지 않도록 설정된 HttpClient 생성
        HttpClient httpClient = HttpClient.create()
                .secure(spec -> spec.sslContext(SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)));

        // Reactor Netty의 HttpClient를 사용하여 WebClient 설정
        this.webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("https://my.hnu.kr")
                .build();
    }

    public String getJsonHagsig() {
        String endpoint = "/api/widget/internalWidget/selectSikdan?SIKDAN_DT=20241004&SIKDANG_GB=2";
        Mono<String> response = webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(String.class);
        return response.block();
    }

}
