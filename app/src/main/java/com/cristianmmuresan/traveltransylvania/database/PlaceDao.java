package com.cristianmmuresan.traveltransylvania.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM place ORDER BY id")
    LiveData<List<PlaceEntry>> loadAllPlaces();

    @Insert
    void insertPlace(PlaceEntry placeEntry);

    @Query("SELECT * FROM place WHERE id = :id")
    LiveData<PlaceEntry> loadPlaceById(int id);
}
