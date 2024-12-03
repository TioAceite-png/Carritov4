package com.zimmer.carritov4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button registerTextButton;
    private TextView loginRedirect;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.editTextEmailR);
        passwordEditText = findViewById(R.id.editTextPasswordR);
        registerTextButton = findViewById(R.id.registerTextButton);
        loginRedirect = findViewById(R.id.loginRedirect);

        registerTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                registerUser();
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginClientActivity.class));
                finish();
            }
        });
    }
    private  void registerUser(){
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                FirebaseUser user = mAuth.getCurrentUser();
                Toast.makeText(RegisterActivity.this, "Registro exitoso: "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                //Redirigir al menu cliente si el registro fue exitoso
                startActivity(new Intent(RegisterActivity.this, MenuClientActivity.class));
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Error al registrar: " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}