package com.moming.jml.starmovie;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moming.jml.starmovie.entities.MovieEntity;
import com.moming.jml.starmovie.utilities.NetworkUtils;
import com.moming.jml.starmovie.utilities.OpenMovieJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements IndexMovieAdapter.IndexMovieAdapterOnClickHandler{
    private RecyclerView mRecyclerView;
    private IndexMovieAdapter theMovieAdaper;
    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingIndicator =(ProgressBar)findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay=(TextView)findViewById(R.id.tv_error_message_display);
        mRecyclerView=(RecyclerView)findViewById(R.id.rv_movie_list);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        theMovieAdaper= new IndexMovieAdapter(this);
        mRecyclerView.setAdapter(theMovieAdaper);
        loadMovieData();
    }

    private void loadMovieData(){
        showMovieData();
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute();

    }
    private void showMovieData(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

    }
    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int theSelectedItemId=item.getItemId();
        switch (theSelectedItemId){
            case R.id.action_sort_by_popular:

                break;
            case R.id.action_sort_by_rating:

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

    public class FetchMovieTask extends AsyncTask<Void,Void,MovieEntity[]>{
        @Override
        protected MovieEntity[] doInBackground(Void... params) {
            URL movieRequestUrl = NetworkUtils.buildUrl();
            try{
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);
                MovieEntity[] simpleData= OpenMovieJsonUtils.getMovieListFromJson(MainActivity.this,jsonMovieResponse);
                return simpleData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(MovieEntity[] movieEntities) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieEntities!=null){
                showMovieData();
                theMovieAdaper.setMovieData(movieEntities);
            }else {
                showErrorMessage();
            }
        }
    }
}
