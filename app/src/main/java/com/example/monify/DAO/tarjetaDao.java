package com.example.monify.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.monify.Entity.Tarjeta;

import java.util.List;

@Dao
public interface tarjetaDao {

    // Insertar una nueva tarjeta
    @Insert
    void insertarTarjeta(Tarjeta tarjeta);

    // Eliminar una tarjeta
    @Delete
    void eliminarTarjeta(Tarjeta tarjeta);

    // Obtener todas las tarjetas de un usuario (por su ID)
    @Query("SELECT * FROM tarjetas WHERE userId = :userId")
    List<Tarjeta> obtenerTarjetasPorUsuario(int userId);

    // Obtener una tarjeta específica por su ID
    @Query("SELECT * FROM tarjetas WHERE id = :tarjetaId")
    Tarjeta obtenerTarjetaPorId(int tarjetaId);

    // Eliminar todas las tarjetas de un usuario (si fuera necesario)
    @Query("DELETE FROM tarjetas WHERE userId = :userId")
    void eliminarTodasLasTarjetas(int userId);

    @Query("UPDATE tarjetas SET saldo = :nuevoSaldo WHERE id = :tarjetaId")
    void actualizarSaldo(int tarjetaId, double nuevoSaldo);
}
