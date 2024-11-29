package com.example.monify;


import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.monify.DAO.tarjetaDao;
import com.example.monify.DAO.transaccionDao;
import com.example.monify.Entity.Transaccion;
import com.example.monify.Entity.Tarjeta;
import com.example.monify.Interface.AppDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AgregarIngreso extends AppCompatActivity {

    private Spinner spinnerTarjetas;
    private EditText edtMonto;
    private Button btnAgregarIngreso;
    private List<Tarjeta> tarjetas;
    private int tarjetaSeleccionadaId = -1;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ingreso);

        spinnerTarjetas = findViewById(R.id.spinnerTarjetas);
        edtMonto = findViewById(R.id.edtMonto);
        btnAgregarIngreso = findViewById(R.id.btnAgregarIngreso);
        db = AppDatabase.getInstance(this);
        SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);

        // Obtener tarjetas del usuario desde la base de datos en segundo plano
        obtenerTarjetasUsuario(userId);

        // Establecer listener para el Spinner
        spinnerTarjetas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                tarjetaSeleccionadaId = tarjetas.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                tarjetaSeleccionadaId = -1;
            }
        });

        // Configurar el botón para agregar el ingreso
        btnAgregarIngreso.setOnClickListener(v -> {
            String montoStr = edtMonto.getText().toString();
            if (montoStr.isEmpty() || tarjetaSeleccionadaId == -1) {
                Toast.makeText(AgregarIngreso.this, "Por favor selecciona una tarjeta y un monto.", Toast.LENGTH_SHORT).show();
                return;
            }

            double monto = Double.parseDouble(montoStr);

            // Crear una nueva transacción
            Transaccion transaccion = new Transaccion();
            transaccion.setTipo("Ingreso");
            transaccion.setMonto(monto);
            transaccion.setTarjetaId(tarjetaSeleccionadaId);
            transaccion.setUserId(userId);
            insertTransaccion(transaccion);
        });
    }

    private void obtenerTarjetasUsuario(final int userId) {
        // Ejecutar en un hilo en segundo plano
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Crear la lista de tarjetas
                tarjetaDao tarjetaDao = db.tarjetaDao();
                tarjetas = tarjetaDao.obtenerTarjetasPorUsuario(userId);

                // Asegurarte de ejecutar el código que modifica la UI en el hilo principal
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Crear un adaptador para poblar el Spinner usando los números de tarjeta
                        ArrayAdapter<Tarjeta> adapter = new ArrayAdapter<>(AgregarIngreso.this, android.R.layout.simple_spinner_item, tarjetas);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerTarjetas.setAdapter(adapter);

                        // Si las tarjetas no están vacías, establecer la primera tarjeta en el spinner
                        if (!tarjetas.isEmpty()) {
                            Tarjeta primeraTarjeta = tarjetas.get(0);
                            int primeraTarjetaIndex = tarjetas.indexOf(primeraTarjeta);
                            spinnerTarjetas.setSelection(primeraTarjetaIndex);
                            tarjetaSeleccionadaId = primeraTarjeta.getId(); // Establecer la ID de la tarjeta seleccionada
                        }
                    }
                });
            }
        });
    }

    private void insertTransaccion(Transaccion transaccion) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Obtener el DAO de la tarjeta
                transaccionDao transaccionDao = db.transaccionDao();

                // Insertar la transacción en la base de datos
                transaccionDao.insertarTransaccion(transaccion);

                // Mostrar un mensaje de éxito
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AgregarIngreso.this, "Ingreso agregado exitosamente", Toast.LENGTH_SHORT).show();
                        clearFields();  // Limpiar los campos después de insertar
                    }
                });
            }
        }).start();
    }

    private void clearFields() {
        edtMonto.setText("");
    }
}
