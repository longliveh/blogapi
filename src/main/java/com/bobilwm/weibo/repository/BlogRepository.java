package com.bobilwm.weibo.repository;

import com.bobilwm.weibo.entity.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Integer> {
    @Modifying
    @Query(value = "INSERT INTO `blog`(content,userid,media_type,url_list,`date`) VALUES(?1,?2,?3,?4,?5)",nativeQuery = true)
    Integer addUser(String content, Integer userid, Short mediaType,String url_list, Date date);

    @Modifying
    @Query(value = "INSERT INTO `comment`(blog_id,content,level,from,to,date) VALUES(?1,?2,?3,?4,?5,?6)",nativeQuery = true)
    Integer addComment(Integer blogId,String content,Integer level,Integer from,Integer to,Date date);

    List<Blog> findAllByUseridOrderByDate(Integer userId);

    @Query(value = "SELECT star FROM `blog` WHERE id = ?1",nativeQuery = true)
    Integer getBlogStar(Integer blogig);

    @Modifying
    @Query(value = "UPDATE blog SET star = star+1 WHERE id = ?1",nativeQuery = true)
    Integer likeBlog(Integer blogid);

    @Modifying
    @Query(value = "UPDATE blog SET star = star-1 WHERE id = ?1",nativeQuery = true)
    Integer unlikeBlog(Integer blogid);

    Integer countByUserid(Integer userid);

}
