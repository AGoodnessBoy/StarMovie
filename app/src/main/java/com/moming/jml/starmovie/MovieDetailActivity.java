package com.moming.jml.starmovie;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moming.jml.starmovie.data.MovieContract;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private TextView mMovieNameTextView;
    private TextView mMovieSummaryTextView;
    private ImageView mMoviePosterImageView;
    private TextView mMovieRuntimeTextView;
    private TextView mMoreMessageTextView;
    private TextView mMovieDateTextView;
    private TextView mErrorMsgTextView;
    private FrameLayout mMovieDetailFrameLayout;
    private ProgressBar mMovieLoadBar;

    private Cursor mCursor;

    private static final int ID_DETAIL_LOADER = 109;

    public static final String[] MOVIE_DETAIL_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID, //id
            MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE, //大图
            MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,//介绍
            MovieContract.MovieEntry.COLUMN_MOVIE_COMMEMT, //评论
            MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE,//时间
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,//标题
            MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME,//运行时间
            MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER//预告
    };
    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_IMAGE =1;
    public static final int INDEX_MOVIE_OVERVIEW =2;
    public static final int INDEX_MOVIE_COMMEMT =3;
    public static final int INDEX_MOVIE_RELEASE =4;
    public static final int INDEX_MOVIE_TITLE =5;
    public static final int INDEX_MOVIE_RUNTIME =6;
    public static final int INDEX_MOVIE_TRAILER =7;

    private Uri mUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mMovieNameTextView=(TextView)findViewById(R.id.tv_movie_name_in_detail);
        mMoviePosterImageView=(ImageView)findViewById(R.id.iv_movie_banner) ;

        mMovieSummaryTextView=(TextView)findViewById(R.id.tv_movie_summary_in_detail);


        mMovieRuntimeTextView=(TextView)findViewById(R.id.tv_movie_runtime_in_detail);

        mMoreMessageTextView=(TextView)findViewById(R.id.tv_more_message);
        mMovieDateTextView=(TextView)findViewById(R.id.tv_movie_date_in_detail);
        mErrorMsgTextView = (TextView)findViewById(R.id.tv_error_message_in_detail) ;
        mMovieDetailFrameLayout=(FrameLayout)findViewById(R.id.layout_of_movie_detail);
        mMovieLoadBar=(ProgressBar)findViewById(R.id.pb_loading_indicator_in_detail);


        Intent intent =getIntent();
        String sMovieId =intent.getStringExtra("movie_id");
        mUri = MovieContract.MovieEntry.buildMovieUriWithId(sMovieId);
        if (mUri == null) throw new NullPointerException("URI for MovieDetailActivity cannot be null");;
        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER,null,this);

        Log.v("id-intent",sMovieId);
        mMoreMessageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMovie(mMovieNameTextView.getText().toString());
            }
        });

        ViewOutlineProvider mProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                view.setClipToOutline(true);
                outline.setRoundRect(16,16,view.getWidth(),view.getHeight(),10);
            }
        };
        mMovieDetailFrameLayout.setOutlineProvider(mProvider);

    }

    private void searchMovie(String name){
        Uri search = Uri.parse("https://baidu.com/s").buildUpon()
                .appendQueryParameter("wd",name).build();

        Intent searchIntent = new Intent(Intent.ACTION_VIEW,search);
        if (searchIntent.resolveActivity(getPackageManager())!=null){
            startActivity(searchIntent);
        }

    }

    private void showErrorMsg(){
        mErrorMsgTextView.setVisibility(View.VISIBLE);
        mMovieDetailFrameLayout.setVisibility(View.INVISIBLE);
    }

    private void showMovieDetail(){
        mErrorMsgTextView.setVisibility(View.INVISIBLE);
        mMovieDetailFrameLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case ID_DETAIL_LOADER:
                return new CursorLoader(this,
                        mUri,MOVIE_DETAIL_PROJECTION,null,null,null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }

        mMovieNameTextView.setText(data.getString(INDEX_MOVIE_TITLE));
        String img_base_url="http://image.tmdb.org/t/p/w300/";
        String movie_img_url=img_base_url+data.getString(INDEX_MOVIE_IMAGE);
        Picasso.with(MovieDetailActivity.this).load(movie_img_url).error(R.drawable.default_img)
                .into(mMoviePosterImageView);
        mMovieDateTextView.setText(data.getString(INDEX_MOVIE_RELEASE));
        mMovieRuntimeTextView.setText(data.getString(INDEX_MOVIE_RUNTIME));
        mMovieSummaryTextView.setText(data.getString(INDEX_MOVIE_OVERVIEW));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
