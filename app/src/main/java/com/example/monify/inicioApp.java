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
import androidx.lifecycle.ViewModelProvider;

import com.example.monify.Entity.Transaccion;
import com.example.monify.viewmodel.TransaccionViewModelFactory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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
        BarChart barChart = findViewById(R.id.barChart);

        // Datos para el gráfico
        float totalIngresos = 0f;
        float totalGastos = 0f;

        for (Transaccion transaccion : transacciones) {
            if (transaccion.getTipo().equalsIgnoreCase("Ingreso")) {
                totalIngresos += transaccion.getMonto();
            } else if (transaccion.getTipo().equalsIgnoreCase("Gasto")) {
                totalGastos += transaccion.getMonto();
            }
        }

        // Crear las entradas para las barras
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, totalIngresos)); // Posición 0: Ingresos
        entries.add(new BarEntry(1f, totalGastos));  // Posición 1: Gastos

        // Crear el dataset
        BarDataSet dataSet = new BarDataSet(entries, "Transacciones");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        // Configurar los datos del gráfico
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // Ancho de las barras

        barChart.setData(barData);
        barChart.setFitBars(true); // Ajusta las barras al eje X
        barChart.getDescription().setEnabled(false); // Desactiva la descripción
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{"Ingresos", "Gastos"}));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getAxisRight().setEnabled(false); // Oculta el eje derecho
        barChart.getAxisLeft().setAxisMinimum(0f); // Eje izquierdo empieza en 0
        barChart.animateY(1000); // Animación del gráfico
        barChart.invalidate(); // Refresca el gráfico
    }

}
