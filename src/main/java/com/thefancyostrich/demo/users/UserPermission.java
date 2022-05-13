package com.thefancyostrich.demo.users;

public enum UserPermission {
    // Typically we have the format resource:action.
    USER_MODIFY("user:modify"), // For deleiting and changing users.
    USER_READ("user:read"); // Reading users

    private final String permission;

    private UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
