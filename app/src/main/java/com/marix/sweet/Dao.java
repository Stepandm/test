package com.marix.sweet;


import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Query("SELECT * FROM doska")
    List<Doska> getAll();

    @Query("SELECT * FROM doska WHERE id = :id")
    Doska getById(long id);

    @Insert
    void insert(Doska employee);

    @Update
    void update(Doska employee);

    @Delete
    void delete(Doska employee);
}
