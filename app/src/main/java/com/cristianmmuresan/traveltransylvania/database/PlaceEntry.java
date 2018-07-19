package com.cristianmmuresan.traveltransylvania.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "place")
public class PlaceEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private double latitude;
    private double longitude;
    private int rank;
}
