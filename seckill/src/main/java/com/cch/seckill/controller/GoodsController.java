package com.cch.seckill.controller;

import com.cch.seckill.domain.SeckillUser;
import com.cch.seckill.service.model.GoodsDetailVo;
import com.cch.seckill.service.model.GoodsVo;
import com.cch.seckill.service.query.GoodsService;
import com.cch.seckill.service.redis.key.GoodsKey;
import com.cch.seckill.service.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/list")
    public String getGoodsList(HttpServletRequest request, HttpServletResponse response, Model model, SeckillUser user) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsVoList = goodsService.goodsVoList();
        model.addAttribute("goodsVoList", goodsVoList);
        return render(request, response, model, "goods_list", GoodsKey.getGoodsList, "");
    }

    @RequestMapping("/detail/{goodsId}")
    public Result<GoodsDetailVo> getGoodsDetail(HttpServletRequest request, HttpServletResponse response,
                                                Model model, SeckillUser user, @PathVariable("goodsId") long goodsId) {
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        long start = goodsVo.getStartDate().getTime();
        long end = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int seckillStatus = 0;
        int remainSeconds = 0;
        if (now < start) {
            //秒杀还没开始
            seckillStatus = 0;
        } else if (now > end) {
            //秒杀已结束
            seckillStatus = 1;
        }
        return null;
    }

}
