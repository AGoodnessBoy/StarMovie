package com.moming.jml.starmovie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moming.jml.starmovie.entities.NewMovieEntity;
import com.moming.jml.starmovie.utilities.NetworkUtils;
import com.moming.jml.starmovie.utilities.OpenMovieJsonUtilsFromMovieDb;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView mMovieNameTextView;
    private TextView mMovieRatingTextView;
    private TextView mMovieSummaryTextView;
    private ImageView mMoviePosterImageView;
    private TextView mMovieStatusTextView;
    private TextView mMovieRevenuetTextView;
    private TextView mMovieRuntimeTextView;
    private TextView mMovieTypeTextView;
    private TextView mMoreMessageTextView;
    private TextView mMovieDateTextView;
    private TextView mErrorMsgTextView;
    private FrameLayout mMovieDetailFrameLayout;
    private ProgressBar mMovieLoadBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mMovieNameTextView=(TextView)findViewById(R.id.tv_movie_name_in_detail);
        mMoviePosterImageView=(ImageView)findViewById(R.id.iv_movie_banner) ;
        mMovieRatingTextView=(TextView)findViewById(R.id.tv_movie_rating_in_detail);
        mMovieSummaryTextView=(TextView)findViewById(R.id.tv_movie_summary_in_detail);
        mMovieStatusTextView=(TextView)findViewById(R.id.tv_movie_status_in_detail);
        mMovieRevenuetTextView=(TextView)findViewById(R.id.tv_movie_revenue_in_detail);
        mMovieRuntimeTextView=(TextView)findViewById(R.id.tv_movie_runtime_in_detail);
        mMovieTypeTextView=(TextView)findViewById(R.id.tv_movie_type_in_detail);
        mMoreMessageTextView=(TextView)findViewById(R.id.tv_more_message);
        mMovieDateTextView=(TextView)findViewById(R.id.tv_movie_date_in_detail);
        mErrorMsgTextView = (TextView)findViewById(R.id.tv_error_message_in_detail) ;
        mMovieDetailFrameLayout=(FrameLayout)findViewById(R.id.layout_of_movie_detail);
        mMovieLoadBar=(ProgressBar)findViewById(R.id.pb_loading_indicator_in_detail);


        Intent intent =getIntent();
        String sMovieId =intent.getStringExtra("movie_id");
        Log.v("id-intent",sMovieId);
        FetchMovieDetailTask fetchMovieDetailTask = new FetchMovieDetailTask();
        fetchMovieDetailTask.execute(sMovieId);
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


    public class FetchMovieDetailTask extends AsyncTask<String,Void,NewMovieEntity>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMovieDetailFrameLayout.setVisibility(View.INVISIBLE);
            mMovieLoadBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected NewMovieEntity doInBackground(String... params) {

            if (params==null){
                return null;
            }
            String movieId = params[0];
            Context context =MovieDetailActivity.this;

            URL url = NetworkUtils.buildMovieItemUrlFromMovieDbById(movieId);
            try {
                String movieItemJsonReponse = NetworkUtils.getResponseFromHttpUrl(url);
                NewMovieEntity movieEntity = OpenMovieJsonUtilsFromMovieDb
                        .getMovieItemFromMovieDb(context,movieItemJsonReponse);
                return movieEntity;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(NewMovieEntity movieEntity) {
            mMovieLoadBar.setVisibility(View.INVISIBLE);

            if (movieEntity!=null){
                showMovieDetail();
                String img_base_url="http://image.tmdb.org/t/p/w300/";
                String movie_img_url=img_base_url+movieEntity.getImg_path();
                mMovieNameTextView.setText(movieEntity.getTitle());
                if (movieEntity.getOverview()!="null"){
                    mMovieSummaryTextView.setText(movieEntity.getOverview());}
                else {
                    mMovieSummaryTextView.setText("暂无数据");
                }
                mMovieRatingTextView.setText(movieEntity.getVote());
                mMovieRevenuetTextView.setText("$ "+movieEntity.getRevenue());
                if (movieEntity.getRuntime().endsWith("null")||movieEntity.getRuntime().endsWith("0")){
                    mMovieRuntimeTextView.setText("暂无数据");

                }else {
                    mMovieRuntimeTextView.setText(movieEntity.getRuntime()+" 分钟");
                }
                mMovieStatusTextView.setText(movieEntity.getStatus());
                if (movieEntity.getRelease_date()!="null"){
                    mMovieDateTextView.setText(movieEntity.getRelease_date());
                }else {
                    mMovieDateTextView.setText("暂无数据");
                }

                String movieType = "";
                JSONArray array = movieEntity.getType();
                for (int i=0;i<array.length();i++){
                    try {
                        JSONObject type = array.getJSONObject(i);
                        movieType=movieType+type.getString("name")+" ";
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                mMovieTypeTextView.setText(movieType);
                // mMovieCastsTextView.setText(movieEntity.getCompany());
                Picasso.with(MovieDetailActivity.this).load(movie_img_url).error(R.drawable.default_img)
                .into(mMoviePosterImageView);

            }else {
                showErrorMsg();
            }



        }
    }
}
