package com.example.monify.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import com.example.monify.Entity.User;

@Entity(
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        tableName = "tarjetas"  // Especifica el nombre de la tabla en la base de datos
)
public class Tarjeta {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nombre;   // Nombre de la tarjeta, como "Visa" o "Mastercard"

    @ColumnInfo(name = "numero")
    private String numero;   // Número de la tarjeta como String

    private int userId;      // Clave foránea, referencia al ID del usuario

    private double saldo;    // Saldo de la tarjeta

    private String fechaExpiracion; // Fecha de expiración de la tarjeta (formato: "MM/AAAA")

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public String getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(String fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    @Override
    public String toString() {
        return nombre + " - " + numero.substring(numero.length() - 4); // Muestra el nombre y los últimos 4 dígitos del número
    }
}

