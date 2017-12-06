package com.moming.jml.starmovie.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.moming.jml.starmovie.data.MovieContract;
import com.moming.jml.starmovie.utilities.NetworkUtils;
import com.moming.jml.starmovie.utilities.OpenMovieJsonUtilsFromMovieDb;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by jml on 2017/11/21.
 */

public class MovieSyncTask {

    synchronized public static void syncMovie(Context context){
        Log.v("syncMovie","pop");
        //pop request
        try {
            URL moviePopRequestUrl =
                    NetworkUtils.buildMoviePopUrlFromMovieDb();
            String jsonMoviePop = NetworkUtils.
                    getResponseFromHttpUrl(moviePopRequestUrl);

            ContentValues[] popValues = OpenMovieJsonUtilsFromMovieDb.getMovieContentValueFromJson(
                    context,jsonMoviePop,1
            );


            URL movieTopRequestUrl =
                    NetworkUtils.buildMovieTopUrlFromMovieDb();
            String jsonMovieTop = NetworkUtils.
                    getResponseFromHttpUrl(movieTopRequestUrl);
            ContentValues[] topValues = OpenMovieJsonUtilsFromMovieDb.getMovieContentValueFromJson(
                    context,jsonMovieTop,2
            );

            if (popValues != null && popValues .length !=0
                    &&topValues != null && topValues.length !=0){
                ContentResolver movieResolver = context.getContentResolver();
                movieResolver.delete(MovieContract.MovieEntry.CONTENT_URI,null,null);
                movieResolver.bulkInsert(
                        MovieContract.MovieEntry.CONTENT_URI,popValues);
                Log.v("更新数据","pop");
                movieResolver.bulkInsert(
                       MovieContract.MovieEntry.CONTENT_URI,topValues);
                Log.v("更新数据","top");


            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
