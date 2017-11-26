package com.moming.jml.starmovie;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.moming.jml.starmovie.fragment.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity{





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        FragmentManager fragmentManager = getSupportFragmentManager();
        MovieDetailFragment detailFragment = new MovieDetailFragment();
        fragmentManager.beginTransaction()
                .add(R.id.movie_detail_fragment,detailFragment)
                .commit();


    }




}
