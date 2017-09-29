package com.moming.jml.starmovie.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.moming.jml.starmovie.entities.NewMovieEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static NewMovieEntity[] getMovieListFromMovieDb(Context context,String callBackJson)
        throws JSONException{

            NewMovieEntity[] movielist = null;

        JSONObject movieJsonObj = new JSONObject(callBackJson);
        if (movieJsonObj.has("status_code")){
            int errorCode = movieJsonObj.getInt("status_code");
            String errorMsg = movieJsonObj.getString("status_message");
            if (errorCode==34||errorCode==7){
                Toast.makeText(context,errorMsg,
                        Toast.LENGTH_LONG).show();
                return null;
            }
        }
        Log.v("json",callBackJson);
        JSONArray movieArray= movieJsonObj.getJSONArray("results");

        int movienumber= movieArray.length();
        movielist = new  NewMovieEntity[movienumber];
        for (int i=0;i<movienumber;i++){
            NewMovieEntity movieTemp = new NewMovieEntity();
            JSONObject movieTempObj = movieArray.getJSONObject(i);
            movieTemp.setId(movieTempObj.getString(MOVIE_ID));
            movieTemp.setImg_path(movieTempObj.getString(MOVIE_IMG_PATH));
            movieTemp.setTitle(movieTempObj.getString(MOVIE_TITLE));
            movieTemp.setVote(movieTempObj.getString(MOVIE_VOTE));
            movielist[i]=movieTemp;
        }
        return movielist;
    }

    public static NewMovieEntity getMovieItemFromMovieDb(Context context,String callbackJson)
        throws JSONException{
        NewMovieEntity movieEntity = new NewMovieEntity();

        JSONObject movieItemObj = new JSONObject(callbackJson);
        if (movieItemObj.has("status_code")){
            int errorCode = movieItemObj.getInt("status_code");
            String errorMsg = movieItemObj.getString("status_message");
            if (errorCode==34||errorCode==7){
                Toast.makeText(context,errorMsg,
                        Toast.LENGTH_LONG).show();
                return null;
            }
        }

        movieEntity.setId(movieItemObj.getString(MOVIE_ID));
        movieEntity.setTitle(movieItemObj.getString(MOVIE_TITLE));
        movieEntity.setImg_path(movieItemObj.getString(MOVIE_IMG_PATH));
        movieEntity.setVote(movieItemObj.getString(MOVIE_VOTE));
        movieEntity.setType(movieItemObj.getJSONArray(MOVIE_TYPE));
        movieEntity.setCompany(movieItemObj.getJSONArray(MOVIE_COMPANY));
        movieEntity.setOverview(movieItemObj.getString(MOVIE_OVERVIEW));
        movieEntity.setStatus(movieItemObj.getString(MOVIE_STATUS));
        movieEntity.setRuntime(movieItemObj.getString(MOVIE_RUNTIME));
        movieEntity.setRevenue(movieItemObj.getString(MOVIE_REVENUE));
        movieEntity.setRelease_date(movieItemObj.getString(MOVIE_RELEASE_DATE));

        return movieEntity;

    }




}
