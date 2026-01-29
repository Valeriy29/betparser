package com.betparser.service;

import com.betparser.client.LeonApiClient;
import com.betparser.model.EventDto;
import com.betparser.model.MarketDto;
import com.betparser.model.RunnerDto;
import com.betparser.model.SportType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final LeonApiClient leonApiClient;

    public Mono<Void> loadAndPrintEvents(SportType sportType, Long leagueId, String leagueName, String regionName) {
        return leonApiClient.getEvents(leagueId)
                .flatMapMany(Flux::fromIterable)
                .flatMap(dto -> Flux.fromIterable(dto.getEvents()))
                .take(2)
                .flatMap(
                        event ->
                                leonApiClient.getEvent(event.getId())
                                        .doOnNext(eventDto ->
                                                printEvent(sportType, regionName, leagueName, eventDto)
                                        ),
                        1
                )
                .then();
    }

    private void printEvent(SportType sportName, String regionName, String leagueName, EventDto event) {
        System.out.println();
        System.out.println(sportName + ", " + regionName + " " + leagueName);

        System.out.println("    " + event.getName() + ", " + formatUtc(event.getKickoff()) + ", " + event.getId());

        if (event.getMarkets() == null || event.getMarkets().isEmpty()) {
            return;
        }

        Map<String, List<MarketDto>> marketsByType =
                event.getMarkets().stream()
                        .filter(m -> m.getRunners() != null && !m.getRunners().isEmpty())
                        .collect(Collectors.groupingBy(m -> marketType(m.getName()), LinkedHashMap::new, Collectors.toList()));

        for (Map.Entry<String, List<MarketDto>> entry : marketsByType.entrySet()) {
            String marketType = entry.getKey();
            List<MarketDto> markets = entry.getValue();

            System.out.println("        " + marketType);

            for (MarketDto market : markets) {
                for (RunnerDto runner : market.getRunners()) {
                    System.out.println(
                            "            "
                                    + runner.getName()
                                    + ", " + runner.getPrice()
                                    + ", " + runner.getId()
                    );
                }
            }
        }
    }

    private String marketType(String marketName) {
        int idx = marketName.indexOf('(');
        return idx > 0 ? marketName.substring(0, idx).trim() : marketName.trim();
    }

    private String formatUtc(Long kickoff) {
        return Instant.ofEpochMilli(kickoff)
                .atZone(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"));
    }
}
