package com.moming.jml.starmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 2017/11/20.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "star_movie.db";

    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME +" ("+
                        MovieContract.MovieEntry._ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID+
                        " INTEGER NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_TITLE+
                        " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE+
                        " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_POSTER+
                        " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_STATUS+
                        " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE+
                        " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_TYPE+
                        " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_REVENUE+
                        " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER+
                        " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_COMMEMT+
                        " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME+
                        " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_VOTE+
                        " TEXT NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_POP+
                        " INTEGER, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_TOP+
                        " INTEGER, "+
                        MovieContract.MovieEntry.COLUMN_USER_COLLECTION+
                        " INTEGER, "+
                        " UNIQUE ("+ MovieContract.MovieEntry.COLUMN_MOVIE_ID+
                        ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "
            + MovieContract.MovieEntry.TABLE_NAME);

        onCreate(db);
    }
}