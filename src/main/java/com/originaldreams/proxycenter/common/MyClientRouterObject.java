package com.originaldreams.proxycenter.common;

public class MyClientRouterObject {
    /**
     * 方法名，唯一标识一个接口
     */
    private String methodName;
    /**
     * 路由地址
     */
    private String routerUrl;
    /**
     * 组件掩码
     * long可存储32位，因此设计组件数上限为30（理想状态在20以内）
     */
    private long serviceMask;
    /**
     * Controller掩码（组件下的Controller）
     * 设计一个组件下最多有30个Controller（理想状态在10以内）
     */
    private long controllerMask;

    public MyClientRouterObject(String methodName, String routerUrl, long serviceMask, long controllerMask) {
        this.methodName = methodName;
        this.routerUrl = routerUrl;
        this.serviceMask = serviceMask;
        this.controllerMask = controllerMask;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRouterUrl() {
        return routerUrl;
    }

    public void setRouterUrl(String routerUrl) {
        this.routerUrl = routerUrl;
    }

    public long getServiceMask() {
        return serviceMask;
    }

    public void setServiceMask(long serviceMask) {
        this.serviceMask = serviceMask;
    }

    public long getControllerMask() {
        return controllerMask;
    }

    public void setControllerMask(long controllerMask) {
        this.controllerMask = controllerMask;
    }
}
