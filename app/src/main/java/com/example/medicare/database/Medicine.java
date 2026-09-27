package com.example.medicare.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicine")
public class Medicine {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String dosage;
    public int quantity;
    public String time;
    public String frequency;
    public String startDate;
    public String endDate;
}
