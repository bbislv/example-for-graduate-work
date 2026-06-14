package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Роль пользователя", allowableValues = {"USER", "ADMIN"})
public enum Role {
    USER, ADMIN
}
