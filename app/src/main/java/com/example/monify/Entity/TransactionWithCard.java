package com.example.monify.Entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TransactionWithCard {
    @Embedded
    public Transaccion transaccion;

    @Relation(
            parentColumn = "tarjetaId",
            entityColumn = "id"
    )
    public Tarjeta tarjeta;
}
