package com.bobilwm.weibo.service.impl;

import com.bobilwm.weibo.entity.blog.Blog;
import com.bobilwm.weibo.entity.blog.Comment;
import com.bobilwm.weibo.repository.BlogRepository;
import com.bobilwm.weibo.repository.CommentRepository;
import com.bobilwm.weibo.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<Blog> getBlogByUserid(Integer[] userids) {
        List<Blog> blogs = new ArrayList<>();
        for (int i = 0; i < userids.length; i++) {
            blogs.addAll(blogRepository.findAllByUseridOrderByDate(userids[i]));
        }
        return blogs;
    }

    @Override
    public Boolean addBlog(Blog blog) {
        Integer affectrow =  blogRepository.addUser(blog.getContent(),blog.getUserid(),blog.getMediaType(),blog.getUrlList(),blog.getDate());
        return affectrow>0;
    }

    @Override
    public Long addComment(Comment comment) {
//        Integer affectrow = commentRepository.addComment(
//                comment.getBlogId(),comment.getContent(),comment.getLevel(),
//                comment.getFrom(),comment.getTo(),comment.getDate());
//        if (affectrow>0)
//        {
//
//        }
        Comment com = commentRepository.save(comment);
        return com.getId();
    }

    @Override
    public Map getBlogCount(Integer blogid) {
        Integer stars = blogRepository.getBlogStar(blogid);
        Integer commentcount = commentRepository.countCommentByBlogId(blogid);
        Integer sharecount = 1;
        Map<String,Integer> map = new HashMap();
        map.put("stars",stars);
        map.put("commentCount",commentcount);
        map.put("shareCount",sharecount);
        return map;
    }
}
