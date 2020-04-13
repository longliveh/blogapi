package com.bobilwm.weibo.config.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class ShiroSessionListener implements SessionListener {

    private static final Logger logger = LoggerFactory.getLogger(ShiroSessionListener.class);

    @Resource(name = "sessionDAO")
    private RedisSessionDAO sessionDao;

    @Override
    public void onStart(Session session) {
        // 会话创建时触发
        logger.info("ShiroSessionListener session {} 被创建", session.getId());
    }

    @Override
    public void onStop(Session session) {
        sessionDao.delete(session);
        // 会话被停止时触发
        logger.info("ShiroSessionListener session {} 被销毁", session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        sessionDao.delete(session);
        //会话过期时触发
        logger.info("ShiroSessionListener session {} 过期", session.getId());
    }
}