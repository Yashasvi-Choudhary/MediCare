package com.example.medicare;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {Medicine.class},
        version = 1,
        exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    public abstract MedicineDAO medicineDao();
}
