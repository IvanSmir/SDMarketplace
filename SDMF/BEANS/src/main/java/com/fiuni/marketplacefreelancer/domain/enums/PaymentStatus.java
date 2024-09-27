package com.fiuni.marketplacefreelancer.domain.enums;

public enum PaymentStatus {
    PENDING("Pending"),
    COMPLETE("Complete"),
    FAILED("Failed");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }
}
