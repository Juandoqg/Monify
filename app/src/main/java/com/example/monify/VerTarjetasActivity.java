package com.example.monify;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monify.Entity.Tarjeta;
import com.example.monify.Interface.AppDatabase;
import com.example.monify.adapters.TarjetasAdapter;

import java.util.List;

public class VerTarjetasActivity extends AppCompatActivity {

    private RecyclerView recyclerTarjetas;
    private TarjetasAdapter tarjetasAdapter;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_tarjetas);

        recyclerTarjetas = findViewById(R.id.recyclerTarjetas);
        recyclerTarjetas.setLayoutManager(new LinearLayoutManager(this));

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish()); // Finaliza la actividad para volver

        SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);

        // Obtener las tarjetas desde la base de datos
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<Tarjeta> listaTarjetas = db.tarjetaDao().obtenerTarjetasPorUsuario(userId);

            runOnUiThread(() -> {
                // Configurar el adaptador en el hilo principal
                tarjetasAdapter = new TarjetasAdapter(listaTarjetas);
                recyclerTarjetas.setAdapter(tarjetasAdapter);
            });
        }).start();
    }
}
