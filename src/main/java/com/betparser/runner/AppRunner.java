package com.betparser.runner;

import com.betparser.model.SportType;
import com.betparser.service.EventService;
import com.betparser.service.SportLeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final SportLeagueService sportLeagueService;
    private final EventService eventService;

    @Override
    public void run(String... args) {
        List<SportType> sportOrder = List.of(
                SportType.FOOTBALL,
                SportType.TENNIS,
                SportType.ICE_HOCKEY,
                SportType.BASKETBALL
        );

        sportLeagueService.getTopLeagues()
                .flatMapMany(Flux::fromIterable)
                .groupBy(rl -> rl.getLeague().getSportType())
                .sort(Comparator.comparingInt(g -> sportOrder.indexOf(g.key())))
                .concatMap(groupedFlux ->
                        groupedFlux.flatMap(
                                rl -> eventService.loadAndPrintEvents(
                                        rl.getLeague().getSportType(),
                                        rl.getLeague().getId(),
                                        rl.getLeague().getName(),
                                        rl.getRegionName()
                                ),
                                3
                        )
                )
                .blockLast();
    }
}
