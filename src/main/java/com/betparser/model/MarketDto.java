package com.betparser.model;

import lombok.Data;

import java.util.List;

@Data
public class MarketDto {
    private String name;
    private List<RunnerDto> runners;
}
