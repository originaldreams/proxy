package com.originaldreams.proxycenter.service.impl;

import com.originaldreams.common.encryption.MyBase64Utils;
import com.originaldreams.common.response.MyResponse;
import com.originaldreams.common.response.MyServiceResponse;
import com.originaldreams.common.router.MyRouter;
import com.originaldreams.common.router.MyRouterObject;
import com.originaldreams.proxycenter.cache.CacheUtils;
import com.originaldreams.proxycenter.constants.HttpConstant;
import com.originaldreams.proxycenter.dto.HttpParametersDTO;
import com.originaldreams.proxycenter.service.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

@Service
public class HttpServiceImpl implements HttpService {

    private static final Logger logger = LoggerFactory.getLogger(HttpServiceImpl.class);

    @Override
    public ResponseEntity<?> get(HttpParametersDTO httpParametersDTO) {

        String methodName = httpParametersDTO.getMethodName();
        String parameters = httpParametersDTO.getParameters();

        String routerUrl = authenticateAndReturnRouterUrl(MyRouter.REQUEST_METHOD_GET, methodName);
        if(routerUrl == null){
            return MyResponse.forbidden();
        }

        ResponseEntity<String> responseEntity;

        RestTemplate restTemplate = new RestTemplate();

        /**
         * TODO 这里出现一个问题，用户是否可以查看别人的信息？用户查看别人的信息时，需不需要隐藏一些敏感信息
         * 允许管理员在接口中传入userId参数（允许其操作其他User的数据）
         * 不允许普通用户传递（不允许其操作其他User的数据）
         */

        Map<String,Object> map;
        if(isManager()){
            //Manager的空参数请求，说明就是空参数
            if(parameters == null){
                responseEntity = restTemplate.getForEntity(routerUrl,String.class);
                return responseEntity;
            }else{
                //url后拼接的请求参数格式
                String urlParameters = getUrlParameters(parameters);
                routerUrl += urlParameters;
                //请求参数
                map = parseMap(parameters);
                logger.info("get  methodName:" + methodName + ",url:" + routerUrl);
            }
        }else{
            //User的空参数请求自动拼接userId
            if(parameters == null){
                // TODO userId
                responseEntity = restTemplate.getForEntity(routerUrl + "?" + HttpConstant.USER_ID+ "=" + "userId",String.class);
                return responseEntity;
            }else{
                //url后拼接的请求参数格式,原则上不允许上传userId，当请求参数中有userId时，会被改写为自己的userId
                String urlParameters = getUrlParametersWithUserId(parameters);
                routerUrl += urlParameters;
                //请求参数
                map = parseMapWithUserId(parameters);
                logger.info("get  methodName:" + methodName + ",url:" + routerUrl);
            }

        }

        try {
            responseEntity = restTemplate.getForEntity(routerUrl, String.class, map);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.warn("HttpClientErrorException: {}", e.getStatusCode());
            return getResponseFromException(e);
        }


        return responseEntity;
    }

    @Override
    public ResponseEntity<?> post(HttpParametersDTO httpParametersDTO) {

        String methodName = httpParametersDTO.getMethodName();
        String parameters = httpParametersDTO.getParameters();

        String routerUrl = authenticateAndReturnRouterUrl(MyRouter.REQUEST_METHOD_POST,methodName);
        if(routerUrl == null){
            return MyResponse.forbidden();
        }

        RestTemplate restTemplate = new RestTemplate();

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

    @Override
    public ResponseEntity<?> put(HttpParametersDTO httpParametersDTO) {

        String methodName = httpParametersDTO.getMethodName();
        String parameters = httpParametersDTO.getParameters();

        String routerUrl = authenticateAndReturnRouterUrl(MyRouter.REQUEST_METHOD_PUT,methodName);
        if(routerUrl == null){
            return MyResponse.forbidden();
        }

        RestTemplate restTemplate = new RestTemplate();
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

    @Override
    public ResponseEntity<?> delete(HttpParametersDTO httpParametersDTO) {

        String methodName = httpParametersDTO.getMethodName();
        String parameters = httpParametersDTO.getParameters();

        String routerUrl = authenticateAndReturnRouterUrl(MyRouter.REQUEST_METHOD_DELETE,methodName);
        if(routerUrl == null){
            return MyResponse.forbidden();
        }

        RestTemplate restTemplate = new RestTemplate();

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
     * 获取Url参数（含UserId）
     * @param parameters    加密过的参数
     * @return
     * @throws Exception
     */
    private String getUrlParametersWithUserId(String parameters) throws PatternSyntaxException{
        return getUrlParameters(parameters) + HttpConstant.USER_ID +"={" + HttpConstant.USER_ID + "}";
    }

    /**
     * 根据组件返回的错误码重组应答报文
     * @param exception
     * @return
     */
    private ResponseEntity getResponseFromException(HttpStatusCodeException exception){
        ResponseEntity response;
        switch (exception.getStatusCode()){
            case FORBIDDEN:  response = MyResponse.forbidden(); break;
            case BAD_REQUEST: response = MyResponse.badRequest();break;
            case UNAUTHORIZED: response = MyResponse.unauthorized();break;
            default:{
                MyServiceResponse myServiceResponse = new MyServiceResponse(MyServiceResponse.SUCCESS_CODE_FAILED,"未知错误");
                response = ResponseEntity.status(exception.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(myServiceResponse);
            }
        }
        return  response;
    }


//    private Integer getUserId(){
//        Object object = request.getSession().getAttribute("userId");
//        if(object == null){
//            return null;
//        }else{
//            try {
//                return (int)object;
//            }catch (Exception e){
//                logger.error("session获取失败:" + object);
//                return null;
//            }
//        }
//    }

    /**
     * TODO 角色准备初始化在common里面，每次UserManagerCenter启动时刷到DB中
     * @return
     */
    private boolean isManager(){
        Integer userId = getUserId();
        String roleName = CacheUtils.userRoleMap.get(userId);
        if(roleName == null || roleName.equals("User")){
            return false;
        }else if(roleName.equals("Manager")){
            return true;
        }
        return false;
    }

    /**
     * 鉴权
     * // TODO userId
     * @param methodName 客户端调用的方法名
     * @return
     */
    private String authenticateAndReturnRouterUrl(String method, String methodName){
//        Integer userId = getUserId();

//        if(userId == null){
//            return null;
//        }
        List<Integer> routerIdList = CacheUtils.userRouterMap.get("userId");

        MyRouterObject routerObject = MyRouter.getRouter(method,methodName);

        if(routerObject == null || routerIdList == null || routerIdList.size() < 1){
            return null;
        }
        Integer routerId = routerObject.getId();
        String routerUrl = routerIdList.contains(routerId)?routerObject.getRouterUrl():null;
        return routerUrl;

    }



    /**
     * 根据参数生成Map
     * @param parameters    加密过的参数
     * @return
     * @throws Exception
     */
    private Map<String,Object> parseMap(String parameters) throws PatternSyntaxException{
        if(parameters == null){
            return null;
        }
        Map<String ,Object> map = new HashMap<>();
        for(String kValue : parameters.split(HttpConstant.SPLIT_PARAMETERS)){
            String[] array = kValue.split(HttpConstant.SPLIT_KEY_VALUE);
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

    /**
     * 获取Url参数
     * @param parameters 加密过的参数
     * @return
     * @throws Exception
     */
    private String getUrlParameters(String parameters) throws PatternSyntaxException {
        if(parameters == null){
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        for(String kValue : parameters.split(HttpConstant.SPLIT_PARAMETERS)){
            String[] array = kValue.split(HttpConstant.SPLIT_KEY_VALUE);
            String key = array[0];
            builder.append(key).append("={").append(key).append("}&");
        }
        return builder.toString();
    }

    /**
     * 根据参数生成Map （含userId）
     * @param parameters    加密过的参数
     * @return
     * @throws Exception
     */
    private Map<String,Object> parseMapWithUserId(String parameters) throws PatternSyntaxException {
        Map<String ,Object> map = parseMap(parameters);
        map.put(USER_ID,getUserId());
        return map;
    }


}
