package com.cristianmmuresan.traveltransylvania.datacomposer;

import android.content.Context;
import android.content.SharedPreferences;

public class DataComposerPersister {
    private static final boolean IS_FIRST_RUN_DEFAULT = true;
    private static final String IS_FIRST_RUN_KEY = "isFirstRun";
    private final SharedPreferences sharedPreferences;

    public DataComposerPersister(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public boolean getIsFirstRun() {
        boolean result = sharedPreferences.getBoolean(IS_FIRST_RUN_KEY, IS_FIRST_RUN_DEFAULT);
        setIsFirstRun(false);
        return result;
    }

    private void setIsFirstRun(boolean value) {
        sharedPreferences.edit().putBoolean(IS_FIRST_RUN_KEY, value).apply();
    }
}
