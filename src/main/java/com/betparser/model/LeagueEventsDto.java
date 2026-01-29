package com.betparser.model;

import lombok.Data;

import java.util.List;

@Data
public class LeagueEventsDto {
    private List<EventDto> events;
}
