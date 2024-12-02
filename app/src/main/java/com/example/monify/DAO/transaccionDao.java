package com.example.monify.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.monify.Entity.Transaccion;
import com.example.monify.Entity.TransactionWithCard;

import java.util.List;

@Dao
public interface transaccionDao {

    @Insert
    void insertarTransaccion(Transaccion transaccion);

    // Obtener transacciones por usuario
    @Transaction
    @Query("SELECT * FROM transacciones WHERE userId = :userId")
    List<TransactionWithCard> obtenerTransaccionesConTarjeta(int userId);

    @Query("SELECT * FROM transacciones WHERE userId = :userId")
    LiveData<List<Transaccion>> getTransaccionesConUsuario(int userId);
    // Obtener transacciones por tarjeta
    @Query("SELECT * FROM transacciones WHERE tarjetaId = :tarjetaId")
    List<Transaccion> obtenerTransaccionesPorTarjeta(int tarjetaId);

    // Obtener todas las transacciones
    @Query("SELECT * FROM transacciones")
    LiveData<List<Transaccion>> obtenerTodasLasTransacciones();

    @Query("SELECT nombre FROM tarjetas WHERE id = :tarjetaId LIMIT 1")
    String getNombreTarjeta(int tarjetaId);
}
