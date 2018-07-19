package com.originaldreams.proxycenter.common;

public class MyRouterObject {
    private Integer id;
    private String serviceName;
    private String controllerName;
    private String methodName;
    private String routerUrl;
    private Long firstMask;
    private Long secondMask;

    /**
     * @param id
     * @param routerUrl 格式：http://UserManagerCenter/permission/getAllRoles"
     * @param firstMask
     * @param secondMask
     */
    public MyRouterObject(Integer id, String routerUrl, Long firstMask, Long secondMask){
        this.id = id;
        this.routerUrl = routerUrl;
        this.firstMask = firstMask;
        this.secondMask = secondMask;
        /*
        这里可能产生数组越界抛异常，当抛异常时，说明routerUrl格式不正确，需要修改
         */
        try {
            String[] array = routerUrl.split("/");
            this.serviceName = array[2];
            this.controllerName = array[3];
            this.methodName = array[4];
        }catch (ArrayIndexOutOfBoundsException e){
            throw new ArrayIndexOutOfBoundsException("routerUrl格式不正确");
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
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

    public Long getFirstMask() {
        return firstMask;
    }

    public void setFirstMask(Long firstMask) {
        this.firstMask = firstMask;
    }

    public Long getSecondMask() {
        return secondMask;
    }

    public void setSecondMask(Long secondMask) {
        this.secondMask = secondMask;
    }
}
