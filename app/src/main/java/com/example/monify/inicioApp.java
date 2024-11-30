package com.example.monify;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import androidx.lifecycle.ViewModelProvider;

import com.example.monify.DAO.transaccionDao;
import com.example.monify.Entity.Transaccion;
import com.example.monify.Interface.AppDatabase;
import com.example.monify.viewmodel.TransaccionViewModelFactory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.monify.viewmodel.TransaccionViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class inicioApp extends AppCompatActivity {
    private AppDatabase db;  // Instancia de la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_app);
        db = AppDatabase.getInstance(this);
        // Manejo de la insets (barras de sistema)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TransaccionViewModelFactory factory = new TransaccionViewModelFactory(getApplication());
        TransaccionViewModel viewModel = new ViewModelProvider(this, factory).get(TransaccionViewModel.class);

        // Observa las transacciones y actualiza la UI
        viewModel.getTransacciones().observe(this, transacciones -> {
            if (transacciones != null) {
                graficarTransacciones(transacciones);
            }
        });



    Button btnAgregarIngreso = findViewById(R.id.btnAgregarIngreso);
        btnAgregarIngreso.setOnClickListener(view -> {
            Intent intent = new Intent(inicioApp.this, AgregarIngreso.class);
            startActivity(intent);
        });

        Button btnAgregarGasto = findViewById(R.id.btnAgregarGasto);
        btnAgregarGasto.setOnClickListener(view -> {
            Intent intent = new Intent(inicioApp.this, AgregarGasto.class);
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

    private void graficarTransacciones(List<Transaccion> transacciones) {
        // Obtener el DAO
        transaccionDao transaccionDao = db.transaccionDao();
        BarChart barChart = findViewById(R.id.barChart);
        PieChart pieChart = findViewById(R.id.pieChart);

        // Datos para el gráfico de barras
        final float[] totalIngresos = {0f};  // Usamos un array para hacerlo "final"
        final float[] totalGastos = {0f};    // Usamos un array para hacerlo "final"

        // Inicializar los contadores de las tarjetas
        final int[] VISA = {0};
        final int[] MASTERCARD = {0};

        // Calcular total de Ingresos y Gastos
        for (Transaccion transaccion : transacciones) {
            if (transaccion.getTipo().equalsIgnoreCase("Ingreso")) {
                totalIngresos[0] += transaccion.getMonto();
            } else if (transaccion.getTipo().equalsIgnoreCase("Gasto")) {
                totalGastos[0] += transaccion.getMonto();
            }
        }

        // Ejecutar AsyncTask para consultar nombres de las tarjetas
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                // Contar las transacciones de cada tarjeta
                for (Transaccion transaccion : transacciones) {
                    // Consulta el nombre de la tarjeta
                    String tarjetaNombre = transaccionDao.getNombreTarjeta(transaccion.getTarjetaId());

                    if (tarjetaNombre != null) {
                        // Actualiza los contadores según el nombre de la tarjeta
                        if (tarjetaNombre.equalsIgnoreCase("VISA")) {
                            VISA[0]++;
                        } else if (tarjetaNombre.equalsIgnoreCase("MASTERCARD")) {
                            MASTERCARD[0]++;
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Después de completar el AsyncTask, actualizamos los gráficos en el hilo principal
                graficarBarChart(barChart, totalIngresos[0], totalGastos[0]);
                graficarPieChart(pieChart, VISA[0], MASTERCARD[0]);
            }
        }.execute();
    }

    // Método para graficar el gráfico de barras
    private void graficarBarChart(BarChart barChart, float totalIngresos, float totalGastos) {
        // Crear las entradas para las barras
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, totalIngresos)); // Posición 0: Ingresos
        entries.add(new BarEntry(1f, totalGastos));  // Posición 1: Gastos

        // Crear el dataset para el gráfico de barras
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        // Configurar los datos del gráfico de barras
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // Ancho de las barras

        barChart.setData(barData);
        barChart.setFitBars(true); // Ajusta las barras al eje X

        // Habilitar la descripción y colocar el título arriba
        barChart.getDescription().setEnabled(true);
        barChart.getDescription().setText("Total de transacciones por Monto");
        barChart.getDescription().setTextSize(14f);
        barChart.getDescription().setPosition(650f, 45f); // Ajustar posición del título más abajo
        barChart.getDescription().setTextAlign(Paint.Align.CENTER); // Centrar el título

        // Configuración del eje X y demás opciones
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{"Ingresos", "Gastos"}));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getAxisRight().setEnabled(false); // Ocultar el eje derecho
        barChart.getAxisLeft().setAxisMinimum(0f); // Eje izquierdo empieza en 0
        barChart.animateY(1000); // Animación del gráfico
        barChart.setExtraOffsets(10f, 30f, 10f, 10f); // Márgenes adicionales para el gráfico
        barChart.invalidate(); // Refrescar el gráfico
    }

    private void graficarPieChart(PieChart pieChart, int visaCount, int masterCardCount) {
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(visaCount, "VISA"));
        pieEntries.add(new PieEntry(masterCardCount, "MASTERCARD"));

        // Crear el dataset para el gráfico de pie
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextSize(12f);

        // Crear los datos del gráfico de pie
        PieData pieData = new PieData(pieDataSet);

        // Establecer el ValueFormatter para mostrar valores enteros
        pieDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%d", (int) value);  // Muestra el valor como entero sin decimales
            }
        });

        // Configurar el gráfico de pie
        pieChart.setData(pieData);

        // Habilitar la descripción y colocar el título arriba
        pieChart.getDescription().setEnabled(true);
        pieChart.getDescription().setText("Cantidad de Movimientos por Tarjeta");
        pieChart.getDescription().setTextSize(14f);
        pieChart.getDescription().setPosition(600f, 80f); // Ajustar posición del título más abajo
        pieChart.getDescription().setTextAlign(Paint.Align.CENTER); // Centrar el título

        // Desactivar los valores en porcentaje y mostrar el número de transacciones
        pieChart.setUsePercentValues(false);  // Desactivar los valores en porcentaje
        pieChart.animateY(1000);
        pieChart.setExtraOffsets(10f, 20f, 10f, 0f); // Márgenes adicionales para el gráfico
        pieChart.invalidate();
    }





}
