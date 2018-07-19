package com.originaldreams.proxycenter.common;

/**
 * 维护组件名
 * 新增的组件在这里注册
 */
public class MyServiceName {
    /**
     * 服务注册中心
     * 维护所有已启动的组件
     */
    public final static String ServiceRegistryCenter = "ServiceRegistryCenter";

    /**
     * 用户管理中心
     * 维护用户信息，负责用户登录、注册
     */
    public final static String UserManagerCenter = "UserManagerCenter";

    /**
     * 日志中心
     * 负责所有组件的日志记录、查询等功能
     */
    public final static String LogCenter = "LogCenter";

    /**
     * 外部服务中心
     * 负责提供邮件发送、短信发送等对外服务
     */
    public final static String PublicServiceCenter = "PublicServiceCenter";



}
