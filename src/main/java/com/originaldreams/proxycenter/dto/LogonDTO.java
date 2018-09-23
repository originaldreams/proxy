package com.originaldreams.proxycenter.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class LogonDTO {

    @NotEmpty
    @Size(max = 100)
    private String userName;

    @NotEmpty
    @Size(min = 6, max = 100)
    private String password;

    public LogonDTO() {
    }

    @Override
    public String toString() {
        return "LogonDTO{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
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
}
