package com.cch.seckill.service.redis.key;

public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();

}
