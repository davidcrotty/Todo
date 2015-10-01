package com.vualto.todo.appstart;

import android.content.Context;
import android.os.Environment;

import com.vualto.todo.BuildConfig;

/**
 * Created by David on 01/10/2015.
 */
public class Configuration {
    public static String getRealmDirectory(Context context) {
        if(BuildConfig.DEBUG) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + BuildConfig.APPLICATION_ID;
        }
        return context.getFilesDir().getPath();
    }
}
