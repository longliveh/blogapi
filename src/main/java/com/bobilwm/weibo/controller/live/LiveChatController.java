package com.bobilwm.weibo.controller.live;

import com.bobilwm.weibo.controller.respmsg.Result;
import com.bobilwm.weibo.entity.User;
import com.bobilwm.weibo.entity.blog.Blog;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Map;


@RestController
@RequestMapping
public class LiveChatController {

    @Autowired
    LiveChatWs liveChatWs;

    @PostMapping(value = "/broadcast/{roomid}")
    public Result broadcast(@PathVariable(value = "roomid")String roomid, @RequestBody JSONObject json)
    {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        try {
//            for (int i = 0; i < 100; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",user.getNickname());
                jsonObject.put("avatar",user.getAvatar());
                jsonObject.put("msg",json.getString("msg"));
                LiveChatWs.broadcast(roomid,jsonObject.toString());
//                Thread.sleep(100);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success();
    }



}
