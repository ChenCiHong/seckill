package com.cch.seckill.dao;

import com.cch.seckill.domain.OrderInfo;
import com.cch.seckill.domain.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDao {

    void insert(OrderInfo orderInfo);

    void insertSeckillOrder(SeckillOrder seckillOrder);
}
