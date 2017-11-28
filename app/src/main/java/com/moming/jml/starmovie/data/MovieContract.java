package com.moming.jml.starmovie.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by admin on 2017/11/20.
 */

public class MovieContract {

    //content地址
    //content://com.moming.jml.starmovie/

    //包名
    public static final String CONTENT_AUTHORITY =
            "com.moming.jml.starmovie";

    //基础 content 地址
    public static final Uri BASE_CONTENT_URI
            =Uri.parse("content://"+CONTENT_AUTHORITY);

    //电影查询路径

    public static final String PATH_MOVIE = "movie";

    //定义电影表格
    public static final class MovieEntry implements BaseColumns{

        //content movie uri
        public static final Uri CONTENT_URI =BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();
        //table name
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id"; //电影id

        public static final String COLUMN_MOVIE_TITLE = "title"; //标题

        public static final String COLUMN_MOVIE_IMAGE = "image"; //背景图

        public static final String COLUMN_MOVIE_POSTER = "poster"; //海报图


        public static final String COLUMN_MOVIE_RELEASE = "release"; //发行日期

        public static final String COLUMN_MOVIE_RUNTIME = "runtime"; //时长


        public static final String COLUMN_MOVIE_TRAILER = "trailer";//预告

        public static final String COLUMN_MOVIE_COMMEMT = "comment"; //评论

        public static final String COLUMN_MOVIE_VOTE = "vote"; //评分

        public static final String COLUMN_MOVIE_OVERVIEW = "overview"; //介绍





        public static final String COLUMN_USER_COLLECTION = "collection"; //收藏标记

        public static final String COLUMN_MOVIE_POP = "pop"; //热门标记

        public static final String COLUMN_MOVIE_TOP = "top"; //评分标记

        public static Uri buildMovieUriWithId(String id){
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }

    }

}
