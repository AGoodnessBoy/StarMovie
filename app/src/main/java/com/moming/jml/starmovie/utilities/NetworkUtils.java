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
    public static final String MOVIE_API="http://api.douban.com/v2/movie/";
    public static final String IN_THEATERS_URL=MOVIE_API+"in_theaters";//热映
    public static final String SEARCH_URL=MOVIE_API+"search";//搜索查询
    public static final String MOVIE_SUBJECT_URL=MOVIE_API+"subject/";//单个条目检索

    private static final String format = "json";

    final static  String QUERY_PARAM="q";
    final static  String TAG_PARAM="tag";
    final static  String START_PARAM="start";
    final static  String COUNT_PARAM="count";


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
