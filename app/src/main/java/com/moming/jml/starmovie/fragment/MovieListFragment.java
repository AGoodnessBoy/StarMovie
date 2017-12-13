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


    public static final String TAG = MovieListFragment.class.getSimpleName();
    public static final int ID_MOVIE_LOADER = 32;

    public static final int ID_MOVIE_LOADER_POP= 28;
    public static final int ID_MOVIE_LOADER_TOP = 29;
    public static final int ID_MOVIE_LOADER_COL = 30;

    public int lastOffset = RecyclerView.NO_POSITION;
    public int lastPosition = RecyclerView.NO_POSITION;

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

    private ProgressBar mLoadingIndicator;
    private TextView mNullDataTextView;

    private Bundle mBundle;


    //保存列表排序方式
    private SharedPreferences.OnSharedPreferenceChangeListener mListener=
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                    if (key.equals(getString(R.string.pref_sort_key))){

                        getActivity().getSupportLoaderManager().restartLoader(
                                getLoadKeyFromPreference(sharedPreferences),null,callbacks);

                    }
                }
            };


    //Cursor 加载器
    public LoaderManager.LoaderCallbacks<Cursor> callbacks;

    public MovieListFragment(){

    }

    //pad端刷新数据
    private void replaceFragment(String id){
        MovieDetailFragment md = (MovieDetailFragment) getFragmentManager().findFragmentById(R.id.movie_detail_fragment);
        md.freshData(id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.v(TAG,"create");
        //注册设置监听
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(mListener);
        if (savedInstanceState !=null){
            Log.v(TAG,"create save:"+Integer.toString(savedInstanceState.getInt(LASTOFFSET)));
            Log.v(TAG,"create save:"+Integer.toString(savedInstanceState.getInt(LASTPOSITION)));
        }
        Log.v(TAG,PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("sort",""));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG,"activityCreated lastOffset"+Integer.toString(lastOffset));
        Log.v(TAG,"activityCreated lastPosition"+Integer.toString(lastPosition));
        Log.v(TAG,"activityCreated");

        if(getActivity().findViewById(R.id.movie_detail_layout)!=null){
            mTowPan =true;
            Log.v(TAG,"mTowPan=true");
        }else {
            mTowPan =false;
            Log.v(TAG,"mTowPan=false");
        }

        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getActivity().getSupportLoaderManager().
                initLoader(getLoadKeyFromPreference(mSharedPreferences),null,callbacks);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {

        Log.v(TAG,"CreateView");
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

        if (savedInstanceState!=null){
            mBundle = savedInstanceState;

        }else {
            mBundle = null;
        }

        callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {



            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Log.v(TAG,"onCreateLoader");
                switch (id){
                    case ID_MOVIE_LOADER_POP:
                        Log.v(TAG,"Loader id"+Integer.toString(ID_MOVIE_LOADER_POP));
                        showLoading();
                        Uri QueryPopUri =
                                MovieContract.MovieEntry.CONTENT_URI;

                        String selectionPop =searchSelection(SORT_BY_POP);

                        String sortOrderPop = MovieContract.MovieEntry.COLUMN_MOVIE_VOTE+ " ASC";

                        return new CursorLoader(getContext(),QueryPopUri,
                                MAIN_MOVIE_PROJECTION,selectionPop,null,sortOrderPop);
                    case ID_MOVIE_LOADER_TOP:
                        Log.v(TAG,"Loader id"+Integer.toString(ID_MOVIE_LOADER_TOP));
                        showLoading();
                        Uri QueryTopUri =
                                MovieContract.MovieEntry.CONTENT_URI;

                        String selectionTop =searchSelection(SORT_BY_TOP);

                        String sortOrderTop = MovieContract.MovieEntry.COLUMN_MOVIE_VOTE+ " ASC";

                        return new CursorLoader(getContext(),QueryTopUri,
                                MAIN_MOVIE_PROJECTION,selectionTop,null,sortOrderTop);

                    case ID_MOVIE_LOADER_COL:
                        Log.v(TAG,"Loader id"+Integer.toString(ID_MOVIE_LOADER_COL));
                        showLoading();
                        Uri QueryColUri =
                                MovieContract.MovieEntry.CONTENT_URI;

                        String selectionCol =searchSelection(USER_COLLECTION);

                        String sortOrderCol = MovieContract.MovieEntry.COLUMN_MOVIE_VOTE+ " ASC";

                        return new CursorLoader(getContext(),QueryColUri,
                                MAIN_MOVIE_PROJECTION,selectionCol,null,sortOrderCol);
                    default:
                        throw new RuntimeException("Loader Not Implemented: " + id);



                }
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                Log.v(TAG,"onLoadFinished");

                theMovieAdaper.swapCursor(data);
                Log.v(TAG,"Position:"+Integer.toString(lastPosition));
                Log.v(TAG,"Offset:"+Integer.toString(lastOffset));

                if (mBundle == null){
                    lastPosition = 0;
                    mRecyclerView.smoothScrollToPosition(lastPosition);
                }else {
                    lastPosition = mBundle.getInt(LASTPOSITION);
                    lastOffset = mBundle.getInt(LASTOFFSET);
                    ((StaggeredGridLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(
                            lastPosition,lastOffset);
                }

                Log.v(TAG,"Position+1:"+Integer.toString(lastPosition));
                Log.v(TAG,"Offset+1:"+Integer.toString(lastOffset));


                Log.v(TAG,"data: "+Integer.toString(data.getCount()));
                if (data.getCount()!=0){
                    showMovieData();
                }else if (data.getCount()==0){
                    showNullData();
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

                Log.v(TAG,"onLoaderReset");

                theMovieAdaper.swapCursor(null);
            }
        };






        MovieSyncUtils.initialize(getContext());





        return view;
    }


    private void getPositionAndOffset(){
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
        View topView = layoutManager.getChildAt(0);

        if (topView != null){
            lastOffset = topView.getTop();
            lastPosition = layoutManager.getPosition(topView);

            Log.v(TAG,"lastOffset-now:"+Integer.toString(lastOffset));
            Log.v(TAG,"lastPosition-now:"+Integer.toString(lastPosition));
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(TAG,"attach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG,"detach");
    }




    private int getLoadKeyFromPreference(SharedPreferences sharedPreferences){
        int sort_key = R.string.pref_sort_key;

        switch (sharedPreferences.getString(getString(sort_key),"")){
            case "pop":
                return ID_MOVIE_LOADER_POP;
            case "top":
                return ID_MOVIE_LOADER_TOP;
            case "col":
                return ID_MOVIE_LOADER_COL;

            default:
                return ID_MOVIE_LOADER_POP;

        }
    }


    @Override
    public void onDestroyView( ) {
        super.onDestroyView();
        Log.v(TAG,"destroyView");
        Log.v(TAG,"destroyView lastOffset"+Integer.toString(lastOffset));
        Log.v(TAG,"destroyView lastPosition"+Integer.toString(lastPosition));

        Bundle bundle = new Bundle();
        bundle.putInt(LASTPOSITION,lastPosition);
        bundle.putInt(LASTOFFSET,lastOffset);

        if (mBundle==null){
            mBundle = new Bundle();
        }

        onSaveInstanceState(mBundle);
    }


    private void showMovieData(){
        Log.v(TAG,"show data");
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mNullDataTextView.setVisibility(View.INVISIBLE);
    }
    private void showLoading(){
        Log.v(TAG,"show load");
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mNullDataTextView.setVisibility(View.INVISIBLE);
    }
    private void showNullData(){
        Log.v(TAG,"show null");
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

        Log.v(TAG,"onSaveInstanceState");
        outState.putInt(LASTOFFSET,lastOffset);
        outState.putInt(LASTPOSITION,lastPosition);

        Log.v(TAG,Integer.toString(outState.getInt(LASTOFFSET)));
        Log.v(TAG,Integer.toString(outState.getInt(LASTPOSITION)));
        //   mBundle = outState;



    }


    public void resetPosition(){
        mBundle=null;
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
