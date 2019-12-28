package com.cch.seckill.dao;

import com.cch.seckill.domain.SeckillGoods;
import com.cch.seckill.service.model.GoodsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsDao {

    GoodsVo getGoodsById(long goodsId);

    int reduceStock(SeckillGoods seckillGoods);

    List<GoodsVo> goodsVoList();

    GoodsVo getGoodsVoByGoodsId(long goodsId);
}
