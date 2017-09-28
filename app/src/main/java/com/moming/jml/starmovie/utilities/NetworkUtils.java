package com.moming.jml.starmovie.utilities;


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by jml on 2017/9/22.
 */

public final class NetworkUtils {
    //豆瓣电影api URL
    public static final String MOVIE_API="http://api.douban.com/v2/movie/";
    public static final String IN_THEATERS_URL=MOVIE_API+"in_theaters";//热映
    public static final String MOVIE_SUBJECT_URL=MOVIE_API+"subject/";//单个条目检索

    //the movie db api URL
    public static final String THE_MOVIE_DB_API_KEY="5ff6126cef0585783e7849804fc8a188";
    public static final String THE_MOVIE_DB_API="https://api.themoviedb.org/3/movie/";
    public static final String MOVIE_POP_URL=THE_MOVIE_DB_API+"popular";
    public static final String MOVIE_RATE_URL=THE_MOVIE_DB_API+"top_rated";

    private static final String format = "json";

    //豆瓣电影 api 参数
    final static String START_PARAM="start";
    final static String COUNT_PARAM="count";

    //the movie db api 参数
    final static String APIKEY_PARAM="api_key";
    final static String LANG_PARAM="language";
    final static String PAGE_PARAM="page";

    public static URL buildMoviePopUrlFromMovieDb(){
        Uri buildUri = Uri.parse(MOVIE_POP_URL).buildUpon()
                .appendQueryParameter(APIKEY_PARAM,THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(LANG_PARAM,"zh")
                .appendQueryParameter(PAGE_PARAM,"1")
                .build();
        URL url = null;
        try{
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildMovieTopUrlFromMovieDb(){
        Uri buildUri = Uri.parse(MOVIE_RATE_URL).buildUpon()
                .appendQueryParameter(APIKEY_PARAM,THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(LANG_PARAM,"zh")
                .appendQueryParameter(PAGE_PARAM,"1")
                .build();
        URL url = null;
        try{
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildMovieItemUrlFromMovieDbById(String id){
        String MOVIE_ITEM_URL=THE_MOVIE_DB_API+id;
        Uri buildUri = Uri.parse(MOVIE_ITEM_URL).buildUpon()
                .appendQueryParameter(APIKEY_PARAM,THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(LANG_PARAM,"zh").build();
        URL url = null;
        try{
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }


    public static URL buildUrl(){
        Uri buildUri = Uri.parse(IN_THEATERS_URL).buildUpon()
                .appendQueryParameter(START_PARAM,"0")
                .appendQueryParameter(COUNT_PARAM,"20")
                .build();
        URL url = null;
        try{
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlbyId(String id){
        String movieUrl=MOVIE_SUBJECT_URL+id;
        Uri buildUri = Uri.parse(movieUrl).buildUpon().build();
        URL url = null;
        try{
            url= new URL((buildUri.toString()));
            Log.v("url",url.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;

    }


    public static String getResponseFromHttpUrl(URL url)throws IOException{
        HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput =scanner.hasNext();
            if (hasInput){
                return  scanner.next();
            }else {
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }


}
