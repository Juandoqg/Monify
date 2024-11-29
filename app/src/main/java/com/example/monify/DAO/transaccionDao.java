package com.example.monify.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.monify.Entity.Transaccion;

import java.util.List;

@Dao
public interface transaccionDao {

    @Insert
    void insertarTransaccion(Transaccion transaccion);

    // Obtener transacciones por usuario
    @Query("SELECT * FROM transacciones WHERE userId = :userId")
    List<Transaccion> obtenerTransaccionesPorUsuario(int userId);

    // Obtener transacciones por tarjeta
    @Query("SELECT * FROM transacciones WHERE tarjetaId = :tarjetaId")
    List<Transaccion> obtenerTransaccionesPorTarjeta(int tarjetaId);

    // Obtener todas las transacciones
    @Query("SELECT * FROM transacciones")
    List<Transaccion> obtenerTodasLasTransacciones();
}
