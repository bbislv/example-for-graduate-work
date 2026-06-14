package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.constant.ApiConstants;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.factory.DtoFactory;

@Tag(name = "Комментарии")
@CrossOrigin(value = ApiConstants.FRONTEND_ORIGIN)
@RestController
@RequestMapping("/ads/{id}/comments")
public class CommentController {

    @Operation(summary = "Получение комментариев объявления", operationId = "getComments")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Comments.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping
    public ResponseEntity<Comments> getComments(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(DtoFactory.emptyComments());
    }

    @Operation(summary = "Добавление комментария к объявлению", operationId = "addComment")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Comment.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable("id") Integer id, @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        return ResponseEntity.ok(DtoFactory.emptyComment());
    }
}
