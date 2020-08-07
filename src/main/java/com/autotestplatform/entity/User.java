package com.autotestplatform.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User {
    private String id;
    @NotBlank(message = "姓名不能为空")
    private String username;
    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 30, message = "密码长度应当在 6 ~ 30 个字符之间")
    private String password;
    @NotNull(message = "email不能为空")
    private String email;
    @NotNull(message = "电话不能为空")
    private String phone;
    @NotNull(message = "部门不能为空")
    private String department;
    @NotNull(message = "角色不能为空")
    private String role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User() {
    }

    public User(String id, @NotNull(message = "姓名不能为空") String username, @NotNull(message = "密码不能为空") @Size(min = 6, max = 30, message = "密码长度应当在 6 ~ 30 个字符之间") String password, @NotNull(message = "email不能为空") String email, @NotNull(message = "电话不能为空") String phone, @NotNull(message = "部门不能为空") String department, @NotNull(message = "角色不能为空") String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", department='" + department + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
