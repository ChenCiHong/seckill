package com.cch.seckill.service.query;

import com.cch.seckill.dao.GoodsDao;
import com.cch.seckill.domain.SeckillGoods;
import com.cch.seckill.service.model.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public GoodsVo getGoodsById(long goodsId) {
        return goodsDao.getGoodsById(goodsId);
    }

    public boolean reduceStock(GoodsVo goodsVo) {
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setGoodsId(goodsVo.getId());
        int ret = goodsDao.reduceStock(seckillGoods);
        return ret > 0;
    }

}
