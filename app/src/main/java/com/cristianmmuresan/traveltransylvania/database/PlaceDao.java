package com.cristianmmuresan.traveltransylvania.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM place ORDER BY id")
    LiveData<List<PlaceEntry>> loadAllPlaces();

    @Insert
    void insertPlace(PlaceEntry placeEntry);

    @Query("SELECT * FROM place WHERE id = :id")
    LiveData<PlaceEntry> loadPlaceById(int id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlace(PlaceEntry placeEntry);
}
