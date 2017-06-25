package com.example.awidcha.numbergame.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.awidcha.numbergame.constants.SharePreferenceKey;

/**
 * Created by Nirvana on 30/10/2559.
 */
public class SharePreferenceManager {

    private static SharePreferenceManager ourInstance = new SharePreferenceManager();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Context context;

    public static void setLeastPoint(int leastPoint) {
        editor.putBoolean(SharePreferenceKey.IS_LEAST_POINT_FOUND, true);
        editor.putInt(SharePreferenceKey.LEAST_POINT, leastPoint);

        // Commit changes
        editor.commit();
    }

    public static SharePreferenceManager getInstance() {
        return ourInstance;
    }

    public void setSharedPreferences(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SharePreferenceKey.SHARE_PREF, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public int getLeastPoint() {
        return sharedPreferences.getInt(SharePreferenceKey.LEAST_POINT, 0);
    }

    public boolean isLeastPointFound() {
        return sharedPreferences.getBoolean(SharePreferenceKey.IS_LEAST_POINT_FOUND, false);
    }
}
