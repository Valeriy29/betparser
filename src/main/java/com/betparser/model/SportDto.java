package com.betparser.model;

import lombok.Data;

import java.util.List;

@Data
public class SportDto {
    private long id;
    private String name;
    private List<RegionDto> regions;
}
