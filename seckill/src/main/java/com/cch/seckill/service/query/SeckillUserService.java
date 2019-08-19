package com.cch.seckill.service.query;

import com.cch.seckill.dao.SeckillUserDao;
import com.cch.seckill.domain.SeckillUser;
import com.cch.seckill.service.redis.RedisService;
import com.cch.seckill.service.redis.key.SeckillUserKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillUserService {

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

}
