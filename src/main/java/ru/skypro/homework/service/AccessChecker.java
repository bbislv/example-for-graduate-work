package ru.skypro.homework.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;

@Component
public class AccessChecker {

    public void checkAdAccess(Ad ad, User currentUser) {
        if (isAdmin(currentUser)) {
            return;
        }
        if (!ad.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Недостаточно прав для работы с объявлением");
        }
    }

    public void checkCommentAccess(Comment comment, User currentUser) {
        if (isAdmin(currentUser)) {
            return;
        }
        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Недостаточно прав для работы с комментарием");
        }
    }

    public boolean isAdmin(User user) {
        return Role.ADMIN.equals(user.getRole());
    }
}
