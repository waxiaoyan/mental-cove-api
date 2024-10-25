package com.mental.cove.service;

import com.mental.cove.entity.User;
import com.mental.cove.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User getOrCreateUser(String name) {
        User user = userRepository.findByName(name);
        if (user == null) {
            user = new User(name);
            userRepository.save(user);
        }
        return user;
    }
}
