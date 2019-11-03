package com.softsqaured.softsquared_as5.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MyLocation myLocation);

    @Query("SELECT * FROM mylocation_table")
    List<MyLocation> getAllLocations();

    @Query("SELECT * FROM mylocation_table WHERE location =:location")
    List<MyLocation> getAllLocationsWithLocation(String location);

    @Query("DELETE FROM mylocation_table WHERE location =:location")
    void deleteByLocation(String location);

    @Delete
    void delete(MyLocation myLocation);
}
