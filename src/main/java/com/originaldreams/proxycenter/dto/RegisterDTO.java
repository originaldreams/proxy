package com.originaldreams.proxycenter.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RegisterDTO {

    @NotEmpty
    @Size(max = 100)
    private String userName;

    @NotEmpty
    @Size(min = 6, max = 100)
    private String password;

    @NotEmpty
    private String verificationCode;

    public RegisterDTO() {
    }

    @Override
    public String toString() {
        return "RegisterDTO{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
