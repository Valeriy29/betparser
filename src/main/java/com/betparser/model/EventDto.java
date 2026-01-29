package com.betparser.model;

import lombok.Data;

import java.util.List;

@Data
public class EventDto {
    private Long id;
    private String name;
    private Long kickoff;
    private List<MarketDto> markets;
}
