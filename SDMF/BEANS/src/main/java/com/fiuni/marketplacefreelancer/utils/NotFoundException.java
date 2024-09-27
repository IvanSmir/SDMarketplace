package com.fiuni.marketplacefreelancer.utils;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, String id) {
        super("No " + message + " found with " + id);
    }
}
