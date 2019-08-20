package com.cch.seckill.service.rabbitmq;

import com.cch.seckill.domain.SeckillOrder;
import com.cch.seckill.domain.SeckillUser;
import com.cch.seckill.service.model.GoodsVo;
import com.cch.seckill.service.query.GoodsService;
import com.cch.seckill.service.query.OrderService;
import com.cch.seckill.service.query.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.cch.seckill.util.ServiceUtils.stringToBean;

@Service
public class MQReceiver {

    private final Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private SeckillService seckillService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receiver(String message) {
        log.info("receiver message:" + message);
        SeckillMessage seckillMessage = stringToBean(message, SeckillMessage.class);
        SeckillUser user = seckillMessage.getUser();
        long goodsId = seckillMessage.getGoodsId();

        GoodsVo goodsVo = goodsService.getGoodsById(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0) {
            return;
        }
        //判断是否已经秒杀成功
        SeckillOrder order = orderService.getOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        seckillService.seckill(user, goodsVo);
    }

}
