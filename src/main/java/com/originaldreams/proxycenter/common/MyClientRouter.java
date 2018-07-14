package com.originaldreams.proxycenter.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 维护组件为客户端提供的接口
 * 当一个接口同时可以提供给Server和Client使用时，只在MyServiceName里注册一下，同时加入到MyClientRouter里的routerMap里即可
 * 当一个接口只提供给Client使用时，直接在MyClientName里注册，同时加入到MyClientRouter里的routerMap里
 */
public class MyClientRouter {

    /**
     *  可供客户端调用的接口路由
     */
    public  static Map<String ,MyClientRouterObject> routerMap = new HashMap<>();

    private final static long byte1th       = 1 << 0;//第1位
    private final static long byte2th       = 1 << 1;//第2位
    private final static long byte3th       = 1 << 2;//第3位
    private final static long byte4th       = 1 << 3;//第4位
    private final static long byte5th       = 1 << 4;//第5位
    private final static long byte6th       = 1 << 5;//第6位
    private final static long byte7th       = 1 << 6;//第7位
    private final static long byte8th       = 1 << 7;//第8位
    private final static long byte9th       = 1 << 8;//第9位
    private final static long byte10th      = 1 << 9;//第10位

    static{
        routerMap.put("LogCenter_Http_Get",new MyClientRouterObject("LogCenter_Http_Get",MyServiceRouter.LogCenter_Http_Get,byte1th,byte1th));
        routerMap.put("LogCenter_Http_Post",new MyClientRouterObject("LogCenter_Http_Get",MyServiceRouter.LogCenter_Http_Post,byte1th,byte2th));
    }

}
