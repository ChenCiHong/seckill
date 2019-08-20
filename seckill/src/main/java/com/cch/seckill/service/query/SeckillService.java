package com.cch.seckill.service.query;

import com.cch.seckill.domain.OrderInfo;
import com.cch.seckill.domain.SeckillUser;
import com.cch.seckill.service.model.GoodsVo;
import com.cch.seckill.service.redis.RedisService;
import com.cch.seckill.service.redis.key.SeckillKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo seckill(SeckillUser user, GoodsVo goodsVo) {
        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goodsVo);
        if (success) {
            return orderService.createOrder(user, goodsVo);
        } else {
            setGoodsOver(goodsVo.getId());
            return null;
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver, "" + goodsId, true);
    }

}
