package com.moming.jml.starmovie.utilities;

import android.content.Context;
import android.util.Log;

import com.moming.jml.starmovie.entities.MovieEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jml on 2017/9/22.
 */

public final class OpenMovieJsonUtils {

    final static String MOVIE_SUBJECTS="subjects";
    final static String MOVIE_RATING="rating";
    final static String MOVIE_TITLE="title";
    final static String RATING_AVERAGE="average";
    final static String MOVIE_IMAGE="images";
    final static String MOVIE_URL="alt";
    final static String MOVIE_ID="id";
    final static String MOVIE_SUMMARY="summary";
    final static String MOVIE_CASTS="casts";
    final static String JSON_CODE="code";
    final static String MOVIE_YEAR="year";

    public static MovieEntity getMovieItemFromJson(Context context,String singleMovieJsonStr) throws JSONException{
        MovieEntity singleMovieItem= new MovieEntity();
        JSONObject movieSubject=new JSONObject(singleMovieJsonStr);
        if (movieSubject.has(JSON_CODE)){
            int errorCode = movieSubject.getInt(JSON_CODE);

            switch (errorCode){
                case 1000:
                    Log.v("api_ERROR","需要权限");
                    return null;
                case 1001:
                    Log.v("api_ERROR","资源不存在");
                    return null;
                case 1002:
                    Log.v("api_ERROR","参数不全");
                    return null;
                case 999:
                    Log.v("api_ERROR","未知错误");
                    return null;
                case 1007:
                    Log.v("api_ERROR","需要验证码，验证码有误");
                    return null;
                default:return null;
            }

        }
        String movie_name;//电影名称
        String movie_rating;//电影评分
        String movie_id;//电影ID
        String movie_url;//电影链接
        String movie_img_url;//电影图片
        String movie_year;//电影年份
        String movie_summary;//电影简介
        JSONArray movie_casts;//电影主演
        JSONObject movie_rating_obj;
        JSONObject movie_img_obj;
        movie_name = movieSubject.getString(MOVIE_TITLE);
        movie_rating_obj =movieSubject.getJSONObject(MOVIE_RATING);
        movie_rating=movie_rating_obj.getString(RATING_AVERAGE);
        movie_id = movieSubject.getString(MOVIE_ID);
        movie_url= movieSubject.getString(MOVIE_URL);
        movie_img_obj = movieSubject.getJSONObject(MOVIE_IMAGE);
        movie_img_url = movie_img_obj.getString("large");
        movie_summary = movieSubject.getString(MOVIE_SUMMARY);
        movie_casts=movieSubject.getJSONArray(MOVIE_CASTS);
        movie_year = movieSubject.getString(MOVIE_YEAR);

        singleMovieItem.setMovie_img_url(movie_img_url);
        singleMovieItem.setMovie_url(movie_url);
        singleMovieItem.setMovie_rating(movie_rating);
        singleMovieItem.setMovie_name(movie_name);
        singleMovieItem.setMovie_id(movie_id);
        singleMovieItem.setMovie_summary(movie_summary);
        singleMovieItem.setMovie_casts(movie_casts);


        return singleMovieItem;
    }

    public static MovieEntity[] getMovieListFromJson(Context context, String movieJsonStr)
            throws JSONException {


        MovieEntity[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);
        if (movieJson.has(JSON_CODE)){
            int errorCode = movieJson.getInt(JSON_CODE);

            switch (errorCode){
                case 1000:
                    Log.v("api_ERROR","需要权限");
                    return null;
                case 1001:
                    Log.v("api_ERROR","资源不存在");
                    return null;
                case 1002:
                    Log.v("api_ERROR","参数不全");
                    return null;
                case 999:
                    Log.v("api_ERROR","未知错误");
                    return null;
                case 1007:
                    Log.v("api_ERROR","需要验证码，验证码有误");
                    return null;
                default:return null;
            }

        }

        JSONArray movieArray = movieJson.getJSONArray(MOVIE_SUBJECTS);
        parsedMovieData = new MovieEntity[movieArray.length()];
        Log.v("json",movieJson.toString());
        for (int i = 0; i<movieArray.length();i++){

            String sMovieName=null;
            String sMovieRating=null;
            String sMovieId=null;
            String sMovieUrl=null;
            String sMovieImageUrl=null;
            JSONObject obj=movieArray.getJSONObject(i);
            MovieEntity theMovie=new MovieEntity();
            sMovieId=obj.getString(MOVIE_ID);
            sMovieName=obj.getString(MOVIE_TITLE);
            JSONObject jMovieRating=obj.getJSONObject(MOVIE_RATING);
            sMovieRating = jMovieRating.getString(RATING_AVERAGE);
            sMovieUrl=obj.getString(MOVIE_URL);
            JSONObject jMovieImage=obj.getJSONObject(MOVIE_IMAGE);
            sMovieImageUrl=jMovieImage.getString("large");

            theMovie.setMovie_id(sMovieId);
            theMovie.setMovie_name(sMovieName);
            theMovie.setMovie_rating(sMovieRating);
            theMovie.setMovie_url(sMovieUrl);
            theMovie.setMovie_img_url(sMovieImageUrl);
            parsedMovieData[i]=theMovie;
            if (obj!=null||theMovie!=null){
                obj=null;
                theMovie=null;
            }
        }


        return parsedMovieData;


    }
}
