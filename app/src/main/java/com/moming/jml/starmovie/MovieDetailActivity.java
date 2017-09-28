package com.moming.jml.starmovie;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.moming.jml.starmovie.entities.NewMovieEntity;
import com.moming.jml.starmovie.utilities.NetworkUtils;
import com.moming.jml.starmovie.utilities.OpenMovieJsonUtilsFromMovieDb;
import com.squareup.picasso.Picasso;

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

    public class FetchMovieDetailTask extends AsyncTask<String,Void,NewMovieEntity>{

        @Override
        protected NewMovieEntity doInBackground(String... params) {

            if (params==null){
                return null;
            }
            String movieId = params[0];
            Log.v("id-ask",movieId);
            Context context =MovieDetailActivity.this;

            URL url = NetworkUtils.buildMovieItemUrlFromMovieDbById(movieId);
            try {
                String movieItemJsonReponse = NetworkUtils.getResponseFromHttpUrl(url);
                NewMovieEntity movieEntity = OpenMovieJsonUtilsFromMovieDb
                        .getMovieItemFromMovieDb(context,movieItemJsonReponse);
               // MovieEntity movieEntity = OpenMovieJsonUtils.getMovieItemFromJson(context,movieItemJsonReponse);
                Log.v("name",movieEntity.getTitle());
                return movieEntity;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(NewMovieEntity movieEntity) {

            if (movieEntity==null){
                //显示错误信息
            }
            String img_base_url="http://image.tmdb.org/t/p/w300/";
            String movie_img_url=img_base_url+movieEntity.getImg_path();
            Log.v("image_url",movie_img_url);
            mMovieNameTextView.setText(movieEntity.getTitle());
            mMovieSummaryTextView.setText(movieEntity.getOverview());
            mMovieRatingTextView.setText(movieEntity.getVote());
           // mMovieCastsTextView.setText(movieEntity.getCompany());
            Picasso.with(MovieDetailActivity.this).load(movie_img_url).into(mMoviePosterImageView);


        }
    }
}
