package com.bobilwm.weibo.service;

import com.bobilwm.weibo.entity.blog.Blog;
import com.bobilwm.weibo.entity.blog.Comment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BlogService {
    Boolean addBlog(Blog blog);
    Comment addComment(Comment comment);
    List<Blog> getBlogByUserid(Integer[] userids);
    Map getBlogDetail(Integer blogid);
    Integer getBlogCount(Integer userid);
    List<Blog> getBlog(Integer select,Integer userid,Integer pagination);
    Boolean likeBlogOrComment(Integer self_userid,Integer toid,Integer type);
    Boolean unlikeBlogOrComment(Integer self_userid,Integer toid,Integer type);
    Optional getById(Integer blogid);
}
