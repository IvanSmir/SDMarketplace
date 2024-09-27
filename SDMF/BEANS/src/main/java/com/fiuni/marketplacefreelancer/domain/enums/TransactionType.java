package com.fiuni.marketplacefreelancer.domain.enums;

public enum TransactionType {
    PAYPAL("Paypal"),
    CREDIT_CARD("Credit Card"),
    BANK_TRANSFER("Bank Transfer"),
    OTHER("Other");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }
}
