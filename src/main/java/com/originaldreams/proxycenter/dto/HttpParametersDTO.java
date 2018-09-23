package com.originaldreams.proxycenter.dto;

import javax.validation.constraints.NotEmpty;

public class HttpParametersDTO {

    @NotEmpty
    private String methodName;

    private String parameters;


    public HttpParametersDTO() {
    }

    public HttpParametersDTO(String methodName, String parameters) {
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
