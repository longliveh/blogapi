package com.bobilwm.weibo.controller.user;

import com.bobilwm.weibo.controller.respmsg.Result;
import com.bobilwm.weibo.controller.respmsg.ResultCode;
import com.bobilwm.weibo.entity.User;
import com.bobilwm.weibo.entity.blog.Blog;
import com.bobilwm.weibo.service.BlogService;
import com.bobilwm.weibo.service.CommentService;
import com.bobilwm.weibo.service.UserService;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    BlogService blogService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/likeComment")
    public Result likeComment(@RequestBody JSONObject json)
    {
        Integer commentid = json.getInt("commentid");
        Boolean isliked = json.getBoolean("isliked");
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        if (isliked==false)
        {
            if (blogService.likeBlogOrComment(user.getId(),commentid,1))
                return Result.success();
            else return Result.error(ResultCode.ERROR);
        }else if (isliked==true)
        {
            if (blogService.unlikeBlogOrComment(user.getId(),commentid,1))
                return Result.success();
            else return Result.error(ResultCode.ERROR);
        }
        return Result.error(ResultCode.ERROR);
    }

    @PostMapping(value = "/whocommentme")
    public Result whocommentme()
    {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return Result.success(commentService.getRecentComment(user.getId()));
    }

    @PostMapping(value = "/getcommentbyid")
    public Result getcommentbyid(@RequestBody JSONObject json)
    {
        Long id = json.getLong("id");
        return Result.success(commentService.getCommentById(id));
    }

    @PostMapping(value = "/getcomment")
    public Result getcomments(@RequestBody JSONObject json)
    {
        Integer orderby = json.getInt("orderby");
        Integer blogid = json.getInt("blogid");
        Long father = json.getLong("father");
        List comments = commentService.getComment(orderby,blogid,father);
        return Result.success(comments);
    }

    @PostMapping(value = "/getuserInfobyCom")
    public Result getuserInfobyCom(@RequestBody JSONObject json)
    {
        Long commentid = (Long) json.getLong("commentid");
        Optional<User> u = userService.findUserById(commentService.getUseridByCom(commentid));
        User user = u.get();
        JSONObject data = new JSONObject();
        data.put("nickname",user.getNickname());
        data.put("avatar",user.getAvatar());
        data.put("userid",user.getId());
        return Result.success(data);
    }

}
