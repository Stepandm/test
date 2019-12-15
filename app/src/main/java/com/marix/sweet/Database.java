package com.marix.sweet;

import androidx.room.RoomDatabase;


@androidx.room.Database(entities = {Doska.class}, version = 4)
public abstract class Database extends RoomDatabase {
    public abstract Dao dao();
}
