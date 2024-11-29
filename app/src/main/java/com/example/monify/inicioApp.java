package com.example.monify;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class inicioApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_app);

        // Manejo de la insets (barras de sistema)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnAgregarIngreso = findViewById(R.id.btnAgregarIngreso);
        btnAgregarIngreso.setOnClickListener(view -> {
            Intent intent = new Intent(inicioApp.this, AgregarIngreso.class);
            startActivity(intent);
        });


        // Configura el BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Establece el listener para los ítems del menú
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            // Verifica si el ítem seleccionado es el correcto
            if (id == R.id.navigation_dashboard) {
                // Lanza la actividad TarjetasActivity
                Intent intent = new Intent(inicioApp.this, TarjetasActivity.class);
                startActivity(intent);
                return true;  // Se ha manejado el evento
            }

            return false;  // Si no es el ítem que nos interesa
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Infla el menú de navegación en la parte inferior
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Agrega un log o Toast para asegurarte de que el ítem fue seleccionado
        if (id == R.id.navigation_dashboard) {
            Toast.makeText(this, "Lanzando TarjetasActivity", Toast.LENGTH_SHORT).show();

            // Lanza la actividad TarjetasActivity
            Intent intent = new Intent(inicioApp.this, TarjetasActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
