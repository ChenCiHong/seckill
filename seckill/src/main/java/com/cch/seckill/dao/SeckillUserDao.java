package com.cch.seckill.dao;

import com.cch.seckill.domain.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SeckillUserDao {

    @Select("select * from seckill_user where id = #{id}")
    SeckillUser getById(@Param("id") Long id);

}
