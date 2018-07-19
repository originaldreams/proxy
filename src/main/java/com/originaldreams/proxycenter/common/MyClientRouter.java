package com.originaldreams.proxycenter.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 维护组件为客户端提供的接口，这些接口会自动读取到my_core库的router表中
 * 当一个接口同时可以提供给Server和Client使用时，只在MyServiceName里注册一下，同时加入到MyClientRouter里的routerMap里即可
 * 当一个接口只提供给Client使用时，直接在MyClientName里注册，同时加入到MyClientRouter里的routerMap里
 */
public class MyClientRouter {
    private static Logger logger = LoggerFactory.getLogger(MyClientRouter.class);
    public final static String Prefix = "http://";


    public final static String UserManagerCenter_Permission_GetAllRoles = Prefix + MyServiceName.UserManagerCenter + "/permission/getAllRoles";
    public final static String UserManagerCenter_Permission_GetRolesByUserId = Prefix + MyServiceName.UserManagerCenter + "/permission/getRolesByUserId";
    public final static String UserManagerCenter_Permission_GetRolesByRouterId = Prefix + MyServiceName.UserManagerCenter + "/permission/getRolesByRouterId";
    public final static String UserManagerCenter_Permission_GetUsersByRoleId = Prefix + MyServiceName.UserManagerCenter + "/permission/getUsersByRoleId";
    public final static String UserManagerCenter_Permission_GetAllRouters = Prefix + MyServiceName.UserManagerCenter + "/permission/getAllRouters";
    public final static String UserManagerCenter_Permission_GetRoutersByRoleId = Prefix + MyServiceName.UserManagerCenter + "/permission/getRoutersByRoleId";

    public final static String UserManagerCenter_PermissionManager_addRole = Prefix + MyServiceName.UserManagerCenter + "/permissionManager/addRole";
    public final static String UserManagerCenter_PermissionManager_addRoleForUser = Prefix + MyServiceName.UserManagerCenter + "/permissionManager/addRoleForUser";
    public final static String UserManagerCenter_PermissionManager_addRouterForRole = Prefix + MyServiceName.UserManagerCenter + "/permissionManager/addRouterForRole";


    /**
     *  可供客户端调用的接口路由
     */
    public  static Map<String ,MyRouterObject> routerMap = new HashMap<>();

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

    /**
     * 初始化routerMap
     * 这个在添加新的router时要仔细检查，服务启动时会根据routerMap来初始化路由表
     */
    static{

        routerMap.put("LogCenter_Http_Get", new MyRouterObject(1000, MyServiceRouter.LogCenter_Http_Get,byte1th,byte1th));
        routerMap.put("LogCenter_Http_Post", new MyRouterObject(1001, MyServiceRouter.LogCenter_Http_Post,byte1th,byte1th));

        routerMap.put("UserManagerCenter_Permission_GetAllRoles", new MyRouterObject(2000, UserManagerCenter_Permission_GetAllRoles,byte2th,byte1th));
        routerMap.put("UserManagerCenter_Permission_GetRolesByUserId", new MyRouterObject(2001, UserManagerCenter_Permission_GetRolesByUserId,byte2th,byte1th));
        routerMap.put("UserManagerCenter_Permission_GetRolesByRouterId", new MyRouterObject(2002, UserManagerCenter_Permission_GetRolesByRouterId,byte2th,byte1th));
        routerMap.put("UserManagerCenter_Permission_GetUsersByRoleId", new MyRouterObject(2003, UserManagerCenter_Permission_GetUsersByRoleId,byte2th,byte1th));
        routerMap.put("UserManagerCenter_Permission_GetAllRouters", new MyRouterObject(2004, UserManagerCenter_Permission_GetAllRouters,byte2th,byte1th));
        routerMap.put("UserManagerCenter_Permission_GetRoutersByRoleId", new MyRouterObject(2005, UserManagerCenter_Permission_GetRoutersByRoleId,byte2th,byte1th));

        routerMap.put("UserManagerCenter_PermissionManager_addRole", new MyRouterObject(2100, UserManagerCenter_PermissionManager_addRole,byte2th,byte2th));
        routerMap.put("UserManagerCenter_PermissionManager_addRoleForUser", new MyRouterObject(2101, UserManagerCenter_PermissionManager_addRoleForUser,byte2th,byte2th));
        routerMap.put("UserManagerCenter_PermissionManager_addRouterForRole", new MyRouterObject(2102, UserManagerCenter_PermissionManager_addRouterForRole,byte2th,byte2th));
    }

}
