package com.bobilwm.weibo.service;

import com.bobilwm.weibo.entity.Focus;
import com.bobilwm.weibo.entity.User;
import com.bobilwm.weibo.entity.blog.LikeTo;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findUserById(Integer id);
    User findUserByTelOrEmail(String username);
    Integer addUser(String tel,String email, String password, String nickname);
    Integer getFocusCount(Integer userid);
    Integer getFansCount(Integer userid);
    Boolean isFocus(Focus focus);
    Focus addFocus(Focus focus);
    void delFocus(Focus focus);
    Boolean changeAvatar(Integer userid,String filename);
    Boolean changeName(Integer userid,String nickname);
    List<Focus> getFocusOrFans(Integer userid,Integer type);
    Boolean isLiked(LikeTo to);
    List<LikeTo> whoLikedMe(Integer userid);
}
