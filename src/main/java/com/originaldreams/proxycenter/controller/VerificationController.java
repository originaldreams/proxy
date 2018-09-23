package com.originaldreams.proxycenter.controller;

import com.originaldreams.common.response.MyResponse;
import com.originaldreams.common.router.MyRouter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@RequestMapping(value = "/api")
@RestController
public class VerificationController {

    @RequestMapping(value = "/sendVerificationCode",method = RequestMethod.GET)
    public ResponseEntity sendVerificationCode(String phone){
        try {
            logger.info("register  :" );
            if(phone == null || phone.isEmpty()){
                return MyResponse.badRequest();
            }
            Map<String, String> map = new HashMap<>();
            map.put("phone",phone);

            ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                    MyRouter.PUBLIC_SERVICE_SMS_SEND_VERIFICATIONCODE + "?phone={phone}",String.class,map);
            return  responseEntity;
        }catch (HttpClientErrorException e){
            logger.warn("HttpClientErrorException:" + e.getStatusCode());
            return getResponseFromException(e);
        }
    }
}
