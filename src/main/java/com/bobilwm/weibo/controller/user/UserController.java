package com.bobilwm.weibo.controller.user;

import com.bobilwm.weibo.config.shiro.ShiroRealm;
import com.bobilwm.weibo.controller.respmsg.Result;
import com.bobilwm.weibo.controller.respmsg.ResultCode;
import com.bobilwm.weibo.entity.Focus;
import com.bobilwm.weibo.entity.Role;
import com.bobilwm.weibo.entity.User;
import com.bobilwm.weibo.entity.blog.LikeTo;
import com.bobilwm.weibo.service.BlogService;
import com.bobilwm.weibo.service.UserService;
import com.bobilwm.weibo.service.impl.BlogServiceImpl;
import com.bobilwm.weibo.utils.FileUtil;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;


    @Value("${file.address}")
    private String uploadPath;

    @PostMapping(value = "/recentcomment")
    public Result recentcomment(){

        return  Result.success();
    }

    @PostMapping(value = "/getfocusorfanslist")
    public Result getfocusorfanslist(@RequestBody JSONObject json) {
        Integer userid = json.getInt("userid");
        Integer type =  json.getInt("type");
        List<Focus> list = userService.getFocusOrFans(userid,type);
        return Result.success(list);
    }

    @PostMapping(value = "/isfocus")
    public Result isfocus(@RequestBody JSONObject json) {
        Integer touserid = (Integer) json.get("userid");
        User myself = (User) SecurityUtils.getSubject().getPrincipal();
        Focus focus = new Focus(myself.getId(), touserid);
        if (userService.isFocus(focus)) {
            return Result.error(ResultCode.USER_FOCUS_TRUE);
        }
        return Result.error(ResultCode.USER_FOCUS_FALSE);
    }

    @PostMapping(value = "/wholikedme")
    public Result wholikedme(){
        User myself = (User) SecurityUtils.getSubject().getPrincipal();
        return Result.success(userService.whoLikedMe(myself.getId()));
    }

    @PostMapping(value = "/isliked")
    public Result isliked(@RequestBody JSONObject json) {
        Integer queryid = (Integer) json.get("queryid");
        Integer type = (Integer) json.get("type");
        User myself = (User) SecurityUtils.getSubject().getPrincipal();
        LikeTo likeTo = new LikeTo(myself.getId(),queryid,type);
        JSONObject res = new JSONObject();
        res.put("isliked",userService.isLiked(likeTo));
        return Result.success(res);
    }

    @PostMapping(value = "/focusornot")
    public Result focusornot(@RequestBody JSONObject json) {
        Integer touserid = (Integer) json.get("userid");
        Boolean focusOrNot = (Boolean) json.getBoolean("focus");
        Optional<User> u = userService.findUserById(touserid);
        User user = u.get();
        User myself = (User) SecurityUtils.getSubject().getPrincipal();
        if (user != null) {
            Focus focus = new Focus(myself.getId(), touserid);
            if (focusOrNot) {
                Focus focus1 = userService.addFocus(focus);
                return focus1 != null ? Result.success() : Result.error(ResultCode.USER_FOCUS_ERROR);
            } else {
                try {
                    userService.delFocus(focus);
                    return Result.success();
                } catch (Exception e) {
                    return Result.error(ResultCode.USER_NOT_EXIST);
                }
            }
        }
        return Result.error(ResultCode.USER_NOT_EXIST);
    }

    @PostMapping(value = "/getuserInfo")
    public Result getuserInfo(@RequestBody JSONObject json) {
        Integer userid = (Integer) json.get("userid");
        if (userid == -1 || userid == null) {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            userid = user.getId();
        }
        Optional<User> u = userService.findUserById(userid);
        User user = u.get();
        if (user != null) {
            JSONObject data = new JSONObject();
            data.put("nickname", user.getNickname());
            data.put("avatar", user.getAvatar());
            data.put("userid", user.getId());
            data.put("focus", userService.getFocusCount(userid));
            data.put("fans", userService.getFansCount(userid));
            data.put("blogs", blogService.getBlogCount(userid));
            return Result.success(data);
        }
        return Result.error(ResultCode.USER_NOT_EXIST);
    }


    //post登录
    @PostMapping(value = "/regist")
    public Result regist(@RequestBody User user) {
        try {
            Integer r = userService.addUser(user.getTel(), user.getEmail(), user.getPassword(), user.getNickname());
        } catch (Exception e) {
            return Result.error(ResultCode.USER_Register_ERROR);
        }
        return Result.success();

    }

    @PostMapping(value = "/changeavatar")
    public Result changeavatar(@RequestParam(value = "img") MultipartFile img) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        System.out.println(user);
        try {
            //上传目录地址
            String uploadDir = uploadPath + "avatar/";

            //如果目录不存在，自动创建文件夹
            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdir();
            }

            String filename = FileUtil.executeChangeAvatar(uploadDir, img, user.getAvatar());

            if (userService.changeAvatar(user.getId(), filename)) {
                user.setAvatar(filename);
                Subject subject = SecurityUtils.getSubject();
                PrincipalCollection principalCollection = subject.getPrincipals();
                String realmName = principalCollection.getRealmNames().iterator().next();
                PrincipalCollection newPrincipalCollection =
                        new SimplePrincipalCollection(user, realmName);
                subject.runAs(newPrincipalCollection);
                return Result.success();
            }
        } catch (Exception e) {
            //打印错误堆栈信息
            e.printStackTrace();
            return Result.error(ResultCode.SYSTEM_INNER_ERROR);
        }
        System.out.println();
        return Result.error(ResultCode.ERROR);
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
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Result.success();
    }

}
