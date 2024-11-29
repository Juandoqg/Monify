package com.example.monify.Interface;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.monify.DAO.tarjetaDao;
import com.example.monify.DAO.transaccionDao;
import com.example.monify.Entity.Tarjeta;
import com.example.monify.Entity.User;
import com.example.monify.Entity.Transaccion;
import com.example.monify.DAO.userDao;

@Database(entities = {User.class, Tarjeta.class, Transaccion.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    public abstract  userDao userDao();
    public abstract tarjetaDao tarjetaDao();
    public abstract transaccionDao transaccionDao();

    // Declara la instancia única
    private static volatile AppDatabase INSTANCE;

    // Método para obtener la única instancia
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "monify.db")
                            .fallbackToDestructiveMigration()  // Elimina y recrea la base de datos si hay un cambio de esquema
                            .build();
                    Log.d("AppDatabase", "Base de datos creada: " + INSTANCE.getOpenHelper().getWritableDatabase().getPath());


                }
            }
        }
        return INSTANCE;
    }
}