package com.moming.jml.starmovie;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.moming.jml.starmovie.entities.MovieEntity;
import com.moming.jml.starmovie.utilities.NetworkUtils;
import com.moming.jml.starmovie.utilities.OpenMovieJsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView mMovieNameTextView;
    private TextView mMovieRatingTextView;
    private TextView mMovieSummaryTextView;
    private ImageView mMoviePosterImageView;
    private TextView mMovieCastsTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mMovieNameTextView=(TextView)findViewById(R.id.tv_movie_name_in_detail);
        mMoviePosterImageView=(ImageView)findViewById(R.id.iv_movie_banner) ;
        mMovieRatingTextView=(TextView)findViewById(R.id.tv_movie_rating_in_detail);
        mMovieSummaryTextView=(TextView)findViewById(R.id.tv_movie_summary_in_detail);
        mMovieCastsTextView = (TextView)findViewById(R.id.tv_movie_actors) ;

        Intent intent =getIntent();
        String sMovieId =intent.getStringExtra("movie_id");
        Log.v("id-intent",sMovieId);
        FetchMovieDetailTask fetchMovieDetailTask = new FetchMovieDetailTask();
        fetchMovieDetailTask.execute(sMovieId);
    }

    public class FetchMovieDetailTask extends AsyncTask<String,Void,MovieEntity>{

        @Override
        protected MovieEntity doInBackground(String... params) {

            if (params==null){
                return null;
            }
            String movieId = params[0];
            Log.v("id-ask",movieId);
            Context context =MovieDetailActivity.this;

            URL url = NetworkUtils.buildUrlbyId(movieId);
            try {
                String movieItemJsonReponse = NetworkUtils.getResponseFromHttpUrl(url);

                MovieEntity movieEntity = OpenMovieJsonUtils.getMovieItemFromJson(context,movieItemJsonReponse);
                Log.v("name",movieEntity.getMovie_name());
                return movieEntity;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieEntity movieEntity) {

            if (movieEntity==null){
                //显示错误信息
            }
            mMovieNameTextView.setText(movieEntity.getMovie_name());
            mMovieSummaryTextView.setText(movieEntity.getMovie_summary());
            mMovieRatingTextView.setText(movieEntity.getMovie_rating());

            String actors ="";
            JSONArray actorJson=movieEntity.getMovie_casts();
            if (actorJson==null){
                mMovieCastsTextView.setText("暂无信息");
            }
            for (int i=0;i<actorJson.length();i++){
                try {
                    JSONObject acttemp=actorJson.getJSONObject(i);
                    actors=actors+acttemp.getString("name")+",";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mMovieCastsTextView.setText(actors);
            Picasso.with(MovieDetailActivity.this).load(movieEntity.getMovie_img_url()).into(mMoviePosterImageView);


        }
    }
}
