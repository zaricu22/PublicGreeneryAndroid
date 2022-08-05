package com.example.myapplication.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

// Room baza je bibl koja olaksava koriscenje SQLLite baze
// exportSchema - schemaLocation good practice to have version history even though it is not mandatory
@Database(entities = {User.class,Event.class,Job.class}, version = 9, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract DatabaseDAO databaseDao();
}