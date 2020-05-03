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
    @Query(value = "SELECT * FROM `comment` WHERE blog_id = ?1 AND father = ?2 AND is_delete = ?3 ORDER BY date DESC LIMIT ?4,10",nativeQuery = true)
    List<Comment> findAllByBlogIdAndFatherAndIsDeleteOrderByDateDesc(Integer blogId,Long father,Boolean isDelete,Integer comment_p);

    //查找一级评论 排序：点赞
    @Query(value = "SELECT * FROM `comment` WHERE blog_id = ?1 AND father = ?2 AND is_delete = ?3 ORDER BY star DESC LIMIT ?4,10",nativeQuery = true)
    List<Comment> findAllByBlogIdAndFatherAndIsDeleteOrderByStarDesc(Integer blogId,Long father,Boolean isDelete,Integer comment_p);

    Comment findById(Long tocomment);

    Integer countCommentByBlogIdAndIsDelete(Integer blogid,Boolean isDelete);

    @Modifying
    @Query(value = "UPDATE comment SET is_delete=1 WHERE id = ?1",nativeQuery = true)
    Integer deleteComment(Long com_id);

    @Modifying
    @Query(value = "UPDATE comment SET star = star+1 WHERE id = ?1",nativeQuery = true)
    Integer likeComment(Integer commentid);

    @Modifying
    @Query(value = "UPDATE comment SET star = star-1 WHERE id = ?1",nativeQuery = true)
    Integer unlikeComment(Integer commentid);

    @Query(value = "SELECT c.* FROM (SELECT id FROM `comment` WHERE from_user = ?1) r INNER JOIN `comment` c ON r.id = c.to_comment AND c.is_delete = 0 ORDER BY id DESC LIMIT 10" ,nativeQuery = true)
    List<Comment> whoCommentMe(Integer userid);
}
