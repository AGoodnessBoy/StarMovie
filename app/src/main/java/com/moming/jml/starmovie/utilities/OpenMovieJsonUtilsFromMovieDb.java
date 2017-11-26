package com.moming.jml.starmovie.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.moming.jml.starmovie.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by admin on 2017/9/28.
 */

/*
{
  "adult": false,
  "backdrop_path": "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg",
  "belongs_to_collection": null,
  "budget": 63000000,
  "genres": [
    {
      "id": 18,
      "name": "Drama"
    }
  ],
  "homepage": "",
  "id": 550,
  "imdb_id": "tt0137523",
  "original_language": "en",
  "original_title": "Fight Club",
  "overview": "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \"fight clubs\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
  "popularity": 0.5,
  "poster_path": null,
  "production_companies": [
    {
      "name": "20th Century Fox",
      "id": 25
    }
  ],
  "production_countries": [
    {
      "iso_3166_1": "US",
      "name": "United States of America"
    }
  ],
  "release_date": "1999-10-12",
  "revenue": 100853753,
  "runtime": 139,
  "spoken_languages": [
    {
      "iso_639_1": "en",
      "name": "English"
    }
  ],
  "status": "Released",
  "tagline": "How much can you know about yourself if you've never been in a fight?",
  "title": "Fight Club",
  "video": false,
  "vote_average": 7.8,
  "vote_count": 3439
}

 */

public final class OpenMovieJsonUtilsFromMovieDb {
    //the movie db api json 参数
    final static String MOVIE_ID="id"; //电影ID
    final static String MOVIE_TITLE="title";//电影标题
    final static String MOVIE_IMG_PATH="backdrop_path";//电影图片
    final static String MOVIE_VOTE="vote_average";//电影评分
    final static String MOVIE_OVERVIEW="overview";//电影简介
    final static String MOVIE_TYPE="genres";//电影类型
    final static String MOVIE_COMPANY="production_companies";//电影公司
    final static String MOVIE_STATUS="status";//发行状态
    final static String MOVIE_RELEASE_DATE="release_date";//发行时间
    final static String MOVIE_RUNTIME="runtime";//电影时长
    final static String MOVIE_REVENUE="revenue";//票房
    final static String MOVIE_POSTER_PATH="poster_path";//电影海报

    final static int MOVIE_POP = 1;
    final static int MOVIE_TOP = 2;



    public static ContentValues[] getMovieContentValueFromJson(Context context,String movieJsonStr,int type)
    throws JSONException{
        JSONObject movieJson = new JSONObject(movieJsonStr);
        if (movieJson.has("status_code")){
            int errorCode = movieJson.getInt("status_code");
            String errorMsg = movieJson.getString("status_message");
            if (errorCode==34||errorCode==7){
                Toast.makeText(context,errorMsg,
                        Toast.LENGTH_LONG).show();
                return null;
            }
        }
        Log.v("json",movieJson.toString());

        JSONArray movieArray= movieJson.getJSONArray("results");

        //导入数据

        ContentValues[] movieCVs = new ContentValues[movieArray.length()];

        for (int i=0 ; i<movieArray.length() ;i++) {

            ContentValues movieValues = new ContentValues();
            JSONObject movieTempObj = movieArray.getJSONObject(i);

            String movie_id = movieTempObj.getString(MOVIE_ID);
            String movie_post = movieTempObj.getString(MOVIE_POSTER_PATH);
            int movie_vote = movieTempObj.getInt(MOVIE_VOTE);
            String movie_title = movieTempObj.getString(MOVIE_TITLE);
            String movie_img = movieTempObj.getString(MOVIE_IMG_PATH);
            String movie_release = movieTempObj.getString(MOVIE_RELEASE_DATE);
            String movie_overview =movieTempObj.getString(MOVIE_OVERVIEW);

            //String movie_video = getMovieVideoJsonFromMD(movie_id);
            //String movie_reviews =getMovieReviewsJsonFromMD(movie_id);
           // String movie_item = getMovieItemJsonFromMD(movie_id);







            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,movie_id);
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE,movie_img);
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,movie_post);
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,movie_title);
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE,movie_release);
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE,movie_vote);
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,movie_overview);
            //movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER,movie_video);
            //movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_COMMEMT,movie_reviews);



            switch (type) {
                case MOVIE_POP:
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POP, 1);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TOP, 0);
                    break;
                case MOVIE_TOP:
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POP, 0);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TOP, 1);
                    break;
                default:
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POP, 0);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TOP, 0);
            }


            movieCVs[i]=movieValues;

        }

        return movieCVs;
    }

    public static String getMovieVideoJsonFromMD(String id){
        try {
            URL videoUrl = NetworkUtils.buildMovieVideoUrlFromMovieDbById(id);
            String videoReq = NetworkUtils.getResponseFromHttpUrl(videoUrl);
           return videoReq;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getMovieReviewsJsonFromMD(String id){
        try {
            URL reviewsUrl = NetworkUtils.buildMovieReviewsUrlFromMovieDbById(id);
            String reviewsReq = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);
            return reviewsReq;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getMovieItemJsonFromMD(String id){
        try {
            URL itemUrl = NetworkUtils.buildMovieItemUrlFromMovieDbById(id);
            String itemReq = NetworkUtils.getResponseFromHttpUrl(itemUrl);
            return itemReq;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }







}
