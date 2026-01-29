package com.betparser.service;

import com.betparser.client.LeonApiClient;
import com.betparser.model.LeagueDto;
import com.betparser.model.RegionLeagueDto;
import com.betparser.model.SportType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.AbstractMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SportLeagueService {

    private final LeonApiClient apiClient;

    public Mono<List<RegionLeagueDto>> getTopLeagues() {
        return apiClient.getSports()
                .map(sports -> sports.stream()
                        .map(s -> {
                            SportType type = SportType.fromApiName(s.getName());
                            if (type == null) return null;
                            return new AbstractMap.SimpleEntry<>(type, s);
                        })
                        .filter(Objects::nonNull)
                        .flatMap(e -> {
                            if (e.getValue().getRegions() == null) return Stream.empty();
                            return e.getValue().getRegions().stream()
                                    .filter(r -> r.getLeagues() != null)
                                    .flatMap(r -> r.getLeagues().stream()
                                            .filter(LeagueDto::isTop)
                                            .peek(l -> l.setSportType(e.getKey()))
                                            .map(l -> new RegionLeagueDto(r.getName(), l))
                                    );
                        })
                        .toList()
                );
    }

}
