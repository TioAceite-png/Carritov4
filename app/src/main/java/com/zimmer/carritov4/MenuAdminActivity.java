package com.zimmer.carritov4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MenuAdminActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextPrecio;
    private Button btnAgregarProducto, btnActualizarProducto, btnEliminarProducto, btnCerrarSesion, btnHistorial;
    private ListView listViewProductosAdmin, listViewHistorial;

    private FirebaseFirestore db; // Para trabajar con Firestore
    private String selectedProductId = ""; // Para almacenar el ID del producto seleccionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        // Inicializar campos y botones
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextPrecio = findViewById(R.id.editTextPrecio);
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto);
        btnActualizarProducto = findViewById(R.id.btnActualizarProducto);
        btnEliminarProducto = findViewById(R.id.btnEliminarProducto);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnHistorial = findViewById(R.id.btnHistorial);
        listViewProductosAdmin = findViewById(R.id.listViewProductosAdmin);
        listViewHistorial = findViewById(R.id.listViewHistorial);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Cargar productos desde Firestore y mostrarlos en el ListView
        loadProducts();

        // Acción para agregar producto (sin necesidad de ID)
        btnAgregarProducto.setOnClickListener(view -> {
            String nombre = editTextNombre.getText().toString();
            String precio = editTextPrecio.getText().toString();

            if (nombre.isEmpty() || precio.isEmpty()) {
                Toast.makeText(MenuAdminActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Producto producto = new Producto(nombre, precio);

            db.collection("productos")
                    .add(producto)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(MenuAdminActivity.this, "Producto agregado correctamente", Toast.LENGTH_SHORT).show();
                        loadProducts(); // Recargar la lista de productos después de agregar uno nuevo
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MenuAdminActivity.this, "Error al agregar producto", Toast.LENGTH_SHORT).show();
                    });
        });

        // Acción para actualizar producto
        btnActualizarProducto.setOnClickListener(view -> {
            if (selectedProductId.isEmpty()) {
                Toast.makeText(MenuAdminActivity.this, "Por favor, seleccione un producto", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = editTextNombre.getText().toString();
            String precio = editTextPrecio.getText().toString();

            if (nombre.isEmpty() || precio.isEmpty()) {
                Toast.makeText(MenuAdminActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Producto producto = new Producto(nombre, precio);

            db.collection("productos").document(selectedProductId)
                    .set(producto)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(MenuAdminActivity.this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                        loadProducts(); // Recargar la lista de productos después de actualizar
                    })
                    .addOnFailureListener(e -> Toast.makeText(MenuAdminActivity.this, "Error al actualizar producto", Toast.LENGTH_SHORT).show());
        });

        // Acción para eliminar producto
        btnEliminarProducto.setOnClickListener(view -> {
            if (selectedProductId.isEmpty()) {
                Toast.makeText(MenuAdminActivity.this, "Por favor, seleccione un producto", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("productos").document(selectedProductId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(MenuAdminActivity.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                        loadProducts(); // Recargar la lista de productos después de eliminar
                    })
                    .addOnFailureListener(e -> Toast.makeText(MenuAdminActivity.this, "Error al eliminar producto", Toast.LENGTH_SHORT).show());
        });

        // Acción para cerrar sesión
        btnCerrarSesion.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MenuAdminActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MenuAdminActivity.this, LoginsActivity.class));
            finish();
        });

        // Acción para mostrar el historial de ventas
        btnHistorial.setOnClickListener(v -> {
            if (listViewHistorial.getVisibility() == View.GONE) {
                listViewHistorial.setVisibility(View.VISIBLE); // Mostrar historial
                obtenerVentasDeFirebase();  // Cargar las ventas desde Firebase
            } else {
                listViewHistorial.setVisibility(View.GONE); // Ocultar historial
            }
        });
    }

    // Método para cargar productos desde Firestore y mostrarlos en el ListView
    private void loadProducts() {
        db.collection("productos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> productDetails = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String name = document.getString("nombre");
                        String price = document.getString("precio");
                        if (name != null && price != null) {
                            productDetails.add(name + " - $" + price);
                        }
                    }
                    // Crear el ArrayAdapter y asignarlo al ListView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MenuAdminActivity.this,
                            android.R.layout.simple_list_item_1, productDetails);
                    listViewProductosAdmin.setAdapter(adapter);

                    // Configurar el listener para seleccionar un producto
                    listViewProductosAdmin.setOnItemClickListener((adapterView, view, position, id) -> {
                        selectedProductId = queryDocumentSnapshots.getDocuments().get(position).getId();
                        String selectedItem = productDetails.get(position);
                        Toast.makeText(MenuAdminActivity.this, "Producto seleccionado: " + selectedItem, Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(MenuAdminActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show());
    }

    // Método para obtener las ventas de Firebase y mostrarlas en el ListView
    private void obtenerVentasDeFirebase() {
        db.collection("ventas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> ventas = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombreProducto = document.getString("nombreProducto");
                            String precioProducto = document.getString("precioProducto");
                            ventas.add("Producto: " + nombreProducto + "\nPrecio: " + precioProducto);
                        }

                        // Mostrar las ventas en el ListView
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ventas);
                        listViewHistorial.setAdapter(adapter);
                    } else {
                        Toast.makeText(MenuAdminActivity.this, "Error al obtener las ventas.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
