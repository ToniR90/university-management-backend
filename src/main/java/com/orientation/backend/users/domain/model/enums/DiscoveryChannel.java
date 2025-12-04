package com.orientation.backend.users.domain.model.enums;

// TODO: Review with client in Sprint 2 - temporary values

public enum DiscoveryChannel {
    WEBSITE("Pàgina web"),
    SOCIAL_MEDIA("Xarxes socials"),
    REFERRAL("Recomanació"),
    UNIVERSITY_EVENT("Esdeveniment universitari"),
    EMAIL_CAMPAIGN("Campanya de correu"),
    OTHER("Altre");

    private final String displayName;

    DiscoveryChannel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
