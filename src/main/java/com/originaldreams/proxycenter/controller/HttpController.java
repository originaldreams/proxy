package com.originaldreams.proxycenter.controller;

import com.originaldreams.common.encryption.MyBase64Utils;
import com.originaldreams.common.response.MyResponse;
import com.originaldreams.common.response.MyServiceResponse;
import com.originaldreams.common.router.MyRouter;
import com.originaldreams.common.router.MyRouterObject;
import com.originaldreams.common.util.StringUtils;
import com.originaldreams.proxycenter.cache.CacheUtils;
import com.originaldreams.proxycenter.dto.HttpParametersDTO;
import com.originaldreams.proxycenter.service.HttpService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 杨凯乐
 * @date   2018-07-28 19:00:16
 */
@RestController
@RequestMapping("/api")
public class HttpController {

    private static final Logger logger = LoggerFactory.getLogger(HttpController.class);

    @Autowired
    HttpService httpService;

    /**
     * 针对一般用户所有get请求的转发
     * 请求格式如：localhost:8805?methodName=USER_MANAGER_PERMISSION_GET_ROLES_BY_ROUTER_ID&parameters=routerId:MTAwMDE=
     * 特点：当查询条件为用户id时，不用上传用户id
     * 1.鉴权
     * 2.转发
     * 3.针对错误返回码（401、403等）转处理为不同的应答
     * @param methodName    方法名
     * @param parameters    参数 格式：key1:base64(value1);key2:base64(value2) 如：routerId:MTAwMDE=
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity get(@Valid @RequestBody HttpParametersDTO httpParametersDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return MyResponse.badRequest();
        }

        return httpService.get(httpParametersDTO);

    }
    /**
     * POST请求不允许空参数
     * @param methodName 请求的方法名
     * @param parameters 请求参数
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity post(String methodName,String parameters){
        if(methodName == null || parameters == null){
            return MyResponse.badRequest();
        }
        String routerUrl = authenticateAndReturnRouterUrl(MyRouter.REQUEST_METHOD_POST,methodName);
        if(routerUrl == null){
            return MyResponse.forbidden();
        }
        ResponseEntity<String> responseEntity;
        try{
            Map<String,Object> map ;
            if(isManager()){
                routerUrl = routerUrl + getUrlParameters(parameters);
                map = parseMap(parameters);
            }else{
                routerUrl = routerUrl + getUrlParametersWithUserId(parameters);
                map = parseMapWithUserId(parameters);
            }
            logger.info("post  methodName:" + methodName + ",url:" + routerUrl);
            responseEntity = restTemplate.postForEntity(routerUrl,null,String.class,map);
        }catch (HttpClientErrorException e){
            logger.warn("HttpClientErrorException:" + e.getStatusCode());
            return getResponseFromException(e);
        }catch (Exception e){
            return MyResponse.badRequest();
        }
        return responseEntity;
    }

    /**
     * DELETE请求不允许参数为空
     * @param methodName
     * @param parameters
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity delete(String methodName,String parameters){
        if(methodName == null || parameters == null){
            return MyResponse.badRequest();
        }
        String routerUrl = authenticateAndReturnRouterUrl(MyRouter.REQUEST_METHOD_DELETE,methodName);
        if(routerUrl == null){
            return MyResponse.forbidden();
        }
        try{
            Map<String,Object> map ;
            if(isManager()){
                routerUrl = routerUrl + getUrlParameters(parameters);
                map = parseMap(parameters);
            }else{
                routerUrl = routerUrl + getUrlParametersWithUserId(parameters);
                map = parseMapWithUserId(parameters);
            }
            logger.info("delete methodName:" + methodName + ",url:" + routerUrl);
            restTemplate.delete(routerUrl,map);
        }catch (HttpClientErrorException e){
            logger.warn("HttpClientErrorException:" + e.getStatusCode());
            return getResponseFromException(e);
        }catch (Exception e){
            return MyResponse.badRequest();
        }
        return MyResponse.ok(new MyServiceResponse("删除成功"));
    }

    /**
     * PUT不允许空参数
     * @param methodName
     * @param parameters
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity put(String methodName,String parameters){
        if(methodName == null || parameters == null){
            return MyResponse.badRequest();
        }
        String routerUrl = authenticateAndReturnRouterUrl(MyRouter.REQUEST_METHOD_PUT,methodName);
        if(routerUrl == null){
            return MyResponse.forbidden();
        }
        try{
            Map<String,Object> map ;
            if(isManager()){
                routerUrl = routerUrl + getUrlParameters(parameters);
                map = parseMap(parameters);
            }else{
                routerUrl = routerUrl + getUrlParametersWithUserId(parameters);
                map = parseMapWithUserId(parameters);
            }
            logger.info("put methodName:" + methodName + ",url:" + routerUrl);
            restTemplate.put(routerUrl,null,map);
        }catch (HttpClientErrorException e){
            logger.warn("HttpClientErrorException:" + e.getStatusCode());
            return getResponseFromException(e);
        }catch (Exception e){
            return MyResponse.badRequest();
        }
        return MyResponse.ok(new MyServiceResponse("修改成功"));
    }





}
