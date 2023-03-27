package com.sp.model;

import java.util.List;

public class UserDTO {
    private String username;
    private String name;
    private String password;
    private Float money;
    private List<String> roleList;

    public Float getMoney() {
        return money;
    }
    
    public void setMoney(Float money) {
        this.money = money;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserDTO [username=")
                .append(username)
                .append(", role=")
                .append(getRoleList())
                .append(", name=")
                .append(name)
                .append(", money=")
                .append(money).append("]");
        
        return builder.toString() ;
    }

}
