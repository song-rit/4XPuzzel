package com.example.awidcha.numbergame.utils;

/**
 * Created by Awidcha on 25/6/2560.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.awidcha.numbergame.constants.Point;
import com.example.awidcha.numbergame.model.PointModel;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();

    private SQLiteDatabase sqLiteDatabase;

    public DBHelper(Context context) {
        super(context, Point.DATABASE_NAME, null, Point.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_FRIEND_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY  AUTOINCREMENT, %s TEXT)",
                Point.TABLE,
                Point.Column.ID,
                Point.Column.FASTEST_POINT
        );

        Log.i(TAG, CREATE_FRIEND_TABLE);

        // create table
        db.execSQL(CREATE_FRIEND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_FRIEND_TABLE = "DROP TABLE IF EXISTS " + Point.TABLE;

        db.execSQL(DROP_FRIEND_TABLE);

        Log.i(TAG, "Upgrade Database from " + oldVersion + " to " + newVersion);

        onCreate(db);
    }

    public List<PointModel> getAllPoint() {

        String QUERY_ALL_FRIEND = "SELECT * FROM " + Point.TABLE;

        List<PointModel> userModels = new ArrayList<>();

        sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(QUERY_ALL_FRIEND, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            PointModel userModel = new PointModel();
            userModel.setId((int) cursor.getLong(0));
            userModel.setFastestPoint(cursor.getString(1));
            userModels.add(userModel);

            cursor.moveToNext();
        }

        sqLiteDatabase.close();


        return userModels;
    }


    public void addPoint(PointModel point) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Point.Column.FASTEST_POINT, point.getFastestPoint());

        sqLiteDatabase.insert(Point.TABLE, null, values);
        sqLiteDatabase.close();
    }

    public void deleteAllPoint() {

        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Point.TABLE, null, null);
        sqLiteDatabase.close();
    }

    //UPDATE
    public void updateFriend(PointModel point) {

        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Point.Column.ID, point.getId());
        values.put(Point.Column.FASTEST_POINT, point.getFastestPoint());

        int row = sqLiteDatabase.update(Point.TABLE,
                values,
                Point.Column.ID + " = ? ",
                new String[]{String.valueOf(point.getId())});

        sqLiteDatabase.close();
    }

}