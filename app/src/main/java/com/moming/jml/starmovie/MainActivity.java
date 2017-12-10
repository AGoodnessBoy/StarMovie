package com.moming.jml.starmovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.bus.ActivityResultBus;
import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.bus.ActivityResultEvent;
import com.moming.jml.starmovie.data.MovieContract;
import com.moming.jml.starmovie.data.MoviePerference;
import com.moming.jml.starmovie.fragment.MovieDetailFragment;
import com.moming.jml.starmovie.fragment.MovieListFragment;
import com.moming.jml.starmovie.sync.MovieSyncUtils;

import static com.moming.jml.starmovie.fragment.MovieListFragment.ID_MOVIE_LOADER;

public class MainActivity extends AppCompatActivity{

    public final static String LASTOFFSET = "last_offset";
    public final static String LASTPOSITION = "last_position";
    private final String TAG = MainActivity.class.getSimpleName();

    Bundle saveStateInMain;


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

    final static String SORT_BY_POP ="pop";
    final static String SORT_BY_TOP ="top";
    final static String SORT_KET = "sortBy";
    final static String USER_COLLECTION="col";

    public static final int ID_MOVIE_LOADER_POP= 28;

    public static final int ID_MOVIE_LOADER_TOP = 29;

    public static final int ID_MOVIE_LOADER_COL = 30;

    private static MovieListFragment movieListFragment;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R
                .layout.activity_main);

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();


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
                Log.v(TAG,"pop");
                mlf.resetPosition();
                getSupportLoaderManager().restartLoader(
                        ID_MOVIE_LOADER_POP,bundle,mlf.callbacks
                );
                MoviePerference.setSortWayFromPerf(this,SORT_BY_POP);
                break;
            case R.id.action_sort_by_rating:
                Log.v(TAG,"top");
                mlf.resetPosition();
                getSupportLoaderManager().restartLoader(
                        ID_MOVIE_LOADER_TOP,null,mlf.callbacks
                );
                MoviePerference.setSortWayFromPerf(this,SORT_BY_TOP);
                break;
            case R.id.action_user_collection:
                mlf.resetPosition();
                getSupportLoaderManager().restartLoader(
                        ID_MOVIE_LOADER_COL,null,mlf.callbacks
                );
                MoviePerference.setSortWayFromPerf(this,USER_COLLECTION);
                break;
            case R.id.action_settings:

                Intent startSettingsActivity = new
                        Intent(this,SettingsActivity.class);
                startActivity(startSettingsActivity);
                break;
            case R.id.action_reflash:
                mlf.resetPosition();
                MovieSyncUtils.startImmediateSync(this);
                getSupportLoaderManager().restartLoader(
                        ID_MOVIE_LOADER_POP,null,mlf.callbacks
                );
                MoviePerference.setSortWayFromPerf(this,SORT_BY_POP);

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
