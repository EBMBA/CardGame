package com.sp.model;

public class UserDTO {
    private String username;
    private String name;
    private String password;
    private Float money;
    private Integer role;

    public Float getMoney() {
        return money;
    }
    
    public void setMoney(Float money) {
        this.money = money;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
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
                .append(role)
                .append(", name=")
                .append(name)
                .append(", money=")
                .append(money).append("]");
        
        return builder.toString() ;
    }

}
