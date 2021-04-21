package com.gmail.kirilllapitsky.finnhub.security.enumerable;

public enum Permissions {
    ADMINISTRATE("ADMINISTRATE"),
    TRACKING("TRACKING"),
    WIEWING("WIEWING");

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
