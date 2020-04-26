package com.bobilwm.weibo.repository;

import com.bobilwm.weibo.entity.blog.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Modifying
    @Query(value = "INSERT INTO `comment`(blog_id,content,father,from,to,date) VALUES(?1,?2,?3,?4,?5,?6)",nativeQuery = true)
    Integer addComment(Integer blogId, String content, Long father, Integer from, Integer to, Date date);

    //查找一级评论 排序：时间
    List<Comment> findAllByBlogIdAndFatherOrderByDateDesc(Integer blogId,Long father);

    //查找一级评论 排序：点赞
    List<Comment> findAllByBlogIdAndFatherOrderByStarDesc(Integer blogId,Long father);

    Comment findById(Long tocomment);

    Integer countCommentByBlogId(Integer blogid);

    @Modifying
    @Query(value = "UPDATE comment SET star = star+1 WHERE id = ?1",nativeQuery = true)
    Integer likeComment(Integer commentid);

    @Modifying
    @Query(value = "UPDATE comment SET star = star-1 WHERE id = ?1",nativeQuery = true)
    Integer unlikeComment(Integer commentid);

    @Query(value = "SELECT 	c.*  FROM 	`comment` c 	INNER JOIN `comment` c1 ON c.id = c1.to_comment  WHERE 	c.from_user = ?1 UNION ALL SELECT 	c.*  FROM 	`comment` c 	INNER JOIN `blog` b ON c.blog_id = b.id  WHERE 	b.userid = ?1  ORDER BY 	id DESC LIMIT 10" ,nativeQuery = true)
    List<Comment> whoCommentMe(Integer userid);
}
