package com.example.monify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;



import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.monify.Interface.AppDatabase;
import com.example.monify.credenciales.ingresar;
import com.example.monify.credenciales.crearCuenta;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = AppDatabase.getInstance(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void ingresar(View view){
        Intent intent = new Intent(this, ingresar.class);
        startActivity(intent);

    }

    public void crear_cuenta(View view){
        Intent intent = new Intent(this, crearCuenta.class);
        startActivity(intent);
        }
}


