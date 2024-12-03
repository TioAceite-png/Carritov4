package com.zimmer.carritov4;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class CarritoActivity extends AppCompatActivity {

    private Button btnComprar, btnVolver;
    private ListView listViewCarrito;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);

        // Inicializar los elementos de la vista
        listViewCarrito = findViewById(R.id.listViewCarrito);
        btnComprar = findViewById(R.id.btnComprar);
        btnVolver = findViewById(R.id.btnVolverT);

        // Obtener la lista de productos desde el singleton
        ArrayList<Producto> productos = CarritoSingleton.getInstance().getProductos();

        // Adaptador modificado para mostrar nombre y precio
        ArrayList<String> productosStr = new ArrayList<>();
        for (Producto producto : productos) {
            // Mostrar solo el nombre y precio del producto
            productosStr.add(producto.getNombre() + " - $" + producto.getPrecio());
        }

        // Inicializar el adaptador con la lista de productos como Strings
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productosStr);
        listViewCarrito.setAdapter(adapter);

        // Botón "Volver"
        btnVolver.setOnClickListener(v -> finish());

        // Botón "Comprar"
        btnComprar.setOnClickListener(v -> {
            // Guardar los productos comprados en el historial de ventas
            ArrayList<Producto> productosComprados = new ArrayList<>(CarritoSingleton.getInstance().getProductos());

            // Guardar el historial en memoria (o en base de datos si lo prefieres)
            for (Producto producto : productosComprados) {
                agregarVentaAFirebase(producto.getNombre(), producto.getPrecio());
            }

            // Limpiar el carrito
            CarritoSingleton.getInstance().getProductos().clear();
            adapter.notifyDataSetChanged();
        });
    }

    // Método para agregar la venta a Firebase
    private void agregarVentaAFirebase(String nombreProducto, String precioProducto) {
        // Crear un objeto de venta
        HashMap<String, String> venta = new HashMap<>();
        venta.put("nombreProducto", nombreProducto);
        venta.put("precioProducto", precioProducto);

        // Obtener la referencia a la colección "ventas"
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ventas")
                .add(venta)
                .addOnSuccessListener(documentReference -> {
                    // Venta agregada exitosamente
                    Log.d("Venta", "Venta agregada con ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    // Manejo de error
                    Log.w("Venta", "Error al agregar la venta", e);
                });
    }
}
