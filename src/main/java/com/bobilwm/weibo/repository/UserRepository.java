package com.bobilwm.weibo.repository;

import com.bobilwm.weibo.entity.User;
import com.bobilwm.weibo.entity.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//JpaRepository<T,ID>：T 需要类型化为实体类(Entity)User，ID需要实体类User中Id（我定义的Id类型是Long）的类型
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByTelOrEmail(String tel,String email);


    /**
     * 新增用户添加user表
     * @param tel
     * @param email
     * @param password
     * @param nickname
     * @param salt
     * @return
     */
//    ;SELECT `LAST_INSERT_ID`();

    @Modifying
    @Query(value = "INSERT INTO `user`(email,nickname,`password`,tel,salt) VALUES(?2,?4,?3,?1,?5)",nativeQuery = true)
    Integer addUser(String tel,String email, String password, String nickname,String salt);

    @Query(value = "SELECT LAST_INSERT_ID()",nativeQuery = true)
    Integer getlast_insert_id();


    @Modifying
    @Query(value = "INSERT INTO user_role(user_id,role_id) VALUES(?1,?2)",nativeQuery = true)
    Integer saveUser_Role(Integer userid, Integer roleid);


    @Modifying
    @Query(value = "UPDATE `user` SET avatar = ?2 WHERE id = ?1",nativeQuery = true)
    Integer changeAvatar(Integer userid, String avatar);

    @Modifying
    @Query(value = "UPDATE `user` SET nickname = ?2 WHERE id = ?1",nativeQuery = true)
    Integer changeNickname(Integer userid, String nickname);

    @Query(value = "SELECT id FROM `user` WHERE nickname like ?1",nativeQuery = true)
    List<Integer> searchUserNickname(String str);
}
