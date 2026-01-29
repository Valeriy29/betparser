package com.betparser.model;

public enum SportType {

    FOOTBALL("Football"),
    TENNIS("Tennis"),
    BASKETBALL("Basketball"),
    ICE_HOCKEY("Ice Hockey");

    private final String apiName;

    SportType(String apiName) {
        this.apiName = apiName;
    }

    public String getApiName() {
        return apiName;
    }

    public static SportType fromApiName(String name) {
        for (SportType type : values()) {
            if (type.apiName.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}
