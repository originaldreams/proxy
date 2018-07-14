package com.originaldreams.proxycenter.common;

/**
 * 维护组件间相互调用的接口
 * 新增的供组件间调用的接口在这里注册
 * 接口命名规则：组件名_Controller上的RequestMapping.value_接口上的RequestMapping.value  首字母大写
 */
public class MyServiceRouter {

    public final static String Prefix = "http://";

    /**
     * LogCenter提供的测试http get请求的接口
     * 接口参数：
     * id: Integer，null
     * name: String，null
     */
    public final static String LogCenter_Http_Get = Prefix + MyServiceName.LogCenter + "/http/get";

    /**
     * LogCenter提供的测试http post请求的接口
     * 接口参数：
     * id: Integer，notNull
     * name: String，notNull
     */
    public final static String LogCenter_Http_Post = Prefix + MyServiceName.LogCenter + "/http/post";

}
