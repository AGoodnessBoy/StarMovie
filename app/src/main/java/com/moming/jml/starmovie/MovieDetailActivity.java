package com.moming.jml.starmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.moming.jml.starmovie.fragment.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity{





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        String sMovieId = intent.getStringExtra("movie_id");
        MovieDetailFragment md = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(
                R.id.movie_detail_fragment);
        md.freshData(sMovieId);



    }




}
