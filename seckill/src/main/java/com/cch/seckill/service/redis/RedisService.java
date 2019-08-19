package com.cch.seckill.service.redis;

import com.cch.seckill.service.redis.key.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;

import static com.cch.seckill.util.ServiceUtils.beanToString;
import static com.cch.seckill.util.ServiceUtils.stringToBean;

@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    /**
     *获取当个对象
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        try(Jedis jedis = jedisPool.getResource()) {
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = stringToBean(str, clazz);
            return t;
        }
    }

    /**
     * 设置对象
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        try (Jedis jedis = jedisPool.getResource()) {
            String str = beanToString(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            String realKey = prefix.getPrefix() + key;
            int seconds = prefix.expireSeconds();
            if (seconds <= 0) {
                jedis.set(realKey, str);
            } else {
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }
    }

    public <T> boolean setNXEX(final KeyPrefix prefix, final String key, final T req) {
        if (req == null) {
            return false;
        }
        int expireSeconds = prefix.expireSeconds();
        if (expireSeconds == 0) {
            throw new RuntimeException("[SET EX NX]必须设置超时时间");
        }
        String realKey = prefix.getPrefix() + key;
        String value = beanToString(req);
        try (Jedis jedis = jedisPool.getResource()) {
            String ret = jedis.set(realKey, value, "nx", "ex", expireSeconds);
            return "OK".equals(ret);
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断key是否存在
     */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        try(Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        }
    }

    /**
     * 删除
     */
    public boolean delete(KeyPrefix prefix, String key) {
        try(Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            long ret = jedis.del(realKey);
            return ret > 0;
        }
    }

    /**
     * 增加值
     */
    public <T> Long incr(KeyPrefix prefix, String key) {
        try(Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        }
    }

    /**
     * 减少值
     */
    public <T> Long decr(KeyPrefix prefix, String key) {
        try(Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        }
    }

    public boolean delete(KeyPrefix prefix) {
        if (prefix == null) {
            return false;
        }
        List<String> keys = scanKeys(prefix.getPrefix());
        if (keys == null || keys.size() <= 0) {
            return false;
        }
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(keys.toArray(new String[0]));
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> scanKeys(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> keys = new ArrayList<>();
            String cursor = "0";
            ScanParams sp = new ScanParams();
            sp.match("*" + key + "*");
            sp.count(100);
            do {
                ScanResult<String> result = jedis.scan(cursor, sp);
                List<String> results = result.getResult();
                if (result != null && results.size() > 0) {
                    keys.addAll(results);
                }
                cursor = result.getStringCursor();
            } while (!cursor.equals("0"));
            return keys;
        }
    }

}
