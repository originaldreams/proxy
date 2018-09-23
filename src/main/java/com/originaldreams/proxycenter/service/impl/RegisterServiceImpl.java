package com.originaldreams.proxycenter.service.impl;

import com.originaldreams.common.router.MyRouter;
import com.originaldreams.proxycenter.dto.RegisterDTO;
import com.originaldreams.proxycenter.service.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Override
    public ResponseEntity<?> register(RegisterDTO registerDTO) {

        logger.info("register  :" );

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> map = new HashMap<>();
        map.put("userName", registerDTO.getUserName());
        map.put("password", registerDTO.getPassword());
        map.put("verificationCode", registerDTO.getVerificationCode());
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(MyRouter.USER_MANAGER_REGISTER +
                "?userName={userName}&password={password}&verificationCode={verificationCode}",null,String.class,map);
        return  responseEntity;

    }
}
