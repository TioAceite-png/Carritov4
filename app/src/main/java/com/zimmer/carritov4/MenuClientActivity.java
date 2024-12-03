package com.zimmer.carritov4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MenuClientActivity extends AppCompatActivity {

    private ListView listViewProductos;
    private ArrayAdapter<String> adapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_client);

        mAuth = FirebaseAuth.getInstance();

         listViewProductos = findViewById(R.id.listViewProductosCliente);
        db = FirebaseFirestore.getInstance();

        // Inicializar el ArrayAdapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listViewProductos.setAdapter(adapter);

        // Botón para ver el carrito
        ImageButton btnCarrito = findViewById(R.id.btnCarrito);
        btnCarrito.setOnClickListener(view -> {
            // Redirigir a la actividad del carrito
            Intent intent = new Intent(MenuClientActivity.this, CarritoActivity.class);
            startActivity(intent);
        });

        // Botón para cerrar sesión
        Button btnCerrarSesion = findViewById(R.id.btnCerrarS);
        btnCerrarSesion.setOnClickListener(view -> {

            mAuth.signOut();

            // Redirigir a la pantalla de login
            Intent intent = new Intent(MenuClientActivity.this, LoginsActivity.class);
            startActivity(intent);
            finish(); // Finalizar la actividad actual
        });

        // Cargar los productos desde Firestore
        loadProducts();
    }

    private void loadProducts() {
        db.collection("productos") // Obtener la colección de productos
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> productos = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Obtener los datos del documento
                            String nombre = document.getString("nombre");
                            String precio = document.getString("precio");

                            // Crear un objeto Producto
                            Producto producto = new Producto(nombre, precio);

                            // Crear una cadena con la información del producto para mostrar
                            String productoInfo = "Nombre: " + nombre + "\nPrecio: " + precio;
                            productos.add(productoInfo);
                        }
                        // Actualizar el ListView
                        adapter.clear();
                        adapter.addAll(productos);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MenuClientActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
                    }
                });

        // Acción al seleccionar un producto
        listViewProductos.setOnItemClickListener((parent, view, position, id) -> {
            // Aquí deberías obtener el objeto Producto relacionado con el texto seleccionado
            // Al seleccionar un producto, se agrega al carrito
            Producto productoSeleccionado = new Producto("Nombre Producto", "Precio Producto"); // Esto es solo un ejemplo
            CarritoSingleton.getInstance().agregarProducto(productoSeleccionado);

            Toast.makeText(MenuClientActivity.this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
        });
    }
}
