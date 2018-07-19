package com.cristianmmuresan.traveltransylvania.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
interface PlaceDao {

    @Query("SELECT * FROM place ORDER BY rank")
    LiveData<List<PlaceEntry>> loadAllPlaces();
}
