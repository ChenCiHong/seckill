package com.cch.seckill.controller;

import com.cch.seckill.service.query.UserService;
import com.cch.seckill.service.model.LoginVo;
import com.cch.seckill.service.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> login(HttpServletResponse response, LoginVo loginVo) {
        String token = userService.login(response, loginVo);
        return Result.success(token);
    }

}
