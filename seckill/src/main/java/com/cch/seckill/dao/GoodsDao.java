package com.cch.seckill.dao;

import com.cch.seckill.domain.SeckillGoods;
import com.cch.seckill.service.model.GoodsVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsDao {

    GoodsVo getGoodsById(long goodsId);

    int reduceStock(SeckillGoods seckillGoods);
}
