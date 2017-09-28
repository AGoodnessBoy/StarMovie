package com.moming.jml.starmovie;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moming.jml.starmovie.entities.NewMovieEntity;
import com.moming.jml.starmovie.utilities.NetworkUtils;
import com.moming.jml.starmovie.utilities.OpenMovieJsonUtilsFromMovieDb;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements IndexMovieAdapter.IndexMovieAdapterOnClickHandler{

    final static String SORT_BY_POP ="1";
    final static String SORT_BY_TOP ="2";
    final static String SORT_DEFAULT="0";

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
        loadMovieData(SORT_DEFAULT);
    }

    private void loadMovieData(String sortBy){
        showMovieData();
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute(sortBy);

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
                loadMovieData(SORT_BY_POP);
                break;
            case R.id.action_sort_by_rating:
                loadMovieData(SORT_BY_TOP);
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

    public class FetchMovieTask extends AsyncTask<String,Void,NewMovieEntity[]>{
        @Override
        protected NewMovieEntity[] doInBackground(String... params) {
            String sortBy = params[0];
            URL finalUrl=null;

            switch (sortBy){
                case SORT_BY_POP:
                    finalUrl=NetworkUtils.buildMoviePopUrlFromMovieDb();
                    break;
                case SORT_BY_TOP:
                    finalUrl=NetworkUtils.buildMovieTopUrlFromMovieDb();
                    break;
                default:
                    finalUrl=NetworkUtils.buildMoviePopUrlFromMovieDb();
            }
            try{
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(finalUrl);
                NewMovieEntity[] movieDbData= OpenMovieJsonUtilsFromMovieDb
                        .getMovieListFromMovieDb(MainActivity.this,jsonMovieResponse);
                //MovieEntity[] simpleData= OpenMovieJsonUtils.getMovieListFromJson(MainActivity.this,jsonMovieResponse);
                return movieDbData;
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
        protected void onPostExecute(NewMovieEntity[] movieEntities) {
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
