package com.zimmer.carritov4;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HistorialVentasActivity extends AppCompatActivity {

    private ListView listViewHistorial;
    private ArrayList<String> ventasList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_ventas);

        // Inicializar el ListView
        listViewHistorial = findViewById(R.id.listViewHistorial);
        ventasList = new ArrayList<>();

        // Cargar el historial de ventas desde Firebase
        cargarHistorialVentas();

        // Crear el adaptador para mostrar las ventas
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ventasList);
        listViewHistorial.setAdapter(adapter);
    }

    private void cargarHistorialVentas() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ventas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombreProducto = document.getString("nombreProducto");
                            String precioProducto = document.getString("precioProducto");

                            if (nombreProducto != null && precioProducto != null) {
                                ventasList.add("Producto: " + nombreProducto + "\nPrecio: " + precioProducto);
                            }
                        }

                        // Actualizar el adaptador para reflejar los datos obtenidos
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(HistorialVentasActivity.this, "Error al cargar las ventas", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
