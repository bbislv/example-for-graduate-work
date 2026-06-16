package ru.skypro.homework.service;

import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

public interface UserService {

    User getCurrentUser(String email);

    UpdateUser updateCurrentUser(String email, UpdateUser updateUser);

    void updateCurrentUserImage(String email);
}
