package com.cch.seckill.controller;

import com.cch.seckill.service.redis.RedisService;
import com.cch.seckill.service.redis.key.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class BaseController {

    @Value("#{'true'}")
    private boolean pageCacheEnable;

    @Autowired
    private RedisService redisService;
    @Autowired
    protected ThymeleafViewResolver thymeleafViewResolver;

    public String render(HttpServletRequest request, HttpServletResponse response,
                          Model model, String templateName, KeyPrefix prefix, String key) {
        if (!pageCacheEnable) {
            return templateName;
        }
        //取缓存
        String html = redisService.get(prefix, key, String.class);
        if (!StringUtils.isEmpty(html)) {
            out(response, html);
            return null;
        }
        WebContext wc = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process(templateName, wc);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(prefix, key, html);
        }
        out(response, html);
        return null;
    }

    public static void out(HttpServletResponse response, String html) {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (OutputStream out = response.getOutputStream()){
            out.write(html.getBytes("UTF-8"));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
