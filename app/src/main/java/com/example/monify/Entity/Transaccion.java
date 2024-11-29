package com.example.monify.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "transacciones",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Tarjeta.class,
                        parentColumns = "id",
                        childColumns = "tarjetaId",
                        onDelete = ForeignKey.CASCADE)
        })
public class Transaccion {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String tipo; // "Agregar" o "Gastar"
    private double monto;
    private int userId;  // Clave foránea hacia Usuario
    private int tarjetaId;  // Clave foránea hacia Tarjeta

    // Constructor
    public Transaccion(String tipo, double monto, int userId, int tarjetaId) {
        this.tipo = tipo;
        this.monto = monto;
        this.userId = userId;
        this.tarjetaId = tarjetaId;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTarjetaId() {
        return tarjetaId;
    }

    public void setTarjetaId(int tarjetaId) {
        this.tarjetaId = tarjetaId;
    }
}
