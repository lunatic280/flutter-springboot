package com.hagsig.Hagsig;
import com.google.gson.Gson;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cglib.core.Local;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import io.netty.handler.ssl.SslContextBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


//    public List<Map<String, String>> getAnnouncement() {
//        String url = "https://www.hannam.ac.kr/kor/community/community_01_1.html";
//        String selector = "a[href]";
//        Document doc = null;
//
//        try {
//            doc = Jsoup.connect(url).get(); // URL에 연결해서 문서를 가져온다.
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            return null; // 에러 발생 시 null 반환
//        }
//
//        Elements links = doc.select(selector);  // <a> 태그의 요소들 가져오기
//        List<Map<String, String>> announcementList = new ArrayList<>();  // 반환할 리스트
//
//        // 상위 5개 링크만 가져오기
//        int count = 0;
//        for (Element link : links) {
//            if (count >= 5) break;  // 5개를 초과하면 루프 종료
//
//            String linkHref = link.attr("href");  // 각 링크의 href 속성 가져오기
//            String linkText = link.text();        // 각 링크의 텍스트 가져오기
//
//            // 상대 경로를 절대 경로로 변환
//            if (!linkHref.startsWith("http")) {
//                linkHref = "https://www.hannam.ac.kr" + linkHref;
//            }
//
//            Map<String, String> linkData = new HashMap<>();
//            linkData.put("linkText", linkText);
//            linkData.put("linkHref", linkHref);
//            announcementList.add(linkData);
//            count++;  // 추가된 항목에 대해 카운트 증가
//        }
//
//        System.out.println(announcementList);  // 디버깅을 위해 콘솔에 출력
//        return announcementList;  // 5개 링크 반환
//    }

}
