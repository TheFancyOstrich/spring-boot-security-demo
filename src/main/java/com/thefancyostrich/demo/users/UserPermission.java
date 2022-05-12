package com.thefancyostrich.demo.users;

public enum UserPermission {
    // Typically we have the format resource:action.
    TEST_ADMIN("test:admin"),
    TEST_MODERATOR("test:moderator"),
    TEST_USER("test:user");

    private final String permission;

    private UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
