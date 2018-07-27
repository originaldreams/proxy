package com.originaldreams.proxycenter.controller;

import com.originaldreams.common.encryption.MyBase64Utils;
import com.originaldreams.common.response.MyResponse;
import com.originaldreams.common.response.MyServiceResponse;
import com.originaldreams.common.router.MyRouter;
import com.originaldreams.proxycenter.cache.CacheUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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


    @RequestMapping(value = "/logonWithUserName" , method = RequestMethod.POST)
    public ResponseEntity logonWithUserName(String userName,String password){
        try{
            logger.info("logonWithUserName  userName:" + userName);
            if(userName == null || password == null)
                return MyResponse.badRequest();
            Map<String, String> map = new HashMap<>();
            map.put("userName", userName);
            map.put("password",password);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    MyRouter.UserManager_Logon + "?userName={userName}&password={password}",null,String.class,map);
            logger.info("logonWithUserName response:" + responseEntity.getBody());

            setCacheForLogon(responseEntity);

            return  responseEntity;
        }catch (HttpClientErrorException e){
            logger.warn("HttpClientErrorException:" + e.getStatusCode());
            return getResponseFromException(e);
        }
    }

    @RequestMapping(value = "/logonWithPhone" , method = RequestMethod.POST)
    public ResponseEntity logonWithPhone(String phone,String password){
        try {
            logger.info("logonWithPhone  phone:" + phone);
            if(phone == null || password == null)
                return MyResponse.badRequest();
            Map<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("password",password);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    MyRouter.UserManager_Logon + "?phone={phone}&password={password}",null,String.class,map);
            setCacheForLogon(responseEntity);
            return  responseEntity;
        }catch (HttpClientErrorException e){
            logger.warn("HttpClientErrorException:" + e.getStatusCode());
            return getResponseFromException(e);
        }

    }

    @RequestMapping(value = "/logonWithEmail" , method = RequestMethod.POST)
    public ResponseEntity logonWithEmail(String email,String password){
        try {
            logger.info("logonWithEmail  email:" + email);
            if(email == null || password == null)
                return MyResponse.badRequest();
            Map<String, String> map = new HashMap<>();
            map.put("email", email);
            map.put("password",password);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    MyRouter.UserManager_Logon + "?email={email}&password={password}",null,String.class,map);
            setCacheForLogon(responseEntity);
            return  responseEntity;
        }catch (HttpClientErrorException e){
            logger.warn("HttpClientErrorException:" + e.getStatusCode());
            return getResponseFromException(e);
        }

    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ResponseEntity register(String userName,String phone,String email,String password){
        try {
            logger.info("register  :" );
            Map<String, String> map = new HashMap<>();
            map.put("userName",userName);
            map.put("phone",phone);
            map.put("email", email);
            map.put("password",password);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    MyRouter.UserManager_Register + "?userName={userName}&phone={phone}&email={email}&password={password}",null,String.class,map);
            return  responseEntity;
        }catch (HttpClientErrorException e){
            logger.warn("HttpClientErrorException:" + e.getStatusCode());
            return getResponseFromException(e);
        }
    }

    /**
     * 针对一般用户所有get请求的转发
     * 特点：当查询条件为用户id时，不用上传用户id
     * 1.鉴权
     * 2.转发
     * 3.针对错误返回码（401、403等）转处理为不同的应答
     * @param methodName    方法名
     * @param parameters    参数
     * @return
     */
    @RequestMapping(value = "/user" , method = RequestMethod.GET)
    public ResponseEntity userGet(String methodName,String parameters){
        if(methodName == null)
            return MyResponse.badRequest();
        if(!authenticate(methodName))
            return MyResponse.forbidden();

        ResponseEntity<String> responseEntity;
        String routerUrl = MyRouter.getRouterUrlByMethodName(methodName);
        if(routerUrl == null){
            return MyResponse.badRequest();
        }
        try{
            if(parameters == null){
                responseEntity = restTemplate.getForEntity(routerUrl + "?id=" + getUserId(),String.class);
            }else{
                //url后拼接的请求参数格式
                String urlParameters = getUrlParameters(parameters);
                routerUrl += urlParameters;
                //请求参数
                Map<String,Object> map = parseMap(parameters);
                logger.info("get  methodName:" + methodName + ",url:" + routerUrl);
                responseEntity = restTemplate.getForEntity(routerUrl,String.class,map);
            }
        }catch (HttpClientErrorException e){
            logger.warn("HttpClientErrorException:" + e.getStatusCode());
            return getResponseFromException(e);
        }catch (Exception e){
            return MyResponse.badRequest();
        }
        return responseEntity;
    }

    @RequestMapping(value = "/manager" , method = RequestMethod.GET)
    public ResponseEntity managerGet(String methodName,String parameters){
        if(methodName == null)
            return MyResponse.badRequest();
        if(!authenticate(methodName))
            return MyResponse.forbidden();

        ResponseEntity<String> responseEntity;
        String routerUrl = MyRouter.getRouterUrlByMethodName(methodName);
        if(routerUrl == null){
            return MyResponse.badRequest();
        }
        try{
            if(parameters == null){
                responseEntity = restTemplate.getForEntity(routerUrl,String.class);
            }else{
                //url后拼接的请求参数格式
                String urlParameters = getUrlParameters(parameters);
                routerUrl += urlParameters;
                //请求参数
                Map<String,Object> map = parseMap(parameters);
                logger.info("get  methodName:" + methodName + ",url:" + routerUrl);
                responseEntity = restTemplate.getForEntity(routerUrl,String.class,map);
            }
        }catch (HttpClientErrorException e){
            logger.warn("HttpClientErrorException:" + e.getStatusCode());
            return getResponseFromException(e);
        }catch (Exception e){
            return MyResponse.badRequest();
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
        if(methodName == null)
            return MyResponse.badRequest();
        if(!authenticate(methodName))
            return MyResponse.forbidden();

        ResponseEntity<String> responseEntity;
        String routerUrl = MyRouter.getRouterUrlByMethodName(methodName);
        if(routerUrl == null || parameters == null){
             return MyResponse.badRequest();
        }
        try{
            routerUrl += getUrlParametersWithUserId(parameters);
            logger.info("post  methodName:" + methodName + ",url:" + routerUrl);
            Map<String,Object> map = parseMapWithUserId(parameters);
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
     * 鉴权
     * @param methodName 客户端调用的方法名
     * @return
     */
    private boolean authenticate(String methodName){
        Integer userId = getUserId();
        if(userId == null)
            return false;
        List<Integer> routerIdList = CacheUtils.userRouterMap.get(getUserId());
        try{
            Integer routerId = MyRouter.getRouterByMethodName(methodName).getId();
            return  routerIdList.contains(routerId);
        }catch (Exception e){
            logger.error("鉴权异常，methodName:" + methodName + ",userId:" + userId);
           return false;
        }
    }



    /**
     * 根据参数生成Map
     * @param parameters    加密过的参数
     * @return
     * @throws Exception
     */
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

    /**
     * 根据参数生成Map （含userId）
     * @param parameters    加密过的参数
     * @return
     * @throws Exception
     */
    private Map<String,Object> parseMapWithUserId(String parameters) throws Exception {
        Map<String ,Object> map = parseMap(parameters);
        map.put("my_id",getUserId());
        return map;
    }

    /**
     * 获取Url参数
     * @param parameters 加密过的参数
     * @return
     * @throws Exception
     */
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

    /**
     * 获取Url参数（含UserId）
     * @param parameters    加密过的参数
     * @return
     * @throws Exception
     */
    private String getUrlParametersWithUserId(String parameters) throws  Exception{
        return getUrlParameters(parameters) + "my_id={my_id}";
    }

    /**
     * 根据组件返回的错误码重组应答报文
     * @param exception
     * @return
     */
    private ResponseEntity getResponseFromException(HttpClientErrorException exception){
        ResponseEntity response;
        switch (exception.getStatusCode()){
            case FORBIDDEN:  response = MyResponse.forbidden(); break;
            case BAD_REQUEST: response = MyResponse.badRequest();break;
            case UNAUTHORIZED: response = MyResponse.unauthorized();break;
            default:{
                MyServiceResponse myServiceResponse = new MyServiceResponse(MyServiceResponse.success_code_failed,"未知错误");
                response = ResponseEntity.status(exception.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(myServiceResponse);
            }
        }
        return  response;
    }

    /**
     * 设置登录时的缓存
     * 包含请求session和用户权限缓存
     * @param response
     */
    private void setCacheForLogon(ResponseEntity<String> response){
        String result = response.getBody();
        JSONObject json = new JSONObject(result);
        int success = json.getInt("success");
        //登录不成功，不记录session
        if(success != 0 ){
            return;
        }
        int userId = json.getInt("data");
        logger.info("logonWithUserName userId:" + userId);
        //将userId放入Session
        request.getSession().setAttribute("userId",userId);

        //查询用户的权限Id
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                MyRouter.UserManager_Permission_GetRouterIdsByUserId + "?userId=" + userId,String.class);

        logger.info("UserManager_Permission_GetRouterIdsByUserId response:" + responseEntity.getBody());
        //将查询到的Id列表转化为List，放入缓存
        json = new JSONObject(responseEntity.getBody());
        json.getJSONArray("data");
        List<Object> list = json.getJSONArray("data").toList();
        List<Integer> routerIds = new ArrayList<>();
        for(Object object:list){
            routerIds.add((int)object);
        }
        logger.info("logonWithUserName routerIds:" + routerIds);
        CacheUtils.userRouterMap.put(userId,routerIds);
    }

    private Integer getUserId(){
        Object object = request.getSession().getAttribute("userId");
        if(object == null){
            return null;
        }else{
            try {
                return (int)object;
            }catch (Exception e){
                logger.error("session获取失败:" + object);
                return null;
            }
        }
    }

}
