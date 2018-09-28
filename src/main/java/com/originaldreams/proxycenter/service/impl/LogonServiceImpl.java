package com.originaldreams.proxycenter.service.impl;

import com.originaldreams.common.response.MyResponse;
import com.originaldreams.common.router.MyRouter;
import com.originaldreams.common.util.ValidUserName;
import com.originaldreams.proxycenter.cache.CacheUtils;
import com.originaldreams.proxycenter.constants.HttpConstant;
import com.originaldreams.proxycenter.dto.LogonDTO;
import com.originaldreams.proxycenter.service.LogonService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogonServiceImpl implements LogonService {

    private static final Logger logger = LoggerFactory.getLogger(LogonServiceImpl.class);

    @Override
    public ResponseEntity<?> logon(LogonDTO logonDTO) {

        String userName = logonDTO.getUserName();
        String password = logonDTO.getPassword();

        logger.info("logon  userName: {} ", userName);

        RestTemplate restTemplate = new RestTemplate();

        // TODO 替代map
        Map<String, String> map = new HashMap<>();
        map.put("password",password);
        ResponseEntity<String> responseEntity;
        //手机号
        if(ValidUserName.isValidPhoneNumber(userName)){
            map.put("phone",userName);
            responseEntity = restTemplate.postForEntity(
                    MyRouter.USER_MANAGER_LOGON + "?email={phone}&password={password}",null,String.class,map);
        }
        //邮箱

        else if(ValidUserName.isValidEmailAddress(userName)){
            map.put("email", userName);
            responseEntity = restTemplate.postForEntity(
                    MyRouter.USER_MANAGER_LOGON + "?phone={email}&password={password}",null,String.class,map);
        }
        //用户名
        else if(ValidUserName.isValidUserName(userName)){
            map.put("userName", userName);
            responseEntity = restTemplate.postForEntity(
                    MyRouter.USER_MANAGER_LOGON + "?userName={userName}&password={password}",null,String.class,map);
        } else {
            return MyResponse.badRequest();
        }

        //登录成功，缓存
        // TODO 异步存储
        setCacheForLogon(responseEntity);


        return responseEntity;
    }

    /**
     * 设置登录时的缓存
     * 包含请求session和用户权限缓存
     * @param response
     */
    private void setCacheForLogon(ResponseEntity<String> response){
        String result = response.getBody();

        try{

            JSONObject json = new JSONObject(result);
            int success = json.getInt("success");
            //登录不成功，不记录session
            if(success != 0 ){
                return;
            }
            Object data = json.get("data");
            if ("null".equals(data.toString())) {
                return;
            }
            int userId = json.getInt("data");
            logger.info("logonWithUserName userId:" + userId);
            //将userId放入Session
//            request.getSession().setAttribute("userId",userId);
//            HttpSession session = request.getSession();
//            session.setAttribute("userId", userId);
            RestTemplate restTemplate = new RestTemplate();
            //查询用户的权限Id
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                    MyRouter.USER_MANAGER_PERMISSION_GET_ROUTER_IDS_BY_USER_ID + "?" + HttpConstant.USER_ID +"=" + userId,String.class);

            logger.info("USER_MANAGER_PERMISSION_GET_ROUTER_IDS_BY_USER_ID response:" + responseEntity.getBody());
            //将查询到的Id列表转化为List，放入缓存
            json = new JSONObject(responseEntity.getBody());
            json.getJSONArray("data");
            List<Object> list = json.getJSONArray("data").toList();
            List<Integer> routerIds = new ArrayList<>();
            for(Object object:list){
                routerIds.add((int)object);
            }
            //routerIds放入缓存
            CacheUtils.userRouterMap.put(userId,routerIds);
            //用户权限放入缓存
            responseEntity = restTemplate.getForEntity(
                    MyRouter.USER_MANAGER_PERMISSION_GET_ROLE_BY_USER_ID + "?" + HttpConstant.USER_ID +"=" + userId,String.class);

            json = new JSONObject(responseEntity.getBody());
            String roleName = json.getJSONObject("data").getString("name");
            //角色名放入缓存
            CacheUtils.userRoleMap.put(userId,roleName);
            logger.info("logonWithUserName roleName :" + roleName + ", routerIds:" + routerIds);
        }catch (JSONException e){
            e.printStackTrace();
            logger.error("setCacheForLogon {}", e.getMessage());

        }

    }
}
