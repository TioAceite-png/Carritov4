package com.zimmer.carritov4;

import java.util.ArrayList;

public class CarritoSingleton {
    private static CarritoSingleton instance;
    private ArrayList<Producto> productos;

    private CarritoSingleton() {
        productos = new ArrayList<>();
    }

    // Obtener la instancia del Singleton
    public static CarritoSingleton getInstance() {
        if (instance == null) {
            instance = new CarritoSingleton();
        }
        return instance;
    }

    // Obtener los productos del carrito
    public ArrayList<Producto> getProductos() {
        return productos;
    }

    // Agregar un producto al carrito
    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    // Limpiar el carrito
    public void clear() {
        productos.clear();
    }

    // Verificar si el carrito está vacío
    public boolean isEmpty() {
        return productos.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Producto producto : productos) {
            sb.append(producto.toString()).append("\n");
        }
        return sb.toString();
    }
}
