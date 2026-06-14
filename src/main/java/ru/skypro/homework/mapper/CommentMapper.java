package ru.skypro.homework.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.constant.ApiConstants;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.image", target = "authorImage")
    Comment toDto(ru.skypro.homework.entity.Comment comment);

    List<Comment> toDtoList(List<ru.skypro.homework.entity.Comment> comments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    ru.skypro.homework.entity.Comment toEntity(CreateOrUpdateComment createOrUpdateComment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    void updateEntityFromDto(
            CreateOrUpdateComment createOrUpdateComment,
            @MappingTarget ru.skypro.homework.entity.Comment comment);

    default Comments toCommentsDto(List<ru.skypro.homework.entity.Comment> comments) {
        List<ru.skypro.homework.entity.Comment> source =
                Optional.ofNullable(comments).orElse(Collections.emptyList());
        Comments result = new Comments();
        result.setCount(source.size());
        result.setResults(toDtoList(source));
        return result;
    }

    default Comments emptyCommentsDto() {
        Comments comments = new Comments();
        comments.setCount(ApiConstants.EMPTY_COUNT);
        comments.setResults(Collections.emptyList());
        return comments;
    }
}
