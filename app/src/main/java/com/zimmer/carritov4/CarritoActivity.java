package com.zimmer.carritov4;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
            // Obtener los productos del carrito
            ArrayList<Producto> productosComprados = new ArrayList<>(CarritoSingleton.getInstance().getProductos());

            // Verificar si el carrito está vacío
            if (productosComprados.isEmpty()) {
                Toast.makeText(CarritoActivity.this, "El carrito está vacío, no se puede realizar la compra.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar los productos comprados en el historial de ventas
            for (Producto producto : productosComprados) {
                // Agregar cada venta a Firebase
                agregarVentaAFirebase(producto.getNombre(), producto.getPrecio());
            }

            // Limpiar el carrito
            CarritoSingleton.getInstance().clear();
            adapter.notifyDataSetChanged();

            // Mostrar mensaje de éxito
            Toast.makeText(CarritoActivity.this, "Compra realizada con éxito.", Toast.LENGTH_SHORT).show();
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
