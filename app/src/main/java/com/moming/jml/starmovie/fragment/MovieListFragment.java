package com.moming.jml.starmovie.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moming.jml.starmovie.IndexMovieAdapter;
import com.moming.jml.starmovie.MovieDetailActivity;
import com.moming.jml.starmovie.R;
import com.moming.jml.starmovie.data.MovieContract;
import com.moming.jml.starmovie.sync.MovieSyncUtils;

/**
 * Created by jml on 2017/11/26
 */

public class MovieListFragment extends Fragment implements
        IndexMovieAdapter.IndexMovieAdapterOnClickHandler{

    private final String TAG = MovieListFragment.class.getSimpleName();
    public static final int ID_MOVIE_LOADER = 32;

    public int lastOffset;
    public int lastPosition;

    public final static String LASTOFFSET = "last_offset";

    public final static String LASTPOSITION = "last_position";

    final static String SORT_BY_POP ="pop";
    final static String SORT_BY_TOP ="top";
    final static String SORT_KET = "sortBy";
    final static String USER_COLLECTION="col";

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
    public RecyclerView mRecyclerView;
    private IndexMovieAdapter theMovieAdaper;
    protected int mPosition = RecyclerView.NO_POSITION;

    private ProgressBar mLoadingIndicator;
    private TextView mNullDataTextView;

    private SharedPreferences.OnSharedPreferenceChangeListener mListener=
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    Log.v("pref-fragment",key);

                    if (key.equals(getString(R.string.pref_sort_key))){

                        Log.v("pref-fragment",key);

                        Bundle bundle = new Bundle();
                        bundle.putString(SORT_KET,getSortFromPreference(sharedPreferences));

                        getActivity().getSupportLoaderManager().restartLoader(
                                ID_MOVIE_LOADER,bundle,callbacks);
                        bundle.clear();

                    }
                }
            };



    public LoaderManager.LoaderCallbacks<Cursor> callbacks;

    public MovieListFragment(){

    }
    private void replaceFragment(String id){
        MovieDetailFragment md = (MovieDetailFragment) getFragmentManager().findFragmentById(R.id.movie_detail_fragment);
        md.freshData(id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"create");
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(mListener);
        Log.v(TAG,PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("sort",""));





    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_part,container,false);
        mLoadingIndicator =(ProgressBar)view.findViewById(R.id.pb_loading);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv_movie_list);
        mNullDataTextView = (TextView)view.findViewById(R.id.tv_null_data);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        theMovieAdaper= new IndexMovieAdapter(getContext(),this);
        mRecyclerView.setAdapter(theMovieAdaper);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getLayoutManager()!=null){
                    getPositionAndOffset();
                }
            }
        });

        callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                switch (id){
                    case ID_MOVIE_LOADER:
                        showLoading();
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
                Log.v("data",Integer.toString(data.getCount()));
                if (data.getCount()!=0 ) {
                    showMovieData();
                }else {
                    showNullData();
                }
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

        if (savedInstanceState != null) {
            Log.v("savedInstanceState","not null");
            Log.v("savedInstanceState",Integer.toString(savedInstanceState.getInt(LASTOFFSET)));
            Log.v("savedInstanceState",Integer.toString(savedInstanceState.getInt(LASTPOSITION)));
            Bundle bundle = new Bundle();
            SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            bundle.putString(SORT_KET,getSortFromPreference(mSharedPreferences));
            getActivity().getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER,bundle,callbacks);
            bundle.clear();

            //mRecyclerView.scrollToPosition(16);

            //mRecyclerView.scrollTo(12,0);
           mRecyclerView.getLayoutManager().scrollToPosition(12);
            ((StaggeredGridLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(10,0);
            //((StaggeredGridLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(
              //      savedInstanceState.getInt(LASTPOSITION), savedInstanceState.getInt(LASTOFFSET));

        }else{
            Bundle bundle = new Bundle();
            SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            bundle.putString(SORT_KET,getSortFromPreference(mSharedPreferences));
            getActivity().getSupportLoaderManager().initLoader(ID_MOVIE_LOADER,bundle,callbacks);
            MovieSyncUtils.initialize(getContext());
            bundle.clear();
        }

        return view;
    }


    private void getPositionAndOffset(){
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
        View topView = layoutManager.getChildAt(0);

        if (topView != null){
            lastOffset = topView.getTop();
            lastPosition = layoutManager.getPosition(topView);

            Log.v("OFF",Integer.toString(lastOffset));
            Log.v("OFF",Integer.toString(lastPosition));
        }
    }


    @Override
    public void onResume() {
        Log.v(TAG,"resume");

        super.onResume();

    }


    @Override
    public void onPause() {
        Log.v(TAG,"pause");

        super.onPause();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG,"start");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG,"stop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"destory");
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(mListener);
    }

    private String getSortFromPreference(SharedPreferences sharedPreferences){
        int sort_key = R.string.pref_sort_key;
        return sharedPreferences.getString(getString(sort_key),"");
    }







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
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mNullDataTextView.setVisibility(View.INVISIBLE);
    }
    private void showLoading(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mNullDataTextView.setVisibility(View.INVISIBLE);
    }
    private void showNullData(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mNullDataTextView.setVisibility(View.VISIBLE);
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
        outState.putInt(LASTOFFSET,lastOffset);
        outState.putInt(LASTPOSITION,lastPosition);

        Log.v(TAG,Integer.toString(lastOffset));
        Log.v(TAG,Integer.toString(lastPosition));

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
