package com.zimmer.carritov4;

import java.util.ArrayList;

public class VentasSingleton {
    private static VentasSingleton instance;
    private ArrayList<ArrayList<Producto>> historialVentas;

    private VentasSingleton() {
        historialVentas = new ArrayList<>();
    }

    public static VentasSingleton getInstance() {
        if (instance == null) {
            instance = new VentasSingleton();
        }
        return instance;
    }

    public void addVenta(ArrayList<Producto> productos) {
        historialVentas.add(productos);
    }

    public ArrayList<ArrayList<Producto>> getHistorialVentas() {
        return historialVentas;
    }
}
