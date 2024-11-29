package com.example.monify.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "users",
        indices = {@Index(value = "email", unique = true)}
)
public class User {
   @PrimaryKey(autoGenerate = true)
   public int id;  // El ID de usuario se autogenera

   @ColumnInfo(name = "userName")
   public String userName;

   @ColumnInfo(name = "email")
   @NonNull
   public String email;

   @ColumnInfo(name = "password")
   public String password;

   // Getters y Setters
   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }
}
