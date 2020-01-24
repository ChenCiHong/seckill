package com.cch.seckill.service.query;

import com.cch.seckill.dao.SeckillUserDao;
import com.cch.seckill.domain.SeckillUser;
import com.cch.seckill.exception.GlobalException;
import com.cch.seckill.service.model.LoginVo;
import com.cch.seckill.service.redis.RedisService;
import com.cch.seckill.service.redis.key.SeckillUserKey;
import com.cch.seckill.service.result.CodeMsg;
import com.cch.seckill.util.MD5Util;
import com.cch.seckill.util.ServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class SeckillUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private SeckillUserDao seckillUserDao;
    @Autowired
    private RedisService redisService;

    public SeckillUser getById(Long id) {
        //取缓存
        SeckillUser user = redisService.get(SeckillUserKey.getById, "" + id, SeckillUser.class);
        if (user != null) {
            return user;
        }
        //取数据库
        user = seckillUserDao.getById(id);
        if (user != user) {
            redisService.set(SeckillUserKey.getById, "" + id, user);
        }
        return user;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPwd = loginVo.getPassword();
        SeckillUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPwd = user.getPassword();
        String saltDB = user.getSalt();
        String calcPwd = MD5Util.formPwdTODbPwd(formPwd, saltDB);
        if (!calcPwd.equals(dbPwd)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = ServiceUtils.UUID();
        addCookie(response, token, user);
        return null;
    }

    private void addCookie(HttpServletResponse response, String token, SeckillUser user) {
        redisService.set(SeckillUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SeckillUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public SeckillUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        SeckillUser user = redisService.get(SeckillUserKey.token, token, SeckillUser.class);
        // 延长有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }
}
