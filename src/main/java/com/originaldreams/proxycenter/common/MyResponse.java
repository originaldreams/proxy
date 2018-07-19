package com.originaldreams.proxycenter.common;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 规定Controller统一的消息返回格式
 */
public class MyResponse {
    /**
     * 返回OK
     * @param object
     * @return
     */
    public static ResponseEntity ok(MyServiceResponse object){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(object);
    }

    /**
     * 异常请求
     * @param message
     * @return
     */
    public static ResponseEntity badRequest(String message){
        MyServiceResponse object = new MyServiceResponse(MyServiceResponse.success_code_failed,message);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(object);
    }
}
