package com.originaldreams.proxycenter.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


@RestController
@RequestMapping("/httpTest")
public class HttpTestController {
    @RequestMapping(value = "/testEnCode",method = RequestMethod.GET)
    public ResponseEntity testEnCode(String str){
        try {
            BASE64Encoder encoder = new BASE64Encoder();
            String result = encoder.encodeBuffer(str.getBytes("utf-8"));

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("出错了");

        }

    }
    @RequestMapping(value = "/testDeCode",method = RequestMethod.GET)
    public ResponseEntity testDeCode(String str){
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            String result = new String(decoder.decodeBuffer(str),"utf-8");
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("出错了");

        }

    }
}
