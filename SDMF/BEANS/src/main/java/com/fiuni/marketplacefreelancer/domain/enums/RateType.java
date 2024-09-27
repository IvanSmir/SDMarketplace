package com.fiuni.marketplacefreelancer.domain.enums;

import lombok.Getter;

@Getter
public enum RateType {
    HOUR("Rate per hour"),
    DAY("Rate per day"),
    WEEK("Rate per week"),
    MONTH("Rate per month"),
    PROJECT("Rate per project");

    private final String displayName;

    RateType(String displayName) {
        this.displayName = displayName;
    }
}
