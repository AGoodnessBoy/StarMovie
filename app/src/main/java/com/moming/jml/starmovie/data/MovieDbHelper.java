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
                        " REAL NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE+
                        " REAL NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_POSTER+
                        " REAL NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_STATUS+
                        " REAL NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE+
                        " REAL NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_TYPE+
                        " REAL NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_REVENUE+
                        " REAL NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER+
                        " REAL NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_COMMEMT+
                        " REAL NOT NULL, "+
                        MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME+
                        " REAL NOT NULL, "+
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
