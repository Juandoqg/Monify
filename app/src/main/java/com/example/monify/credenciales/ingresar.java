package com.example.monify.credenciales;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monify.Interface.AppDatabase;
import com.example.monify.DAO.userDao;
import com.example.monify.Entity.User;
import com.example.monify.R;
import com.example.monify.inicioApp;

public class ingresar extends AppCompatActivity {

    private AppDatabase db;
    private userDao userDao;

    private EditText get_Username, get_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);

        // Inicializar la base de datos y el DAO
        db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        // Inicializar vistas
        get_Username = findViewById(R.id.get_Username);// Obtener EditText desde TextInputLayout
        get_Password = findViewById(R.id.get_Password); // Obtener EditText desde TextInputLayout
    }

    public void volver(View view) {
        onBackPressed(); // Vuelve a la actividad anterior
    }

    // Método para manejar el inicio de sesión
    public void singIn(View v) {
        String usuario = get_Username.getText().toString().trim();
        String contrasena = get_Password.getText().toString().trim();

        // Validación de campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor complete ambos campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Operación en hilo de fondo para verificar las credenciales
        new Thread(() -> {
            User user = userDao.login(usuario, contrasena);

            runOnUiThread(() -> {
                if (user != null) {
                    // Usuario encontrado, redirigir a la pantalla principal
                    Toast.makeText(ingresar.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("userId", user.getId());  // Guardamos el ID del usuario
                    editor.apply();
                    abrirInicio();  // Redirige a la actividad inicioApp
                } else {
                    // Usuario no encontrado o credenciales incorrectas
                    Toast.makeText(ingresar.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }

            });
        }).start();
    }

    public void abrirInicio() {
        Intent intent = new Intent(this, inicioApp.class);  // Redirige a inicioApp
        startActivity(intent);
        finish();
    }
}
