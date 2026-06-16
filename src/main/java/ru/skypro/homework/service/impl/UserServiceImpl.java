package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.exception.ResourceNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "Пользователь не найден";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public ru.skypro.homework.dto.User getCurrentUser(String email) {
        return userMapper.toDto(getUserEntity(email));
    }

    @Override
    @Transactional
    public UpdateUser updateCurrentUser(String email, UpdateUser updateUser) {
        ru.skypro.homework.entity.User user = getUserEntity(email);
        userMapper.updateEntityFromDto(updateUser, user);
        userRepository.save(user);
        UpdateUser result = new UpdateUser();
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setPhone(user.getPhone());
        return result;
    }

    @Override
    @Transactional
    public void updateCurrentUserImage(String email) {
        getUserEntity(email);
    }

    private ru.skypro.homework.entity.User getUserEntity(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
    }
}
