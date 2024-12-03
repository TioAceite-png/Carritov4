package com.zimmer.carritov4;

import java.util.ArrayList;

public class VentasSingleton {
    private static VentasSingleton instance;
    private ArrayList<Venta> historialVentas;

    private VentasSingleton() {
        historialVentas = new ArrayList<>();
    }

    public static VentasSingleton getInstance() {
        if (instance == null) {
            instance = new VentasSingleton();
        }
        return instance;
    }

    public void addVenta(Venta venta) {
        historialVentas.add(venta);
    }

    public ArrayList<Venta> getHistorialVentas() {
        return historialVentas;
    }
}
