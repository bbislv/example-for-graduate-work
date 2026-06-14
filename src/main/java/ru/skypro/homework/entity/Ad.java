package ru.skypro.homework.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.skypro.homework.constant.EntityConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"author", "comments"})
@Entity
@Table(name = EntityConstants.TABLE_ADS)
public class Ad {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = EntityConstants.TITLE_MAX_LENGTH)
    private String title;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = EntityConstants.DESCRIPTION_MAX_LENGTH)
    private String description;

    @Column(length = EntityConstants.IMAGE_PATH_MAX_LENGTH)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "ad", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}
