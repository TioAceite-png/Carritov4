package com.zimmer.carritov4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsuario, editTextContraseña;
    private Button buttonIniciarSesion, buttonVolver;
    private ProgressDialog progressDialog;

    // Datos locales (simulando una base de datos)
    private static final String CORREO_ADMIN = "tiopepito";
    private static final String CONTRASENA_ADMIN = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar las vistas
        editTextUsuario = findViewById(R.id.editTextEmailA);
        editTextContraseña = findViewById(R.id.editTextPasswordA);
        buttonIniciarSesion = findViewById(R.id.btnIniciarSesionA);
        buttonVolver = findViewById(R.id.btnVolverA);

        // Configuración del ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);

        // Configuración del botón de inicio de sesión
        buttonIniciarSesion.setOnClickListener(v -> loginAdmin());

        // Configuración del botón "Volver"
        buttonVolver.setOnClickListener(v -> finish());
    }

    private void loginAdmin() {
        String email = editTextUsuario.getText().toString().trim();
        String password = editTextContraseña.getText().toString().trim();

        // Verificar si el correo y la contraseña coinciden con los valores locales
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        // Comprobamos si el correo y la contraseña son correctos
        if (email.equals(CORREO_ADMIN) && password.equals(CONTRASENA_ADMIN)) {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Bienvenido, Administrador", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, MenuAdminActivity.class));
            finish();
        } else {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Acceso denegado. Credenciales incorrectas.", Toast.LENGTH_SHORT).show();
        }
    }
}
