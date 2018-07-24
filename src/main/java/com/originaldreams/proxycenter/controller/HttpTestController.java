package com.originaldreams.proxycenter.controller;

import com.originaldreams.common.MyBase64Utils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/httpTest")
public class HttpTestController {
    @RequestMapping(value = "/testEnCode",method = RequestMethod.GET)
    public ResponseEntity testEnCode(String str){
        try {

            String result = MyBase64Utils.encode(str);

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("出错了");

        }

    }
    @RequestMapping(value = "/testDeCode",method = RequestMethod.GET)
    public ResponseEntity testDeCode(String str){
        try {
            String result = MyBase64Utils.decode(str);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("出错了");

        }

    }
}
