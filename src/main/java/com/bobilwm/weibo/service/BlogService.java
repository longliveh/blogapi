package com.bobilwm.weibo.service;

import com.bobilwm.weibo.entity.blog.Blog;
import com.bobilwm.weibo.entity.blog.Comment;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Boolean addBlog(Blog blog);
    Long addComment(Comment comment);
    List<Blog> getBlogByUserid(Integer[] userids);
    Map getBlogDetail(Integer blogid);
    Integer getBlogCount(Integer userid);
    List<Blog> getBlogByRandom();
    Boolean likeBlogOrComment(Integer self_userid,Integer toid,Integer type);
    Boolean unlikeBlogOrComment(Integer self_userid,Integer toid,Integer type);
}
