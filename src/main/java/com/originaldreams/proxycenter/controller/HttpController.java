package com.originaldreams.proxycenter.controller;

import com.originaldreams.common.encryption.MyBase64Utils;
import com.originaldreams.common.response.MyResponse;
import com.originaldreams.common.router.MyRouter;
import com.originaldreams.proxycenter.cache.CacheUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

    private Integer my_id = null;


    @RequestMapping(value = "logonWithUserName" , method = RequestMethod.POST)
    public ResponseEntity logonWithUserName(String userName,String password){
        logger.info("logonWithUserName  userName:" + userName);
        if(userName == null || password == null)
            return MyResponse.badRequest("参数异常");
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("password",password);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                MyRouter.UserManager_Logon + "?userName={userName}&password={password}",null,String.class,map);
        logger.info("logonWithUserName response:" + responseEntity.getBody());

        setCacheForLogon(responseEntity);

        return  responseEntity;
    }

    /**
     * 设置登录时的缓存
     * 包含请求session和用户权限缓存
     * @param response
     */
    private void setCacheForLogon(ResponseEntity<String> response){
        String result = response.getBody();
        JSONObject json = new JSONObject(result);
        int userId = json.getInt("data");
        logger.info("logonWithUserName userId:" + userId);

        //将userId放入Session
        request.getSession().setAttribute("my_id",userId);

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

    @RequestMapping(value = "logonWithPhone" , method = RequestMethod.POST)
    public ResponseEntity logonWithPhone(String phone,String password){
        logger.info("logonWithPhone  phone:" + phone);
        if(phone == null || password == null)
            return MyResponse.badRequest("参数异常");
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password",password);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                MyRouter.UserManager_Logon + "?phone={phone}&password={password}",null,String.class,map);
        setCacheForLogon(responseEntity);
        return  responseEntity;
    }

    @RequestMapping(value = "logonWithEmail" , method = RequestMethod.POST)
    public ResponseEntity logonWithEmail(String email,String password){
        logger.info("logonWithEmail  email:" + email);
        if(email == null || password == null)
            return MyResponse.badRequest("参数异常");
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password",password);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                MyRouter.UserManager_Logon + "?email={email}&password={password}",null,String.class,map);
        setCacheForLogon(responseEntity);
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

        String routerUrl = MyRouter.routerMap.get(methodName).getRouterUrl();
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
