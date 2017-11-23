package com.moming.jml.starmovie.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.moming.jml.starmovie.data.MovieContract;

/**
 * Created by jml on 2017/11/23.
 */

public class MovieSyncUtils {

    private static boolean sInitialized;

    private static final String MOVIE_SYNC_TAG= "movie-sync";

    synchronized public static void initialize(@NonNull final Context context){
        if (sInitialized)return;

        sInitialized =true;

        Thread checkForEmpty = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                        String[] projectionColumns ={MovieContract.MovieEntry._ID};
                        String selction = MovieContract.MovieEntry.COLUMN_MOVIE_ID
                                +" >= 0";
                        Cursor cursor = context.getContentResolver().query(
                                uri,projectionColumns,selction,null,null
                        );

                        if (null == cursor || cursor.getCount() == 0){
                            startImmediateSync(context);
                        }
                        cursor.close();
                    }
                }
        );

        checkForEmpty.start();

    }

    public static void startImmediateSync(@NonNull final Context context){
        Intent intent = new Intent(context,MovieSyncIntentService.class);
        context.startService(intent);
    }
}
