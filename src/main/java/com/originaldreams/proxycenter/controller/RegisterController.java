package com.originaldreams.proxycenter.controller;

import com.originaldreams.common.response.MyResponse;
import com.originaldreams.common.router.MyRouter;
import com.originaldreams.common.util.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@RequestMapping(value = "/api")
@RestController
public class RegisterController {

    /**
     * 注册
     * @param userName 手机号或邮箱
     * @param password  密码
     * @param verificationCode 验证码
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ResponseEntity register(String userName, String password, String verificationCode){
        try {
            logger.info("register  :" );
            if(StringUtils.isEmpty(userName,password,verificationCode)){
                return MyResponse.badRequest();
            }
            Map<String, String> map = new HashMap<>();
            map.put("userName",userName);
            map.put("password",password);
            map.put("verificationCode",verificationCode);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(MyRouter.USER_MANAGER_REGISTER +
                    "?userName={userName}&password={password}&verificationCode={verificationCode}",null,String.class,map);
            return  responseEntity;
        }catch (HttpClientErrorException e){
            logger.warn("HttpClientErrorException:" + e.getStatusCode());
            return getResponseFromException(e);
        }
    }
}
