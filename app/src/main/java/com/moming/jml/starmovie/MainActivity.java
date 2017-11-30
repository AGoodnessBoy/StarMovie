package com.moming.jml.starmovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.moming.jml.starmovie.data.MovieContract;
import com.moming.jml.starmovie.fragment.MovieListFragment;
import com.moming.jml.starmovie.sync.MovieSyncUtils;

import static com.moming.jml.starmovie.fragment.MovieListFragment.ID_MOVIE_LOADER;

public class MainActivity extends AppCompatActivity{


    private final String TAG = MainActivity.class.getSimpleName();


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

    final static String SORT_BY_POP ="1";
    final static String SORT_BY_TOP ="2";
    final static String SORT_DEFAULT="0";
    final static String SORT_KET = "sortBy";
    final static String USER_COLLECTION="user_collection";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R
                .layout.activity_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int theSelectedItemId=item.getItemId();
        Bundle bundle = new Bundle();
        MovieListFragment mlf = (MovieListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.movie_list_fragment);

        switch (theSelectedItemId){
            case R.id.action_sort_by_popular:
                Log.v("menu","pop");
                bundle.putString(SORT_KET,SORT_BY_POP);
                getSupportLoaderManager().restartLoader(
                        ID_MOVIE_LOADER,bundle,mlf.callbacks
                );
                bundle.clear();
                break;
            case R.id.action_sort_by_rating:
                Log.v("menu","top");
                bundle.putString(SORT_KET,SORT_BY_TOP);
                getSupportLoaderManager().restartLoader(
                        ID_MOVIE_LOADER,bundle,mlf.callbacks
                );
                bundle.clear();
                break;
            case R.id.action_user_collection:
                bundle.putString(SORT_KET,USER_COLLECTION);
                getSupportLoaderManager().restartLoader(
                        ID_MOVIE_LOADER,bundle,mlf.callbacks
                );
                bundle.clear();
                break;
            case R.id.action_reflash:

                MovieSyncUtils.startImmediateSync(this);
                bundle.putString(SORT_KET,SORT_BY_POP);
                getSupportLoaderManager().restartLoader(
                        ID_MOVIE_LOADER,bundle,mlf.callbacks
                );
                bundle.clear();

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
