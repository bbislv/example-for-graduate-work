package ru.skypro.homework.constant;

public final class SecurityConstants {

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String USERS_BY_USERNAME_QUERY =
            "SELECT email, password, true AS enabled FROM users WHERE email = ?";
    public static final String AUTHORITIES_BY_USERNAME_QUERY =
            "SELECT email, 'ROLE_' || role AS authority FROM users WHERE email = ?";
    public static final String USER_EXISTS_SQL = "SELECT email FROM users WHERE email = ?";

    private SecurityConstants() {
    }
}
