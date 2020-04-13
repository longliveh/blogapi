package com.bobilwm.weibo.controller.user;

import com.bobilwm.weibo.controller.respmsg.Result;
import com.bobilwm.weibo.entity.User;
import com.bobilwm.weibo.entity.blog.Blog;
import com.bobilwm.weibo.service.CommentService;
import com.bobilwm.weibo.service.UserService;
import net.sf.json.JSONObject;
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
    private UserService userService;

    @PostMapping(value = "/getcomment")
    public Result getBlog(@RequestBody JSONObject json)
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
