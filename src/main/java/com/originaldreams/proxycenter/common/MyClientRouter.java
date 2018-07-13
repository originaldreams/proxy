package com.originaldreams.proxycenter.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 维护组件为客户端提供的接口
 * 当一个接口同时可以提供给Server和Client使用时，只在MyServiceName里注册一下，同时加入到MyClientRouter里的routerMap里即可
 * 当一个接口只提供给Client使用时，直接在MyClientName里注册，同时加入到MyClientRouter里的routerMap里
 */
public class MyClientRouter {


    public  static Map<String ,String> routerMap = new HashMap<>();
    static{
        routerMap.put("LogCenter_Http_Get",MyServiceRouter.LogCenter_Http_Get);
        routerMap.put("LogCenter_Http_Post",MyServiceRouter.LogCenter_Http_Post);

    }

}
