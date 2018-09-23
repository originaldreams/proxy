package com.originaldreams.proxycenter.controller;

import com.originaldreams.common.response.MyResponse;
import com.originaldreams.common.router.MyRouter;
import com.originaldreams.common.util.StringUtils;
import com.originaldreams.proxycenter.dto.RegisterDTO;
import com.originaldreams.proxycenter.service.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(value = "/api")
@RestController
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    RegisterService registerService;

    /**
     * 注册
     * @param userName 手机号或邮箱
     * @param password  密码
     * @param verificationCode 验证码
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ResponseEntity register(@Valid @RequestBody RegisterDTO registerDTO, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return MyResponse.badRequest();
        }
        return registerService.register(registerDTO);

    }
}
