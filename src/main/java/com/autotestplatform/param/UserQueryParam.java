package com.autotestplatform.param;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserQueryParam {
    @NotNull(message = "email不能为空")
    private String email;
    @NotNull(message = "密码不能为空")
    private String pwd;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
