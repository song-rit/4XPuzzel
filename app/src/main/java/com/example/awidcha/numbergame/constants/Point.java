package com.example.awidcha.numbergame.constants;

import android.provider.BaseColumns;

/**
 * Created by Awidcha on 23/6/2560.
 */

public class Point {
    //Database
    public static final String DATABASE_NAME = "awidcha.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "point";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String FASTEST_POINT = "fastest_point";


    }
}
