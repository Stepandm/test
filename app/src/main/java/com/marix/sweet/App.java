package com.marix.sweet;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {
    public static App instance;

    private Database database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, Database.class, "database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public Database getDatabase() {
        return database;
    }
}
