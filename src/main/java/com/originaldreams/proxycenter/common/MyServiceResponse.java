package com.originaldreams.proxycenter.common;

/**
 * 规定Service统一的消息返回格式
 * 在Controller中统一返回ResponseEntity格式的数据，在ResponseEntity的body里，必须使用MyResponseObject格式的数据
 */
public class MyServiceResponse {

    private int success ;
    private Object data ;
    private String message ;

    public final static int success_code_success = 0;
    public final static int success_code_failed = 1;

    public MyServiceResponse() {
        this.success = success_code_success;
    }

    public MyServiceResponse(Object data) {
        this.success = success_code_success;
        this.data = data;
    }

    public MyServiceResponse(int success, String message) {
        this.success = success;
        this.message = message;
    }

    public MyServiceResponse(int success, Object data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
