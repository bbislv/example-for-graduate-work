package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager userDetailsManager;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean login(String userName, String password) {
        if (!userRepository.existsByEmail(userName)) {
            return false;
        }
        UserDetails userDetails = userDetailsManager.loadUserByUsername(userName);
        return passwordEncoder.matches(password, userDetails.getPassword());
    }

    @Override
    @Transactional
    public boolean register(Register register) {
        if (userRepository.existsByEmail(register.getUsername())) {
            return false;
        }
        User user = userMapper.toEntity(register);
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public boolean changePassword(String email, String currentPassword, String newPassword) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(currentPassword, user.getPassword()))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }
}
