package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.ResourceNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AccessChecker;
import ru.skypro.homework.service.CommentService;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final String AD_NOT_FOUND = "Объявление не найдено";
    private static final String COMMENT_NOT_FOUND = "Комментарий не найден";
    private static final String USER_NOT_FOUND = "Пользователь не найден";

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final AccessChecker accessChecker;

    @Override
    @Transactional(readOnly = true)
    public Comments getCommentsByAdId(Integer adId) {
        getAdEntity(adId);
        return commentMapper.toCommentsDto(commentRepository.findAllByAdId(adId));
    }

    @Override
    @Transactional
    public Comment addComment(Integer adId, String email, CreateOrUpdateComment createOrUpdateComment) {
        ru.skypro.homework.entity.Ad ad = getAdEntity(adId);
        ru.skypro.homework.entity.User author = getUserEntity(email);
        ru.skypro.homework.entity.Comment comment = commentMapper.toEntity(createOrUpdateComment);
        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setCreatedAt(Instant.now().toEpochMilli());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public Comment updateComment(
            Integer adId,
            Integer commentId,
            String email,
            CreateOrUpdateComment createOrUpdateComment) {
        ru.skypro.homework.entity.User currentUser = getUserEntity(email);
        ru.skypro.homework.entity.Comment comment = getCommentEntity(adId, commentId);
        accessChecker.checkCommentAccess(comment, currentUser);
        commentMapper.updateEntityFromDto(createOrUpdateComment, comment);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(Integer adId, Integer commentId, String email) {
        ru.skypro.homework.entity.User currentUser = getUserEntity(email);
        ru.skypro.homework.entity.Comment comment = getCommentEntity(adId, commentId);
        accessChecker.checkCommentAccess(comment, currentUser);
        commentRepository.delete(comment);
    }

    private ru.skypro.homework.entity.Ad getAdEntity(Integer adId) {
        return adRepository.findById(adId)
                .orElseThrow(() -> new ResourceNotFoundException(AD_NOT_FOUND));
    }

    private ru.skypro.homework.entity.Comment getCommentEntity(Integer adId, Integer commentId) {
        return commentRepository.findByIdAndAdId(commentId, adId)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND));
    }

    private ru.skypro.homework.entity.User getUserEntity(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
    }
}
