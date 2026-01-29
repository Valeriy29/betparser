package com.betparser.model;

import lombok.Data;

import java.util.List;

@Data
public class RegionDto {
    private long id;
    private String name;
    private List<LeagueDto> leagues;
}
