package cn.violin.home.book.service;

import cn.violin.home.book.dao.UserRepository;
import cn.violin.home.book.entity.User;
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
