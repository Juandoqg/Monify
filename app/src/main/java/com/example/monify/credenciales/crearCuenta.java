package com.example.monify.credenciales;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monify.Interface.AppDatabase;
import com.example.monify.DAO.userDao;
import com.example.monify.Entity.User;
import com.example.monify.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class crearCuenta extends AppCompatActivity {

    private AppDatabase db;
    private userDao userDao;

    // Declaración de las variables para los campos de entrada
    private TextInputEditText usernameInput, emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        // Inicialización de los TextInputEditText
        usernameInput = findViewById(R.id.usuario);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.contrasena);

        // Inicializa la base de datos
        db = AppDatabase.getInstance(this);
        userDao = db.userDao();
    }

    // Método para registrar un usuario
    public void registrar_usuario(View v) {
        // Obtener valores de los campos
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validación de campos
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showSnackbar(v, "Por favor completa todos los campos");
            return;
        }

        // Operación en un hilo de fondo
        new Thread(() -> {
            try {
                // Verificar si el email ya existe
                int count = userDao.checkEmailExists(email);

                if (count > 0) {
                    // Email ya registrado
                    runOnUiThread(() -> showSnackbar(v, "El email ya está registrado"));
                } else {
                    // Crear usuario
                    User user = new User();
                    user.userName = username;
                    user.email = email;
                    user.password = password;

                    // Insertar usuario en la base de datos
                    userDao.insertUser(user);

                    // Mostrar confirmación en la UI principal
                    runOnUiThread(() -> {
                        showSnackbar(v, "Usuario registrado con éxito");
                    });
                }
            } catch (Exception e) {
                Log.e("Room", "Error al registrar usuario", e);
                runOnUiThread(() -> showSnackbar(v, "Error al registrar usuario"));
            }
        }).start();
        volver(v);
    }

    // Método para volver a la pantalla anterior
    public void volver(View v) {
        onBackPressed();
    }


    // Método para mostrar un Snackbar personalizado
    private void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(getResources().getColor(R.color.md_theme_secondary)); // Fondo del Snackbar
        snackbar.setTextColor(getResources().getColor(R.color.md_theme_onSecondary)); // Color del texto
        snackbar.setDuration(5000);
        View snackbarView = snackbar.getView();
        snackbarView.setTranslationY(-150); // Ajustar posición si es necesario (opcional)
        snackbar.show();
    }
}
