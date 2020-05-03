package com.bobilwm.weibo.service.impl;

import com.bobilwm.weibo.entity.Focus;
import com.bobilwm.weibo.entity.Role;
import com.bobilwm.weibo.entity.User;
import com.bobilwm.weibo.entity.blog.LikeTo;
import com.bobilwm.weibo.repository.FocusRepository;
import com.bobilwm.weibo.repository.LikeToRepository;
import com.bobilwm.weibo.repository.UserRepository;
import com.bobilwm.weibo.service.UserService;
import com.bobilwm.weibo.utils.PasswordHelper;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bobilwm.weibo.utils.PasswordHelper.ALGORITHM_NAME;
import static com.bobilwm.weibo.utils.PasswordHelper.HASH_ITERATIONS;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FocusRepository focusRepository;

    @Autowired
    LikeToRepository likeToRepository;

    @Override
    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User findUserByTelOrEmail(String username) {
        return userRepository.findByTelOrEmail(username, username);
    }

    @Override
    public Integer addUser(String tel, String email, String password, String nickname) {

        String salt = PasswordHelper.randomNumberGenerator.nextBytes().toHex();
        String newPassword = new SimpleHash(ALGORITHM_NAME, password, ByteSource.Util.bytes(salt), HASH_ITERATIONS).toHex();
        Integer affected_row = userRepository.addUser(tel, email, newPassword, nickname, salt);
        if (affected_row > 0) {
            Integer userid = userRepository.getlast_insert_id();
            return userRepository.saveUser_Role(userid, 1);
        }
        return 0;
    }

    @Override
    public Integer getFocusCount(Integer userid) {
        return focusRepository.countByF(userid);
    }

    @Override
    public Integer getFansCount(Integer userid) {
        return focusRepository.countByT(userid);
    }

    @Override
    public Boolean isFocus(Focus focus) {
        return focusRepository.findByFAndT(focus.getF(), focus.getT()) != null;
    }

    @Override
    public Focus addFocus(Focus focus) {
        return focusRepository.save(focus);
    }

    @Override
    public void delFocus(Focus focus) {
        focusRepository.deleteByFAndAndT(focus.getF(), focus.getT());
    }

    @Override
    public Boolean changeAvatar(Integer userid, String filename) {
        return userRepository.changeAvatar(userid, filename) > 0;
    }

    @Override
    public Boolean changeName(Integer userid, String nickname) {
        return userRepository.changeNickname(userid,nickname)>0;
    }

    @Override
    public List<Focus> getFocusOrFans(Integer userid, Integer type) {
        if (type == 10) {
            return focusRepository.findAllByF(userid);
        }
        if (type == 20) {
            return focusRepository.findAllByT(userid);
        }
        return null;
    }

    @Override
    public Boolean isLiked(LikeTo likeTo) {
        return likeToRepository.findByFAndTAndType(likeTo.getF(), likeTo.getT(), likeTo.getType())!=null;
    }

    @Override
    public List<LikeTo> whoLikedMe(Integer userid) {
        return likeToRepository.getWhoLikedMe(userid);
    }
}
