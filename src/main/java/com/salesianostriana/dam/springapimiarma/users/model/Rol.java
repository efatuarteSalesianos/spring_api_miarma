package com.salesianostriana.dam.springapimiarma.users.model;

public enum Rol {
    USER("USER");

    private String value;

    private Rol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
