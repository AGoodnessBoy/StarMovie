package com.moming.jml.starmovie.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.moming.jml.starmovie.R;
import com.moming.jml.starmovie.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by jml on 2017/11/26.
 */

public class MovieDetailFragment extends Fragment {

    private TextView mMovieNameTextView;
    private TextView mMovieSummaryTextView;
    private ImageView mMoviePosterImageView;
    private TextView mMovieRuntimeTextView;
    private TextView mMoreMessageTextView;
    private TextView mMovieDateTextView;
    private FrameLayout mMovieDetailFrameLayout;

    private TextView mPadDefault;
    private ScrollView mScrollView;

    private Context mContent;
    private Activity mActivity;

    private static final int ID_DETAIL_LOADER = 109;

    private LoaderManager.LoaderCallbacks<Cursor> detailLoader;

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

    public MovieDetailFragment(){

    }



    public void freshData(String id){
        mUri = MovieContract.MovieEntry.buildMovieUriWithId(id);

        Log.v("uri",mUri.toString());

        if (mUri == null) throw new NullPointerException("URI for MovieDetailActivity cannot be null");
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.restartLoader(ID_DETAIL_LOADER,null,detailLoader);
        mPadDefault.setVisibility(View.INVISIBLE);
        mScrollView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_part,container,false);




        mMovieNameTextView=(TextView)view.findViewById(R.id.tv_movie_name_in_detail);
        mMoviePosterImageView=(ImageView)view.findViewById(R.id.iv_movie_banner) ;
        mMovieSummaryTextView=(TextView)view.findViewById(R.id.tv_movie_summary_in_detail);
        mMovieRuntimeTextView=(TextView)view.findViewById(R.id.tv_movie_runtime_in_detail);
        mMoreMessageTextView=(TextView)view.findViewById(R.id.tv_more_message);
        mMovieDateTextView=(TextView)view.findViewById(R.id.tv_movie_date_in_detail);
        mMovieDetailFrameLayout=(FrameLayout)view.findViewById(R.id.layout_of_movie_detail);

        mPadDefault =(TextView)view.findViewById(R.id.tv_pad_default);
        mScrollView = (ScrollView) view.findViewById(R.id.sv_movie_detail);

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
                outline.setRoundRect(0,0,view.getWidth(),view.getHeight(),2);
            }
        };
        mMovieDetailFrameLayout.setOutlineProvider(mProvider);

        detailLoader = new LoaderManager.LoaderCallbacks<Cursor>() {


            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                switch (id){
                    case ID_DETAIL_LOADER:
                        return new CursorLoader(getContext(),
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
                String MovieId = data.getString(INDEX_MOVIE_ID);


                mMovieNameTextView.setText(data.getString(INDEX_MOVIE_TITLE));
                String img_base_url="http://image.tmdb.org/t/p/w300/";
                String movie_img_url=img_base_url+data.getString(INDEX_MOVIE_IMAGE);
                Picasso.with(getContext()).load(movie_img_url).error(R.drawable.default_img)
                        .into(mMoviePosterImageView);
                mMovieDateTextView.setText(data.getString(INDEX_MOVIE_RELEASE));
                mMovieRuntimeTextView.setText(data.getString(INDEX_MOVIE_RUNTIME));
                mMovieSummaryTextView.setText(data.getString(INDEX_MOVIE_OVERVIEW));

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
        if (mUri == null){
            mPadDefault.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.INVISIBLE);
        }






        return view;
    }

    private void searchMovie(String name){
        Uri search = Uri.parse("https://baidu.com/s").buildUpon()
                .appendQueryParameter("wd",name).build();

        Intent searchIntent = new Intent(Intent.ACTION_VIEW,search);
        if (searchIntent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivity(searchIntent);
        }

    }



}
