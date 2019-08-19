package com.cch.seckill.service.query;

import com.cch.seckill.exception.GlobalException;
import com.cch.seckill.service.model.LoginVo;
import com.cch.seckill.service.result.CodeMsg;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        return null;
    }

}
