package com.kyel.blog.service;

import com.kyel.blog.po.User;

public interface UserService {

    User checkUser(String username, String password);
}
