package com.bobilwm.weibo.controller.user;

import com.bobilwm.weibo.controller.respmsg.Result;
import com.bobilwm.weibo.controller.respmsg.ResultCode;
import com.bobilwm.weibo.entity.Role;
import com.bobilwm.weibo.entity.User;
import com.bobilwm.weibo.service.UserService;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/getuserInfo")
    public Result getuserInfo(@RequestBody JSONObject json)
    {
        Integer userid = (Integer) json.get("userid");
        Optional<User> u = userService.findUserById(userid);
        User user = u.get();
        JSONObject data = new JSONObject();
        data.put("nickname",user.getNickname());
        data.put("avatar",user.getAvatar());
        data.put("userid",user.getId());
        return Result.success(data);
    }

    //post登录
    @PostMapping(value = "/regist")
    public Result regist(@RequestBody User user){
        try{
            Integer r = userService.addUser(user.getTel(), user.getEmail(), user.getPassword(), user.getNickname());
        }catch (Exception e)
        {
            return Result.error(ResultCode.USER_Register_ERROR);
        }
        return Result.success();

    }

    //post登录
    @PostMapping(value = "/login")
    public Result login(@RequestBody Map map) {
        Result r = new Result();

        //添加用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                map.get("email").toString(),
                map.get("password").toString());
        //进行验证，这里可以捕获异常，然后返回对应信息
        try {
            subject.login(usernamePasswordToken);
        } catch (IncorrectCredentialsException | UnknownAccountException e) {
            System.out.println("账号或密码错误");
            r.setResultCode(ResultCode.USER_LOGIN_ERROR);
            return r;
        }
//        boolean res = SecurityUtils.getSubject().isPermitted("we:roletest");
//        User user = (User) subject.getPrincipal();
        r.setResultCode(ResultCode.SUCCESS);
        return r;
    }

    @PostMapping("/roletest")
//    @RequiresRoles("user")
    public Result roletest() {
        System.out.println();
        return Result.success();
    }

    @PostMapping("/logout")
    public Result logout() {
        return Result.success();
    }

}
