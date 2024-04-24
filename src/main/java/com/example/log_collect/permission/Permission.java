package com.example.log_collect.permission;

public enum Permission {
    LOG_ACCESS("log:access");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
