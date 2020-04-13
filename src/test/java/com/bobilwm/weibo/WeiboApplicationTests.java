package com.bobilwm.weibo;

import com.bobilwm.weibo.entity.Role;
import com.bobilwm.weibo.entity.User;
import com.bobilwm.weibo.repository.RoleRepository;
import com.bobilwm.weibo.repository.UserRepository;
import com.bobilwm.weibo.service.UserService;
import com.bobilwm.weibo.utils.PasswordHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class WeiboApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Test
    void adduser() {
        Role user = new Role("user");
        Role admin = new Role("admin");

        User user1 = new User();
        user1.setEmail("123");
        user1.setNickname("123");
        user1.setPassword("123");
        user1.setSalt("123");
        user1.getRoles().addAll(Arrays.asList(user,admin));

        User user2 = new User();
        user2.setEmail("456");
        user2.setNickname("456");
        user2.setPassword("456");
        user2.setSalt("456");
        user2.getRoles().addAll(Arrays.asList(user,admin));

        userRepository.saveAll(Arrays.asList(user1,user2));


    }
    @Transactional
    @Test
    void re ()
    {
        Integer rs = userService.addUser(null,"123","123","123");
        User user = userRepository.findById(1).get();
        System.out.println(rs);

    }

    @Transactional
    @Test
    void qq()
    {
        userRepository.addUser(null,"123","123","123","123");
        int x = 1/0;
    }


    @Transactional
    @Test
    void finduser() {
        re();
        User user = userRepository.findById(1).get();
        System.out.println(user);
    }

    @Test
    void str()
    {
        List<String> list = new ArrayList<>();
        list.add("fda");
        list.add("fda");
        list.add("fda");
        list.add("fda");
        list.add("fda");
        System.out.println(list);
        List<String> newlist = Arrays.asList(list.toString());
        System.out.println(newlist);
    }



}
