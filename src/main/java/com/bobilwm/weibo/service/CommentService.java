package com.bobilwm.weibo.service;

import com.bobilwm.weibo.entity.blog.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getComment(Integer orderby, Integer blogid,Long father);
    Integer getUseridByCom(Long commentid);
}
