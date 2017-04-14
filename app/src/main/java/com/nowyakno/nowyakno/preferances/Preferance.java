package com.nowyakno.nowyakno.preferances;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LENOVO on 06-04-2017.
 */

public class Preferance {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    public Preferance(Context context) {
        this.context = context;
    }

    public SharedPreferences getPreferanceInstance() {
        return preferences = context.getSharedPreferences("NEW_YAKNO", context.MODE_PRIVATE);
    }
    public SharedPreferences.Editor getEditor(SharedPreferences preferences){
        editor=preferences.edit();
        return editor;
    }
}
