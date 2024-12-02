package com.example.monify.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.monify.Entity.Transaccion;
import com.example.monify.DAO.transaccionDao;
import com.example.monify.Interface.AppDatabase;

import java.util.List;

public class TransaccionViewModel extends AndroidViewModel {

    private final AppDatabase db;

    public TransaccionViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<List<Transaccion>> getTransaccionesConUsuario(int userId) {
        return db.transaccionDao().getTransaccionesConUsuario(userId);
    }
}
