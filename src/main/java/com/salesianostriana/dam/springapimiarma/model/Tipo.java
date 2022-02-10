package com.salesianostriana.dam.springapimiarma.model;

public enum Tipo {
    COMPRA("COMPRA"),
    ALQUILER("ALQUILER"),
    COMPARTIR("COMPARTIR"),
    NUEVA_OBRA("NUEVA_OBRA");

    private String valor;

    private Tipo(String valor){
        this.valor =valor;
    }
    public String getValor(){
        return valor;
    }
    public void setValor(String valor){
        this.valor =valor;
    }
}
