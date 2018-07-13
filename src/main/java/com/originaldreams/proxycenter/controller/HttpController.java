package com.originaldreams.proxycenter.controller;

import com.originaldreams.proxycenter.common.MyClientRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/http")
public class HttpController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/get" , method = RequestMethod.GET)
    public ResponseEntity get(String methodName,String parameters){
        ResponseEntity<String> responseEntity;

        String routerUrl = getRouterUrlByMethodName(methodName);
        if(routerUrl == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("error:请求参数异常");
        }
        if(parameters == null){
            responseEntity = restTemplate.getForEntity(routerUrl,String.class);
        }else{
            responseEntity = restTemplate.getForEntity(routerUrl + "?" +
                            parameters.replace(":","=").replace(";","&")
                    ,String.class);
        }
        return responseEntity;
    }

    /**
     * POST请求不允许空参数
     * @param methodName 请求的方法名
     * @param parameters 请求参数
     * @return
     */
    @RequestMapping(value = "/post" , method = RequestMethod.POST)
    public ResponseEntity post(String methodName,String parameters){
        ResponseEntity<String> responseEntity;

        String routerUrl = getRouterUrlByMethodName(methodName);
        if(routerUrl == null || parameters == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("error:请求参数异常");
        }

        responseEntity = restTemplate.postForEntity(routerUrl + "?" +
                parameters.replace(":","=").replace(";","&")
                ,null,String.class);

        return responseEntity;
    }

    /**
     * 校验MethodName的合法性
     * @param methodName 合法返回true,不合法返回false
     * @return
     */
    private String  getRouterUrlByMethodName(String methodName){
        if(methodName == null){
            return null;
        }
        String routerUrl = MyClientRouter.routerMap.get(methodName);
        if(routerUrl == null || routerUrl.equals("")){
            return null;
        }
        return routerUrl;
    }
}
