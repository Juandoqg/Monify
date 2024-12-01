package com.example.monify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.squareup.picasso.Picasso;
import android.widget.ImageView;

public class TarjetasActivity extends AppCompatActivity {
    private Button btnVolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tarjetas);

        // Configuración para el ajuste de la barra de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botón para navegar a InsertarTarjetaActivity
        Button btnAgregarTarjeta = findViewById(R.id.btnAgregarTarjeta);
        btnAgregarTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navegarAInsertarTarjeta(); // Llamar a la función para navegar a la otra actividad
            }
        });

        // Botón para navegar a VerTarjetasActivity
        Button btnVerTarjetas = findViewById(R.id.btnVerTarjetas);
        btnVerTarjetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navegarAVerTarjetas(); // Llamar a la función para navegar a VerTarjetasActivity
            }
        });
        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        ImageView imagenView = findViewById(R.id.imagen);
        Picasso.get().load(R.drawable.imagen_tarjeta).into(imagenView);
    }

    // Función que maneja el Intent para navegar a InsertarTarjetaActivity
    private void navegarAInsertarTarjeta() {
        Intent intent = new Intent(TarjetasActivity.this, InsertarTarjetaActivity.class);
        startActivity(intent);
    }

    // Función que maneja el Intent para navegar a VerTarjetasActivity
    private void navegarAVerTarjetas() {
        Intent intent = new Intent(TarjetasActivity.this, VerTarjetasActivity.class);
        // Opcional: si necesitas pasar el ID del usuario
        // intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }
}

