package com.hagsig.Hagsig;

import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import io.netty.handler.ssl.SslContextBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class HagsigService {

    private final WebClient webClient;

    public HagsigService(WebClient.Builder webClientBuilder) {
        //Todo 배포하기전에 인증서 검증하는지 확인
        //Todo 안했으면 인즌서 검증하도록 변경
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

        //현재 날짜 가져오기
        LocalDateTime date = LocalDateTime.now();
        //포맷지정 yyyymmdd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String InputDate = date.format(formatter);

        String endpoint = String.format("/api/widget/internalWidget/selectSikdan?SIKDAN_DT=%s&SIKDANG_GB=2", InputDate);
        Mono<String> response = webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(String.class);
        return response.block();
    }

}
