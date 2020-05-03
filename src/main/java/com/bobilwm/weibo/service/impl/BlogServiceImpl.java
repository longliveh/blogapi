package com.bobilwm.weibo.service.impl;

import com.bobilwm.weibo.entity.blog.Blog;
import com.bobilwm.weibo.entity.blog.Comment;
import com.bobilwm.weibo.entity.blog.LikeTo;
import com.bobilwm.weibo.repository.BlogRepository;
import com.bobilwm.weibo.repository.CommentRepository;
import com.bobilwm.weibo.repository.LikeToRepository;
import com.bobilwm.weibo.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    LikeToRepository likeToRepository;

    @Override
    public List<Blog> getBlogByUserid(Integer[] userids) {
        List<Blog> blogs = new ArrayList<>();
        for (int i = 0; i < userids.length; i++) {
            blogs.addAll(blogRepository.findAllByUseridOrderByDateDesc(userids[i]));
        }
        return blogs;
    }

    @Override
    public List<Blog> getBlog(Integer select,Integer userid,Integer pagination) {
        List<Blog> blogs = null;
        switch (select){
            case 0:         //热门
                blogs = blogRepository.findAllOrderByStarDesc(pagination);
                break;
            case 1:         //全部    按时间排序
                blogs = blogRepository.findAllByFocusOrderByDateDesc(userid);
                break;
            case 2:         //图片    按时间排序
                blogs = blogRepository.findAllByFocusAndMediaTypeOrderByDateDesc(userid,1);
                break;
            case 3:         //视频    按时间排序
                blogs = blogRepository.findAllByFocusAndMediaTypeOrderByDateDesc(userid,2);
                break;
            case 4:         //直播    按时间排序
                blogs = blogRepository.findAllByFocusAndMediaTypeOrderByDateDesc(userid,3);
                break;
            default:        //默认--全部    按时间排序
                blogs = blogRepository.findAllByFocusOrderByDateDesc(userid);
                break;
        }
        while (blogs.remove(null));
        return blogs;
    }

    @Override
    public Boolean addBlog(Blog blog) {
        Integer affectrow =  blogRepository.addUser(blog.getContent(),blog.getUserid(),blog.getMediaType(),blog.getUrlList(),blog.getDate());
        return affectrow>0;
    }

    @Override
    public Comment addComment(Comment comment) {
//        Integer affectrow = commentRepository.addComment(
//                comment.getBlogId(),comment.getContent(),comment.getLevel(),
//                comment.getFrom(),comment.getTo(),comment.getDate());
//        if (affectrow>0)
//        {
//
//        }
        Comment com = commentRepository.save(comment);
        return com;
    }

    @Override
    public Map getBlogDetail(Integer blogid) {
        Integer stars = blogRepository.getBlogStar(blogid);
        Integer commentcount = commentRepository.countCommentByBlogIdAndIsDelete(blogid,false);
        Integer sharecount = 0;
        Map<String,Integer> map = new HashMap();
        map.put("stars",stars);
        map.put("commentCount",commentcount);
        map.put("shareCount",sharecount);
        return map;
    }

    @Override
    public Boolean likeBlogOrComment(Integer self_userid,Integer toid, Integer type) {
        LikeTo likeTo = null;
        if (type == 0) {
            likeTo = LikeTo.LikeToBlog(self_userid, toid);
            LikeTo res = likeToRepository.save(likeTo);
            Integer affectrow = blogRepository.likeBlog(toid);
            if (res.getId() != 0 && affectrow > 0) {
                return true;
            }
        }
        if(type == 1)
        {
            likeTo = LikeTo.LikeToComment(self_userid,toid);
            LikeTo res = likeToRepository.save(likeTo);
            Integer affectrow = commentRepository.likeComment(toid);
            if (res.getId()!=0&&affectrow>0)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean unlikeBlogOrComment(Integer self_userid, Integer toid, Integer type) {
        LikeTo likeTo = null;
        if (type == 0) {

            likeTo = LikeTo.LikeToBlog(self_userid, toid);
            likeToRepository.deleteByFAndTAndType(likeTo.getF(),likeTo.getT(),likeTo.getType());
            Integer affectrow = blogRepository.unlikeBlog(toid);
            if (affectrow > 0) {
                return true;
            }
        }
        if(type == 1)
        {
            likeTo = LikeTo.LikeToComment(self_userid,toid);
            likeToRepository.deleteByFAndTAndType(likeTo.getF(),likeTo.getT(),likeTo.getType());
            Integer affectrow = commentRepository.unlikeComment(toid);
            if (affectrow>0)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer getBlogCount(Integer userid) {
        return blogRepository.countByUserid(userid);
    }

    @Override
    public Optional<Blog> getById(Integer blogid) {
        return blogRepository.findById(blogid);
    }
}
