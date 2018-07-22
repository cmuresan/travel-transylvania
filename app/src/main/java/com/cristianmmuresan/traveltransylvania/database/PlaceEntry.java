package com.cristianmmuresan.traveltransylvania.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "place")
public class PlaceEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String readMoreLink;
    private double latitude;
    private double longitude;
    private float zoom;
    private int imageResources;
    private boolean isFavorite;
    private boolean isCity;
    private boolean isVillage;
    private boolean isCastle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReadMoreLink() {
        return readMoreLink;
    }

    public void setReadMoreLink(String readMoreLink) {
        this.readMoreLink = readMoreLink;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public int getImageResources() {
        return imageResources;
    }

    public void setImageResources(int imageResources) {
        this.imageResources = imageResources;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isCity() {
        return isCity;
    }

    public void setCity(boolean city) {
        isCity = city;
    }

    public boolean isVillage() {
        return isVillage;
    }

    public void setVillage(boolean village) {
        isVillage = village;
    }

    public boolean isCastle() {
        return isCastle;
    }

    public void setCastle(boolean castle) {
        isCastle = castle;
    }

    @Override
    public String toString() {
        return "Check out this awesome place: " +
                name + "\n" +
                description + "\n" +
                readMoreLink;
    }
}
