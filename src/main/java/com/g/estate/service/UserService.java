package com.g.estate.service;

import com.g.estate.dao.UserRepository;
import com.g.estate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }
}
