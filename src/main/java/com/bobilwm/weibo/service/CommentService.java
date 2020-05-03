package com.bobilwm.weibo.service;

import com.bobilwm.weibo.entity.blog.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getComment(Integer orderby, Integer blogid,Long father,Integer comment_p);
    Integer getUseridByCom(Long commentid);
    List<Comment> getRecentComment(Integer userid);
    Comment getCommentById(Long c_id);
    Boolean deleteComment(Long com_id);
}
