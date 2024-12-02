package com.example.monify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
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
        SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1); // Asegúrate de que se haya guardado previamente el userId

        TransaccionViewModelFactory factory = new TransaccionViewModelFactory(getApplication());
        TransaccionViewModel viewModel = new ViewModelProvider(this, factory).get(TransaccionViewModel.class);

        // Observa las transacciones y actualiza la UI
        viewModel.getTransaccionesConUsuario(userId).observe(this, transacciones -> {
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
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_dashboard) {

                Intent intent = new Intent(inicioApp.this, TarjetasActivity.class);
                startActivity(intent);
                return true;  // Evento manejado
            } else if (id == R.id.navigation_movements) {

                Intent intent = new Intent(inicioApp.this, MovementsActivity.class);
                startActivity(intent);
                return true;  // Evento manejado
            }

            return false;  // No manejado
        });
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Infla el menú de navegación en la parte inferior
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
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

    private void graficarBarChart(BarChart barChart, float totalIngresos, float totalGastos) {
        // Crear las entradas para las barras
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, totalIngresos)); // Posición 0: Ingresos
        entries.add(new BarEntry(1f, totalGastos));  // Posición 1: Gastos

        // Crear el dataset para el gráfico de barras
        BarDataSet dataSet = new BarDataSet(entries, ""); // Sin título para el dataset general
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);  // Colores para las barras
        dataSet.setValueTextSize(12f);

        // Crear los datos del gráfico de barras
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

        // Habilitar y configurar la leyenda
        Legend legend = barChart.getLegend();
        legend.setEnabled(true);  // Habilitar la leyenda
        legend.setForm(Legend.LegendForm.SQUARE);  // Formato cuadrado para los cuadros de color
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);  // Alinear horizontalmente a la izquierda
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);  // Alinear verticalmente en la parte inferior
        legend.setFormSize(10f); // Tamaño de los cuadros en la leyenda
        legend.setTextSize(12f);  // Tamaño del texto de las leyendas
        legend.setXEntrySpace(60f);  // Asegurar espacio entre las entradas horizontales
        legend.setYEntrySpace(10f);  // Asegurar espacio entre las entradas verticales

        // Agregar más espacio a la izquierda (si es necesario)
        legend.setXOffset(90f);  // Ajustar el desplazamiento de la leyenda a la izquierda

        // Personalizar la leyenda para que "Ingreso" y "Gasto" aparezcan con los colores correspondientes
        List<LegendEntry> legendEntries = new ArrayList<>();
        legendEntries.add(new LegendEntry("Ingreso", Legend.LegendForm.SQUARE, 10f, 10f, null, ColorTemplate.MATERIAL_COLORS[0]));  // Verde (o el primer color)
        legendEntries.add(new LegendEntry("Gasto", Legend.LegendForm.SQUARE, 10f, 10f, null, ColorTemplate.MATERIAL_COLORS[1]));   // Amarillo (o el segundo color)
        legend.setCustom(legendEntries); // Establecer las entradas personalizadas en la leyenda

        barChart.invalidate(); // Refrescar el gráfico
    }





    private void graficarPieChart(PieChart pieChart, int visaCount, int masterCardCount) {
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(visaCount, "VISA"));
        pieEntries.add(new PieEntry(masterCardCount, "MASTERCARD"));

        // Crear el dataset para el gráfico de pie
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);  // Colores para las porciones
        pieDataSet.setValueTextSize(12f);  // Tamaño del texto para los valores

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

        // Ajustar márgenes adicionales para el gráfico
        pieChart.setExtraOffsets(10f, 20f, 10f, 0f);

        // Habilitar los nombres dentro del gráfico (porciones)
        pieChart.setDrawEntryLabels(true);  // Activar los nombres de las entradas

        // Establecer el color de los nombres de las entradas dentro del gráfico
        pieChart.setEntryLabelColor(Color.BLACK);  // Establece el color negro para los nombres dentro del pie

        // Centrar la leyenda
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true); // Habilitar la leyenda
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER); // Centrar horizontalmente
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM); // Colocar en la parte inferior
        legend.setForm(Legend.LegendForm.SQUARE); // Establecer el estilo de la leyenda
        legend.setFormSize(10f); // Tamaño de los cuadros de la leyenda
        legend.setTextSize(12f); // Tamaño del texto de la leyenda
        legend.setXEntrySpace(20f); // Espacio horizontal entre los elementos de la leyenda
        legend.setYEntrySpace(10f); // Espacio vertical entre los elementos de la leyenda

        pieChart.invalidate(); // Refrescar el gráfico
    }
}
