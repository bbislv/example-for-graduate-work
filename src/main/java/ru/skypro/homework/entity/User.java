package ru.skypro.homework.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.skypro.homework.constant.EntityConstants;
import ru.skypro.homework.dto.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"password", "ads", "comments"})
@Entity
@Table(name = EntityConstants.TABLE_USERS)
public class User {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = EntityConstants.EMAIL_MAX_LENGTH)
    private String email;

    @Column(nullable = false, length = EntityConstants.PASSWORD_HASH_MAX_LENGTH)
    private String password;

    @Column(name = "first_name", nullable = false, length = EntityConstants.FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = EntityConstants.LAST_NAME_MAX_LENGTH)
    private String lastName;

    @Column(nullable = false, length = EntityConstants.PHONE_MAX_LENGTH)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = EntityConstants.ROLE_MAX_LENGTH)
    private Role role;

    @Column(length = EntityConstants.IMAGE_PATH_MAX_LENGTH)
    private String image;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Ad> ads = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}
