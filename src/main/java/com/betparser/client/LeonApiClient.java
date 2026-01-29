package com.betparser.client;

import com.betparser.model.EventDto;
import com.betparser.model.LeagueEventsDto;
import com.betparser.model.SportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LeonApiClient {

    private final WebClient leonWebClient;

    private static final String CTAG = "en-US";

    public Mono<List<SportDto>> getSports() {
        return leonWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("api-2/betline/sports")
                        .queryParam("ctag", CTAG)
                        .queryParam("flags", "urlv2")
                        .build())
                .retrieve()
                .bodyToFlux(SportDto.class)
                .collectList();
    }

    public Mono<List<LeagueEventsDto>> getEvents(Long leagueId) {
        return leonWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("api-2/betline/events/all")
                        .queryParam("ctag", CTAG)
                        .queryParam("league_id", leagueId)
                        .queryParam("hideClosed", true)
                        .queryParam("flags", "reg,urlv2,orn2,mm2,rrc,nodup")
                        .build())
                .retrieve()
                .bodyToFlux(LeagueEventsDto.class)
                .collectList();
    }

    public Mono<EventDto> getEvent(Long eventId) {
        return leonWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("api-2/betline/event/all")
                        .queryParam("ctag", CTAG)
                        .queryParam("eventId", eventId)
                        .queryParam("flags", "reg,urlv2,orn2,mm2,rrc,nodup,smgv2,outv2,wd3")
                        .build())
                .retrieve()
                .bodyToMono(EventDto.class);
    }
}
