package com.originaldreams.proxycenter.controller;

import com.originaldreams.common.response.MyResponse;
import com.originaldreams.proxycenter.dto.LogonDTO;
import com.originaldreams.proxycenter.service.LogonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping(value = "/api")
@RestController
public class LogonController {

    @Autowired
    private LogonService logonService;

    private static final Logger logger = LoggerFactory.getLogger(LogonController.class);
    /**
     * 统一的登录接口
     *
     * @return
     */
    @RequestMapping(value = "/logon",method = RequestMethod.POST)
    public ResponseEntity logon(@Valid @RequestBody LogonDTO logonDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return MyResponse.badRequest();
        }

        return logonService.logon(logonDTO);

    }
}
