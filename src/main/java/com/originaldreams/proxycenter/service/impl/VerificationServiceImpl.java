package com.originaldreams.proxycenter.service.impl;

import com.originaldreams.common.response.MyResponse;
import com.originaldreams.common.router.MyRouter;
import com.originaldreams.proxycenter.service.VerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationServiceImpl implements VerificationService {

    private static final Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);

    @Override
    public ResponseEntity<?> sendVerificationCode(String phone) {
        logger.info("register  :" );
        if(phone == null || phone.isEmpty()){
            return MyResponse.badRequest();
        }
        Map<String, String> map = new HashMap<>();
        map.put("phone",phone);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                MyRouter.PUBLIC_SERVICE_SMS_SEND_VERIFICATIONCODE + "?phone={phone}",String.class,map);
        return  responseEntity;
    }
}
