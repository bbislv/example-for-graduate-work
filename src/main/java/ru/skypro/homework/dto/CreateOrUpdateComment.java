package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "CreateOrUpdateComment", description = "Данные для создания или обновления комментария")
public class CreateOrUpdateComment {

    @Schema(description = "текст комментария", requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 8, maxLength = 64)
    private String text;
}
