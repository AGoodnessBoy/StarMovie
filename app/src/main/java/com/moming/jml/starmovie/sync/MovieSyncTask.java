package com.moming.jml.starmovie.sync;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

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
        //pop request
        try {
            URL moviePopRequestUrl =
                    NetworkUtils.buildMoviePopUrlFromMovieDb();
            String jsonMoviePop = NetworkUtils.
                    getResponseFromHttpUrl(moviePopRequestUrl);

            ContentValues[] popValues = OpenMovieJsonUtilsFromMovieDb.getMovieContentValueFromJson(
                    context,jsonMoviePop,1
            );

            if (popValues != null && popValues .length !=0){
                ContentResolver movieResolver = context.getContentResolver();
                movieResolver.delete(
                        MovieContract.MovieEntry.CONTENT_URI,
                        MovieContract.MovieEntry.COLUMN_MOVIE_POP+"=1 ",
                        null);
                movieResolver.bulkInsert(
                        MovieContract.MovieEntry.CONTENT_URI,popValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //top request
        try {
            URL movieTopRequestUrl =
                    NetworkUtils.buildMovieTopUrlFromMovieDb();
            String jsonMovieTop = NetworkUtils.
                    getResponseFromHttpUrl(movieTopRequestUrl);

            ContentValues[] topValues = OpenMovieJsonUtilsFromMovieDb.getMovieContentValueFromJson(
                    context,jsonMovieTop,2
            );

            if (topValues != null && topValues .length !=0){
                ContentResolver movieResolver = context.getContentResolver();
                movieResolver.delete(
                        MovieContract.MovieEntry.CONTENT_URI,
                        MovieContract.MovieEntry.COLUMN_MOVIE_TOP+"=1 ",
                        null);
                movieResolver.bulkInsert(
                        MovieContract.MovieEntry.CONTENT_URI,topValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
