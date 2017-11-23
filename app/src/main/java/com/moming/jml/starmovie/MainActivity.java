package com.moming.jml.starmovie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moming.jml.starmovie.data.MovieContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, IndexMovieAdapter.IndexMovieAdapterOnClickHandler{


    private final String TAG = MainActivity.class.getSimpleName();
    private static final int ID_MOVIE_LOADER = 32;

    final static String SORT_BY_POP ="1";
    final static String SORT_BY_TOP ="2";
    final static String SORT_DEFAULT="0";
    final static String SORT_KET = "sortBy";

    public static final String[] MAIN_MOVIE_PROJECTION ={
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
            MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTE
    };

    public static final int INDEX_MOVIE_ID=0;
    public static final int INDEX_MOVIE_POSTER=1;
    public static final int INDEX_MOVIE_RELEASE=2;
    public static final int INDEX_MOVIE_TITLE=3;
    public static final int INDEX_MOVIE_VOTE=4;





    private RecyclerView mRecyclerView;
    private IndexMovieAdapter theMovieAdaper;
    private int mPosition = RecyclerView.NO_POSITION;
    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R
                .layout.activity_main);
        mLoadingIndicator =(ProgressBar)findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay=(TextView)findViewById(R.id.tv_error_message_display);
        mRecyclerView=(RecyclerView)findViewById(R.id.rv_movie_list);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        theMovieAdaper= new IndexMovieAdapter(this,this);
        mRecyclerView.setAdapter(theMovieAdaper);
        showLoading();
        //loadMovieData(SORT_DEFAULT);

        Bundle bundle = new Bundle();
        bundle.putString(SORT_KET,SORT_DEFAULT);
        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER,bundle,this);

    }


    private void showMovieData(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

    }
    private void showLoading(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int theSelectedItemId=item.getItemId();
        Bundle bundle = new Bundle();
        switch (theSelectedItemId){
            case R.id.action_sort_by_popular:
                bundle.putString(SORT_KET,SORT_BY_POP);
                getSupportLoaderManager().restartLoader(
                        ID_MOVIE_LOADER,bundle,this
                );
                bundle.clear();
                break;
            case R.id.action_sort_by_rating:
                bundle.putString(SORT_KET,SORT_BY_TOP);
                getSupportLoaderManager().restartLoader(
                        ID_MOVIE_LOADER,bundle,this
                );
                bundle.clear();
                break;
            default:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(String movieId) {
        Context context= this;
        Intent showMovieDetail = new Intent(context,MovieDetailActivity.class);
        showMovieDetail.putExtra("movie_id",movieId);

        startActivity(showMovieDetail);

    }

    private String searchSelection(String args){
        switch (args){
            case SORT_BY_POP:
                return MovieContract.MovieEntry.COLUMN_MOVIE_POP+" = 1";
            case SORT_BY_TOP:
                return MovieContract.MovieEntry.COLUMN_MOVIE_TOP+" = 1";
            case SORT_DEFAULT:
                return MovieContract.MovieEntry.COLUMN_MOVIE_POP+" = 1";
            default:
                return MovieContract.MovieEntry.COLUMN_MOVIE_POP+" = 1";
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {



        switch (id){
            case ID_MOVIE_LOADER:
                Uri mainMovieQueryUri =
                        MovieContract.MovieEntry.CONTENT_URI;

                String selection =searchSelection(args.getString(SORT_KET));

                String sortOrder =
                        MovieContract.MovieEntry.COLUMN_MOVIE_VOTE +" ASC";

                return new CursorLoader(this,mainMovieQueryUri,
                        MAIN_MOVIE_PROJECTION,selection,null,sortOrder);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);



        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        theMovieAdaper.swapCursor(data);

        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount()!=0 ) showMovieData();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        theMovieAdaper.swapCursor(null);

    }
}
