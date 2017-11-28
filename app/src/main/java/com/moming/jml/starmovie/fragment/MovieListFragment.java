package com.moming.jml.starmovie.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.moming.jml.starmovie.IndexMovieAdapter;
import com.moming.jml.starmovie.MainActivity;
import com.moming.jml.starmovie.MovieDetailActivity;
import com.moming.jml.starmovie.R;
import com.moming.jml.starmovie.data.MovieContract;
import com.moming.jml.starmovie.sync.MovieSyncUtils;

/**
 * Created by jml on 2017/11/26
 */

public class MovieListFragment extends Fragment implements
        IndexMovieAdapter.IndexMovieAdapterOnClickHandler{

    private final String TAG = MainActivity.class.getSimpleName();
    public static final int ID_MOVIE_LOADER = 32;

    final static String SORT_BY_POP ="1";
    final static String SORT_BY_TOP ="2";
    final static String SORT_DEFAULT="0";
    final static String SORT_KET = "sortBy";
    final static String USER_COLLECTION="user_collection";

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




    private boolean mTowPan;
    protected RecyclerView mRecyclerView;
    private IndexMovieAdapter theMovieAdaper;
    protected int mPosition = RecyclerView.NO_POSITION;



    public LoaderManager.LoaderCallbacks<Cursor> callbacks;

    public MovieListFragment(){

    }
    private void replaceFragment(String id){
        MovieDetailFragment md = (MovieDetailFragment) getFragmentManager().findFragmentById(R.id.movie_detail_fragment);
        md.freshData(id);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                switch (id){
                    case ID_MOVIE_LOADER:
                        Context context = getContext();
                        Uri mainMovieQueryUri =
                                MovieContract.MovieEntry.CONTENT_URI;

                        String selection =searchSelection(args.getString(SORT_KET));

                        String sortOrder = MovieContract.MovieEntry.COLUMN_MOVIE_VOTE+ " ASC";

                        return new CursorLoader(context,mainMovieQueryUri,
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
        };
        if(getActivity().findViewById(R.id.movie_detail_layout)!=null){
            mTowPan =true;
            Log.v(TAG,"mTowPan=true");
        }else {
            mTowPan =false;
            Log.v(TAG,"mTowPan=false");
        }
        Bundle bundle = new Bundle();
        bundle.putString(SORT_KET,SORT_DEFAULT);
        getActivity().getSupportLoaderManager().initLoader(ID_MOVIE_LOADER,bundle,callbacks);
        MovieSyncUtils.initialize(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_part,container,false);
        //mLoadingIndicator =(ProgressBar)view.findViewById(R.id.pb_loading_indicator);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv_movie_list);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        theMovieAdaper= new IndexMovieAdapter(getContext(),this);
        mRecyclerView.setAdapter(theMovieAdaper);

        showLoading();
        return view;
    }


    //失效状态
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int theSelectedItemId=item.getItemId();
        Bundle bundle = new Bundle();
        switch (theSelectedItemId){
            case R.id.action_sort_by_popular:
                Log.v("menu","pop");
                bundle.putString(SORT_KET,SORT_BY_POP);
                getActivity().getSupportLoaderManager().restartLoader(
                        ID_MOVIE_LOADER,bundle,callbacks
                );
                bundle.clear();
                break;
            case R.id.action_sort_by_rating:
                Log.v("menu","top");
                bundle.putString(SORT_KET,SORT_BY_TOP);
                getActivity().getSupportLoaderManager().restartLoader(
                        ID_MOVIE_LOADER,bundle,callbacks
                );
                bundle.clear();
                break;
            case R.id.action_user_collection:
                bundle.putString(SORT_KET,USER_COLLECTION);
                getActivity().getSupportLoaderManager().restartLoader(
                        ID_MOVIE_LOADER,bundle,callbacks
                );
                bundle.clear();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMovieData(){
        //mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showLoading(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        //mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private String searchSelection(String args){
        switch (args){
            case SORT_BY_POP:
                return MovieContract.MovieEntry.COLUMN_MOVIE_POP+" = 1";
            case SORT_BY_TOP:
                return MovieContract.MovieEntry.COLUMN_MOVIE_TOP+" = 1";
            case USER_COLLECTION:
                return MovieContract.MovieEntry.COLUMN_USER_COLLECTION+" = 1";
            default:
                return MovieContract.MovieEntry.COLUMN_MOVIE_POP+" = 1";
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(String movieId) {
        Log.v(TAG,movieId);
        if (mTowPan){
            Log.v(TAG,"mTowPan=true");
            replaceFragment(movieId);

        }else {
            Log.v(TAG,"mTowPan=false");
            Intent showMovieDetail = new Intent(getContext(),MovieDetailActivity.class);
            showMovieDetail.putExtra("movie_id",movieId);
            startActivity(showMovieDetail);
        }



    }
}
