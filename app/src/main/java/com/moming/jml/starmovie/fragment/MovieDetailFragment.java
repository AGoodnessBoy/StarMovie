package com.moming.jml.starmovie.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Outline;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.moming.jml.starmovie.R;
import com.moming.jml.starmovie.ReviewsAdapter;
import com.moming.jml.starmovie.VideosAdapter;
import com.moming.jml.starmovie.data.MovieContract;
import com.moming.jml.starmovie.entities.ReviewsEntity;
import com.moming.jml.starmovie.entities.VideosEntity;
import com.moming.jml.starmovie.utilities.NetworkUtils;
import com.moming.jml.starmovie.utilities.OpenMovieJsonUtilsFromMovieDb;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

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
    private TextView mMovieRateTextView;
    private Button mCollectionButton;

    private TextView mNoReviewsTextView;
    private TextView mNoVideosTextView;

    private TextView mPadDefault;
    private ScrollView mScrollView;

    private ListView mReviewsList;
    private ListView mVideosList;

    private ReviewsAdapter mReviewsAdapter;
    private VideosAdapter mVideosAdapter;
    private String mMovieId;

    private int mCollection;

    private static final int ID_DETAIL_LOADER = 109;

    public static final String TAG= MovieDetailFragment.class.getSimpleName();

    private LoaderManager.LoaderCallbacks<Cursor> detailLoader;

    public static final String[] MOVIE_DETAIL_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID, //id
            MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE, //大图
            MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,//介绍
            MovieContract.MovieEntry.COLUMN_MOVIE_COMMEMT, //评论
            MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE,//时间
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,//标题
            MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME,//运行时间
            MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER,//预告
            MovieContract.MovieEntry.COLUMN_USER_COLLECTION,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTE

    };
    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_IMAGE =1;
    public static final int INDEX_MOVIE_OVERVIEW =2;
    public static final int INDEX_MOVIE_COMMEMT =3;
    public static final int INDEX_MOVIE_RELEASE =4;
    public static final int INDEX_MOVIE_TITLE =5;
    public static final int INDEX_MOVIE_RUNTIME =6;
    public static final int INDEX_MOVIE_TRAILER =7;
    public static final int INDEX_USER_COLLECTION = 8;
    public static final int INDEX_MOVIE_VOTE = 9;

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
        mNoReviewsTextView =(TextView)view.findViewById(R.id.tv_no_reviews_msg);
        mNoVideosTextView  =(TextView)view.findViewById(R.id.tv_no_videos_msg);
        mCollectionButton = (Button)view.findViewById(R.id.bt_user_collection);
        mMovieRateTextView = (TextView)view.findViewById(R.id.tv_movie_rate_in_detail) ;

        mReviewsList = (ListView)view.findViewById(R.id.listView_reviews);
        mVideosList = (ListView)view.findViewById(R.id.listView_videos);

        mPadDefault =(TextView)view.findViewById(R.id.tv_pad_default);
        mScrollView = (ScrollView) view.findViewById(R.id.sv_movie_detail);

        mCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMovieId!=null){
                    ClickTask clickTask = new ClickTask();
                    clickTask.execute(mMovieId);
                }
            }
        });



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

        //内容加载器
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
                mMovieId = data.getString(INDEX_MOVIE_ID);

                mCollection = data.getInt(INDEX_USER_COLLECTION);

                if (mCollection ==1){
                    mCollectionButton.setText("已收藏");
                }else {
                    mCollectionButton.setText("收藏");
                }

                mMovieRateTextView.setText(data.getString(INDEX_MOVIE_VOTE)+" 分");

                mMovieNameTextView.setText(data.getString(INDEX_MOVIE_TITLE));
                String img_base_url="http://image.tmdb.org/t/p/w300/";
                String movie_img_url=img_base_url+data.getString(INDEX_MOVIE_IMAGE);
                Picasso.with(getContext()).load(movie_img_url).error(R.drawable.default_img)
                        .into(mMoviePosterImageView);
                mMovieDateTextView.setText(data.getString(INDEX_MOVIE_RELEASE));
                mMovieRuntimeTextView.setText(data.getString(INDEX_MOVIE_RUNTIME));

                mMovieSummaryTextView.setText(data.getString(INDEX_MOVIE_OVERVIEW));

                ReviewsTask rs = new ReviewsTask();
                rs.execute(mMovieId);
                VideosTask vs = new VideosTask();
                vs.execute(mMovieId);
                RuntimeTask rts = new RuntimeTask();
                rts.execute(mMovieId);

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
    //百度搜索
    private void searchMovie(String name){
        Uri search = Uri.parse("https://baidu.com/s").buildUpon()
                .appendQueryParameter("wd",name).build();

        Intent searchIntent = new Intent(Intent.ACTION_VIEW,search);
        if (searchIntent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivity(searchIntent);
        }

    }
    //打开Youtube链接
    private void openVideoUrl(String key){
        Uri video = NetworkUtils.buildYouTuBeUrl(key);
        Toast.makeText(getContext(),video.toString(),Toast.LENGTH_SHORT).show();

        Intent videoIntent = new Intent(Intent.ACTION_VIEW,video);

        if (videoIntent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivity(videoIntent);
        }

    }
    //收藏Task
    class ClickTask extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... strings) {

            String[] projection = {
                    MovieContract.MovieEntry.COLUMN_USER_COLLECTION
            };
            Cursor cursor = getContext().getContentResolver().query(
                    mUri,projection,null,null,null);
            cursor.moveToFirst();
            int collection = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_COLLECTION));
            cursor.close();

            return collection;
        }

        @Override
        protected void onPostExecute(Integer integer) {

            if (Integer.toString(integer).equals("1")){
                mCollectionButton.setText("收藏");
                ContentValues contentValues= new ContentValues();
                contentValues.put(MovieContract.MovieEntry.COLUMN_USER_COLLECTION,0);
                getContext().getContentResolver().update(mUri,contentValues,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID+" = "+mMovieId,null);

                Toast.makeText(getContext(),"已取消收藏",Toast.LENGTH_SHORT).show();
            }else {
                mCollectionButton.setText("已收藏");
                ContentValues contentValues= new ContentValues();
                contentValues.put(MovieContract.MovieEntry.COLUMN_USER_COLLECTION,1);
                getContext().getContentResolver().update(mUri,contentValues,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID+" = "+mMovieId,null);


                Toast.makeText(getContext(),"已收藏电影",Toast.LENGTH_SHORT).show();
            }


        }
    }
    //预告加载Task
    class VideosTask extends AsyncTask<String,Void,VideosEntity[]>{

        @Override
        protected VideosEntity[] doInBackground(String... params) {
            String id = params[0];
            VideosEntity[] ves;
            try {
                ves =OpenMovieJsonUtilsFromMovieDb.getMovieVideoJsonFromMD(id);

                Log.v(TAG,Integer.toString(ves.length));
                for (int i=0;i<ves.length;i++){
                    Log.v(TAG,ves[i].getInfo());
                    Log.v(TAG,ves[i].getVideo_name());
                }

                return ves;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(VideosEntity[] videosEntities) {
            if (videosEntities[0].getInfo()!="success"||videosEntities==null){
                mNoVideosTextView.setVisibility(View.VISIBLE);
                mVideosList.setVisibility(View.INVISIBLE);
            }else {

                if (getContext()!=null){
                    mNoVideosTextView.setVisibility(View.INVISIBLE);
                    mVideosList.setVisibility(View.VISIBLE);
                    mVideosAdapter = new VideosAdapter(getContext(),videosEntities);
                    mVideosList.setAdapter(mVideosAdapter);
                    int totalVideoHeight = 0;
                    for (int i=0;i<mVideosAdapter.getCount();i++){
                        View videoItem = mVideosAdapter.getView(i,null,mVideosList);
                        videoItem.measure(0,0);
                        totalVideoHeight += videoItem.getMeasuredHeight();
                    }

                    ViewGroup.LayoutParams params = mVideosList.getLayoutParams();

                    params.height =totalVideoHeight+(mVideosList.getDividerHeight()*
                            (mVideosAdapter.getCount()-1));

                    mVideosList.setLayoutParams(params);


                    mVideosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String key = view.getTag().toString();
                            Toast.makeText(getContext(),key,Toast.LENGTH_SHORT).show();
                            openVideoUrl(key);


                        }
                    });
                }else {
                    mNoVideosTextView.setVisibility(View.VISIBLE);
                    mVideosList.setVisibility(View.INVISIBLE);
                }

            }
        }
    }
    //电影时长加载Task
    class RuntimeTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String res;
            try {
                res = OpenMovieJsonUtilsFromMovieDb.getMovieRuntimeFromMD(id);
                return res;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            mMovieRuntimeTextView.setText(s);
        }
    }
    //评论加载Task
    class ReviewsTask extends AsyncTask<String,Void,ReviewsEntity[]>{

        @Override
        protected ReviewsEntity[] doInBackground(String... params) {

            String id = params[0];
            ReviewsEntity[] res;
            try {
                res = OpenMovieJsonUtilsFromMovieDb
                        .getMovieReviewsJsonFromMD(id);
                Log.v("re",res[0].getInfo());
                return res;


            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(ReviewsEntity[] reviewsEntities) {
            if (reviewsEntities[0].getInfo() !="success"||reviewsEntities == null){

                mNoReviewsTextView.setText("暂无评论");
                mNoReviewsTextView.setVisibility(View.VISIBLE);
                mReviewsList.setVisibility(View.GONE);

            }else {
                if (getContext()!=null){
                    mNoReviewsTextView.setVisibility(View.INVISIBLE);
                    mReviewsList.setVisibility(View.VISIBLE);
                    mReviewsAdapter = new ReviewsAdapter(getContext(),reviewsEntities);
                    mReviewsList.setAdapter(mReviewsAdapter);
                }else {
                    mNoReviewsTextView.setText("暂无评论");
                    mNoReviewsTextView.setVisibility(View.VISIBLE);
                    mReviewsList.setVisibility(View.GONE);
                }


            }
        }
    }

}
