package com.gdbjr.idealit.enums;

public enum RoleEnum {

    ADMIN("admin@123"),
    SYSTEM("system@123"),
    POLUPACAO("populacao@123");

    private String password;

    RoleEnum(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
