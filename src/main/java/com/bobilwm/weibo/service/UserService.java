package com.bobilwm.weibo.service;

import com.bobilwm.weibo.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserById(Integer id);
    User findUserByTelOrEmail(String username);
    Integer addUser(String tel,String email, String password, String nickname);
}
