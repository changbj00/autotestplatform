package com.autotestplatform.param;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 登录模块
 */
@Data
public class UserQueryParam {
    @Email
    @NotBlank(message = "email不能为空")
    private String email;
    @Size(min = 6, max = 30, message = "密码长度应当在 6 ~ 30 个字符之间")
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
