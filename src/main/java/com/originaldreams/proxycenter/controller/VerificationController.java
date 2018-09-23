package com.originaldreams.proxycenter.controller;

import com.originaldreams.common.response.MyResponse;
import com.originaldreams.proxycenter.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api")
@RestController
public class VerificationController {

    @Autowired
    VerificationService verificationService;

    @RequestMapping(value = "/sendVerificationCode",method = RequestMethod.GET)
    public ResponseEntity sendVerificationCode(String phone){

        if (null == phone || phone.trim().isEmpty()) {
            return MyResponse.badRequest();
        }

        return verificationService.sendVerificationCode(phone);
    }
}
