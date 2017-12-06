package com.moming.jml.starmovie.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.moming.jml.starmovie.R;

/**
 * Created by admin on 2017/12/5.
 */

public class MoviePerference {

        public static void setSortWayFromPerf(Context context, String value){
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putString(context.getString(R.string.pref_sort_key),value);
            e.apply();
    }


}
