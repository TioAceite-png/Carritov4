package com.zimmer.carritov4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginClientActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, backButton, registerButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_client);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);

        backButton = findViewById(R.id.btnVolverC);
        emailEditText = findViewById(R.id.editTextEmailC);
        passwordEditText = findViewById(R.id.editTextPasswordC);
        loginButton = findViewById(R.id.btnIniciarSesionC);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(view -> loginUser());

        backButton.setOnClickListener(view -> finish());

        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginClientActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginClientActivity.this, "Bienvenido " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginClientActivity.this, MenuClientActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginClientActivity.this, "Error al iniciar sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(LoginClientActivity.this, MenuClientActivity.class));
            finish();
        }
    }
}
