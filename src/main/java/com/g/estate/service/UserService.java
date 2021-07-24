package com.g.estate.service;

import com.g.estate.dao.UserRepository;
import com.g.estate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserById(String id) {
        return new User();
    }

    public User findByUsername(User user) {
        return userRepository.findByUsername(user.getUsername());
    };
}
