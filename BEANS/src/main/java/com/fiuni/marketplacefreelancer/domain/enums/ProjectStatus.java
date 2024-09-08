package com.fiuni.marketplacefreelancer.domain.enums;

import lombok.Getter;

@Getter
public enum ProjectStatus {
    IN_PROGRESS("In Progress"),
    COMPLETE("Complete"),
    IN_BACKLOG("In Backlog");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }
}
