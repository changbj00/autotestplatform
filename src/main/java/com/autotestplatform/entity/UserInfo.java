package com.autotestplatform.entity;

public class UserInfo {
    private String uid;
    private String role;
    private String permission;
    private String department;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid=" + uid +
                ", role='" + role + '\'' +
                ", permission='" + permission + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
