package com.example.walletssi;

public class Credencial {
    private String nombre;
    private boolean oculta;

    public Credencial(String nombre, boolean oculta) {
        this.nombre = nombre;
        this.oculta = oculta;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isOculta() {
        return oculta;
    }

    public void setOculta(boolean oculta) {
        this.oculta = oculta;
    }
}
