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
import com.example.monify.Interface.AppDatabase;
import com.example.monify.Entity.Tarjeta;
import android.text.Editable;
import android.text.TextWatcher;

public class InsertarTarjetaActivity extends AppCompatActivity {

    private Spinner spinnerNombre;
    private EditText etNumero, etSaldo, etFechaExpiracion;
    private Button btnAgregarTarjeta, btnVolver;
    private AppDatabase db;  // Instancia de la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_tarjeta);

        // Inicializar la base de datos
        db = AppDatabase.getInstance(this);

        // Referencias a los EditText, Button y Spinner
        spinnerNombre = findViewById(R.id.spinnerNombreTarjeta);
        etNumero = findViewById(R.id.etNumeroTarjeta);
        etSaldo = findViewById(R.id.etSaldoTarjeta);
        etFechaExpiracion = findViewById(R.id.etFechaExpiracion);
        btnAgregarTarjeta = findViewById(R.id.btnAgregarTarjeta);

        // Configuración del Spinner con las opciones de tarjeta
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tarjetas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNombre.setAdapter(adapter);

        // Agregar TextWatcher al campo etNumero
        etNumero.addTextChangedListener(new TextWatcher() {
            private String currentText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementar nada aquí
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita implementar nada aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                // Evitar bucles infinitos
                if (!input.equals(currentText)) {
                    // Eliminar cualquier carácter no numérico
                    String formattedInput = input.replaceAll("[^0-9]", "");

                    // Actualizar el texto formateado (por ejemplo, añadiendo espacios cada 4 caracteres)
                    StringBuilder formatted = new StringBuilder();
                    for (int i = 0; i < formattedInput.length(); i++) {
                        if (i > 0 && i % 4 == 0) {
                            formatted.append(" ");
                        }
                        formatted.append(formattedInput.charAt(i));
                    }

                    currentText = formatted.toString();
                    etNumero.removeTextChangedListener(this); // Eliminar temporalmente el listener
                    etNumero.setText(currentText); // Establecer el texto actualizado
                    etNumero.setSelection(currentText.length()); // Mover el cursor al final
                    etNumero.addTextChangedListener(this); // Volver a agregar el listener
                }
            }
        });
        // Acción al hacer clic en el botón "Agregar Tarjeta"
        btnAgregarTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos ingresados
                String nombre = spinnerNombre.getSelectedItem().toString();  // Obtener el nombre seleccionado
                String numero = etNumero.getText().toString();
                String saldoText = etSaldo.getText().toString();
                String fechaExpiracion = etFechaExpiracion.getText().toString();

                // Validar los campos
                if (nombre.isEmpty() || numero.isEmpty() || fechaExpiracion.isEmpty() || saldoText.isEmpty()) {
                    Toast.makeText(InsertarTarjetaActivity.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar si el saldo es un número válido
                double saldo = 0.0;
                try {
                    saldo = Double.parseDouble(saldoText);
                } catch (NumberFormatException e) {
                    Toast.makeText(InsertarTarjetaActivity.this, "El saldo debe ser un número válido.", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                int userId = preferences.getInt("userId", -1); // Asegúrate de que se haya guardado previamente el userId

                // Si el userId no es válido, mostrar un error
                if (userId == -1) {
                    Toast.makeText(InsertarTarjetaActivity.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crear el objeto tarjeta y asignar el userId
                Tarjeta tarjeta = new Tarjeta();
                tarjeta.setNombre(nombre);
                tarjeta.setNumero(numero);
                tarjeta.setSaldo(saldo);
                tarjeta.setFechaExpiracion(fechaExpiracion);
                tarjeta.setUserId(userId); // Asignar el userId

                // Insertar la tarjeta en la base de datos
                insertTarjeta(tarjeta);
            }
        });

        Button btnVolverAtras = findViewById(R.id.btnVolverAtras);
        btnVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual y vuelve a la anterior
            }
        });
    }

    // Método para insertar la tarjeta en la base de datos
    private void insertTarjeta(Tarjeta tarjeta) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Obtener el DAO de la tarjeta
                tarjetaDao tarjetaDao = db.tarjetaDao();

                // Insertar la tarjeta en la base de datos
                tarjetaDao.insertarTarjeta(tarjeta);

                // Mostrar un mensaje de éxito
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InsertarTarjetaActivity.this, "Tarjeta agregada exitosamente", Toast.LENGTH_SHORT).show();
                        clearFields();  // Limpiar los campos después de insertar
                    }
                });
            }
        }).start();
    }

    // Limpiar los campos después de insertar una tarjeta
    private void clearFields() {
        spinnerNombre.setSelection(0);  // Resetea la selección del spinner
        etNumero.setText("");
        etSaldo.setText("");
        etFechaExpiracion.setText("");
    }
}
