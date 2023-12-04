package com.example.fundpal.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fundpal.database.dao.DatabaseDao;
import com.example.fundpal.model.ModelDatabase;

@Database(entities = {ModelDatabase.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DatabaseDao databaseDao();
}