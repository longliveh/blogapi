package com.bobilwm.weibo.service;

import com.bobilwm.weibo.entity.blog.Blog;
import com.bobilwm.weibo.entity.blog.Comment;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Boolean addBlog(Blog blog);
    Long addComment(Comment comment);
    List<Blog> getBlogByUserid(Integer[] userids);
    Map getBlogCount(Integer blogid);
}
