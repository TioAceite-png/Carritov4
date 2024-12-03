package com.zimmer.carritov4;

public class Producto {
    private String nombre;
    private String precio;

    // Constructor
    public Producto(String nombre, String precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
    @Override
    public String toString() {
        return "Producto: " + nombre + ", Precio: " + precio;
    }
}


