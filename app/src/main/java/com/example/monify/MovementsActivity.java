package com.example.monify;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.monify.DAO.transaccionDao;
import com.example.monify.Entity.Tarjeta;
import com.example.monify.Entity.Transaccion;
import com.example.monify.Entity.TransactionWithCard;
import com.example.monify.Interface.AppDatabase;
import com.example.monify.adapters.TarjetasAdapter;
import com.example.monify.adapters.TransaccionAdapter;
import java.util.List;

public class MovementsActivity extends AppCompatActivity {

    private transaccionDao transaccionDao;
    private RecyclerView recyclerView;
    private TransaccionAdapter transaccionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movements);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtén el userId pasado desde la actividad principal
        SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1); // Asegúrate de que se haya guardado previamente el userId

        if (userId == -1) {
            Toast.makeText(this, "Usuario no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        new Thread(() -> {
            // Obtener la instancia de la base de datos
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            transaccionDao = db.transaccionDao();

            // Cambia el método para obtener las transacciones con tarjeta
            List<TransactionWithCard> transaccionesConTarjetas = transaccionDao.obtenerTransaccionesConTarjeta(userId);

            // Actualiza la UI en el hilo principal
            runOnUiThread(() -> {
                // Configura el adaptador en el RecyclerView
                TransaccionAdapter transaccionAdapter = new TransaccionAdapter(transaccionesConTarjetas);
                recyclerView.setAdapter(transaccionAdapter);
            });
        }).start();

    }
}
