package com.originaldreams.proxycenter.controller;

import com.originaldreams.common.MyBase64Utils;
import com.originaldreams.common.MyClientRouter;
import com.originaldreams.common.MyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HttpController {
    private Logger logger = LoggerFactory.getLogger(HttpController.class);
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private HttpServletRequest request;

    private Integer my_id = null;

    public ResponseEntity logonWithUserName(String userName,String password){
        logger.info("logonWithUserName  userName:" + userName);
        if(userName == null || password == null)
            return MyResponse.badRequest("参数异常");
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("password",password);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("",map,String.class);
        //TODO 取出返回里的userId 保存到my_id
        return  responseEntity;
    }
    public ResponseEntity logonWithPhone(String phone,String password){
        logger.info("logonWithPhone  phone:" + phone);
        if(phone == null || password == null)
            return MyResponse.badRequest("参数异常");
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password",password);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("",map,String.class);
        //TODO 取出返回里的userId 保存到my_id
        return  responseEntity;
    }

    public ResponseEntity logonWithEmail(String email,String password){
        logger.info("logonWithEmail  email:" + email);
        if(email == null || password == null)
            return MyResponse.badRequest("参数异常");
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password",password);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("",map,String.class);
        //TODO 取出返回里的userId 保存到my_id
        return  responseEntity;
    }

    @RequestMapping(value = "/get" , method = RequestMethod.GET)
    public ResponseEntity get(String methodName,String parameters){
        ResponseEntity<String> responseEntity;
        String routerUrl = getRouterUrlByMethodName(methodName);
        if(routerUrl == null){
            return MyResponse.badRequest("参数异常");
        }
        try{
            if(parameters == null){
                responseEntity = restTemplate.getForEntity(routerUrl,String.class);
            }else{
                //url后拼接的请求参数格式
                String urlParameters = getUrlParametersWithUserId(parameters);
                //请求参数
                Map<String,Object> map = parseMapWithUserId(parameters);
                logger.info("get  methodName:" + methodName + ",url:" + routerUrl + urlParameters);
                responseEntity = restTemplate.getForEntity(routerUrl + urlParameters ,String.class,map);
            }
        }catch (Exception e){
            return MyResponse.badRequest("请求异常");
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
             return MyResponse.badRequest("参数异常");
        }
        try{
            logger.info("post  methodName:" + methodName + ",url:" + routerUrl + getUrlParametersWithUserId(parameters));
            Map<String,Object> map = parseMapWithUserId(parameters);
            responseEntity = restTemplate.postForEntity(routerUrl,map,String.class);
        }catch (Exception e){
            return MyResponse.badRequest("请求异常");
        }
        return responseEntity;
    }

    /**
     * 校验MethodName的合法性
     * @param methodName 合法返回true,不合法返回false
     * @return
     */
    private String  getRouterUrlByMethodName(String methodName){
        if(methodName == null)
            return null;

        String routerUrl = MyClientRouter.routerMap.get(methodName).getRouterUrl();
        if(routerUrl == null || routerUrl.equals("")){
            return null;
        }
        return routerUrl;
    }

    private Map<String,Object> parseMap(String parameters) throws Exception{
        if(parameters == null)
            return null;
        Map<String ,Object> map = new HashMap<>();
        for(String kValue : parameters.split(";")){
            String[] array = kValue.split(";");
            String key = array[0];
            String value = MyBase64Utils.decode(array[1]);
            map.put(key,value);
        }
        return  map;
    }
    private Map<String,Object> parseMapWithUserId(String parameters) throws Exception {
        Map<String ,Object> map = parseMap(parameters);
        map.put("my_id",my_id);
        return map;
    }
    private String getUrlParameters(String parameters) throws Exception{
        if(parameters == null)
            return null;
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        for(String kValue : parameters.split(";")){
            String[] array = kValue.split(";");
            String key = array[0];
           builder.append(key).append("={").append(key).append("}&");
        }
        return builder.toString();
    }
    private String getUrlParametersWithUserId(String parameters) throws  Exception{
        return getUrlParameters(parameters) + "my_id={my_id}";
    }
}
