package com.betparser.model;

import lombok.Data;

@Data
public class LeagueDto {
    private long id;
    private String name;
    private boolean top;
    private SportType sportType;
}
