package com.zimmer.carritov4;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CarritoActivity extends AppCompatActivity {

    private ListView listViewCarrito;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);

        listViewCarrito = findViewById(R.id.listViewCarrito);

        // Obtener la lista de productos desde el singleton
        ArrayList<Producto> productos = CarritoSingleton.getInstance().getProductos();

        // Convertir la lista de objetos Producto a una lista de Strings
        ArrayList<String> productosStr = new ArrayList<>();
        for (Producto producto : productos) {
            // Agregar información del producto como String
            productosStr.add("Nombre: " + producto.getNombre() + "\nPrecio: " + producto.getPrecio());
        }

        // Inicializar el adaptador con la lista de productos como Strings
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productosStr);
        listViewCarrito.setAdapter(adapter);
    }
}