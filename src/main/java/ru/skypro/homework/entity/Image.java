package ru.skypro.homework.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skypro.homework.constant.EntityConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = EntityConstants.TABLE_IMAGES)
public class Image {

    @EqualsAndHashCode.Include
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "file_name", nullable = false, length = EntityConstants.IMAGE_PATH_MAX_LENGTH)
    private String fileName;

    @Column(name = "media_type", nullable = false, length = 50)
    private String mediaType;
}
