package com.example.monify.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.monify.Entity.User;

import java.util.List;

@Dao
public interface userDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("DELETE FROM users WHERE id = :id")
    void deleteUserById(int id);

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int checkEmailExists(String email);

    @Query("SELECT * FROM users WHERE userName = :username AND password = :password")
    User login(String username, String password);
}