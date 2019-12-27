package com.kyel.blog.service;

import com.kyel.blog.dao.UserRepository;
import com.kyel.blog.po.User;
import com.kyel.blog.util.SHA256Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, SHA256Utils.code(password));
        return user;
    }
}
