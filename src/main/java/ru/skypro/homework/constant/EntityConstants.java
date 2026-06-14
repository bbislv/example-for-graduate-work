package ru.skypro.homework.constant;

public final class EntityConstants {

    public static final int EMAIL_MIN_LENGTH = 4;
    public static final int EMAIL_MAX_LENGTH = 32;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 16;
    public static final int PASSWORD_HASH_MAX_LENGTH = 255;
    public static final int FIRST_NAME_MAX_LENGTH = 16;
    public static final int LAST_NAME_MAX_LENGTH = 16;
    public static final int UPDATE_FIRST_NAME_MAX_LENGTH = 10;
    public static final int UPDATE_LAST_NAME_MAX_LENGTH = 10;
    public static final int PHONE_MAX_LENGTH = 20;
    public static final int ROLE_MAX_LENGTH = 10;
    public static final int IMAGE_PATH_MAX_LENGTH = 255;
    public static final int TITLE_MIN_LENGTH = 4;
    public static final int TITLE_MAX_LENGTH = 32;
    public static final int PRICE_MIN_VALUE = 0;
    public static final int PRICE_MAX_VALUE = 10_000_000;
    public static final int DESCRIPTION_MIN_LENGTH = 8;
    public static final int DESCRIPTION_MAX_LENGTH = 64;
    public static final int COMMENT_TEXT_MIN_LENGTH = 8;
    public static final int COMMENT_TEXT_MAX_LENGTH = 64;

    public static final String TABLE_USERS = "users";
    public static final String TABLE_ADS = "ads";
    public static final String TABLE_COMMENTS = "comments";

    private EntityConstants() {
    }
}
