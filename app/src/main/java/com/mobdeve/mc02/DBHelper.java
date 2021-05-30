package com.mobdeve.mc02;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Mobdeve.db";
    public static final String MARKER_TABLE_NAME = "markers";
    public static final String MARKER_COLUMN_ID = "id";
//    public static final String MARKER_NAME = "name";
//    public static final String MARKER_NOTES = "notes";
//    public static final String MARKER_LAT = "latitude";
//    public static final String MARKER_LNG = "longtitude";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table markers" + "(id integer primary key, name text, notes text, latitude real, longtitude real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertMarker(String name, String notes, double lat, double lng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("notes", notes);
        contentValues.put("latitude", lat);
        contentValues.put("longtitude", lng);
        db.insert("markers", null, contentValues);
    }

    public void deleteMarker(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MARKER_TABLE_NAME, MARKER_COLUMN_ID + "=" + id, null);
    }

    public boolean isUniqueName(String string){
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res = db.rawQuery("SELECT * FROM markers WHERE name = ?", new String[]{string});
        return !res.moveToFirst();
    }

    public LatLng markerPosition(String string){
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res = db.rawQuery("SELECT * FROM markers WHERE name = ?", new String[]{string});
        res.moveToFirst();
        double lat = res.getDouble(res.getColumnIndex("latitude"));
        double lng = res.getDouble(res.getColumnIndex("longtitude"));
        return new LatLng(lat,lng);
    }

    public void getAllMarkers(GoogleMap googleMap) {
        Marker marker;
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res = db.rawQuery("SELECT * FROM markers", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            int id = res.getInt(res.getColumnIndex("id"));
            String name = res.getString(res.getColumnIndex("name"));
            String notes = res.getString(res.getColumnIndex("notes"));
            double lat = res.getDouble(res.getColumnIndex("latitude"));
            double lng = res.getDouble(res.getColumnIndex("longtitude"));

            marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(name)
                    .snippet(notes)
            );
            marker.setTag(id);
            res.moveToNext();
        }
    }
}
