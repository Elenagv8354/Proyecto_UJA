package com.example.walletssi;

/**
 * La clase Credencial representa un único dato de credencial personal.
 * Almacena el valor del dato y un estado booleano que indica si este dato
 * debe mostrarse u ocultarse en la interfaz de usuario.
 */

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
