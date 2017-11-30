package com.moming.jml.starmovie;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Outline;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by jml on 2017/9/22
 */

public class IndexMovieAdapter extends RecyclerView.Adapter<IndexMovieAdapter.MovieAdapterViewHolder> {



    private final Context mContext;

    protected Cursor mCursor;

    private final IndexMovieAdapterOnClickHandler mClickHandler;

    public interface IndexMovieAdapterOnClickHandler{
        void onClick(String movieId);
    }

    public IndexMovieAdapter(Context context,IndexMovieAdapterOnClickHandler clickHandler){
        mClickHandler=clickHandler;
        mContext = context;
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater =LayoutInflater.from(mContext);
        boolean shouldAttachToPartentImmediately = false;

        View view  = inflater.inflate(layoutIdForListItem,parent,shouldAttachToPartentImmediately);
        view.setFocusable(true);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        Context context =holder.itemView.getContext();
        String img_base_url="http://image.tmdb.org/t/p/w300/";
        holder.mMovieNameTextView.setText(mCursor.getString(MainActivity.INDEX_MOVIE_TITLE));
        holder.mMovieRatingTextView.setText(mCursor.getString(MainActivity.INDEX_MOVIE_VOTE));
        holder.mMovieDateTextView.setText(mCursor.getString(MainActivity.INDEX_MOVIE_RELEASE));
        String movie_img_url=img_base_url+mCursor.getString(MainActivity.INDEX_MOVIE_POSTER);
        Picasso.with(context).load(movie_img_url)
                .error(R.drawable.default_img).into(holder.mMoviePosterImageView);
        ViewOutlineProvider mProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                view.setClipToOutline(true);
                outline.setRoundRect(0,0,view.getWidth(),view.getHeight(),2);
            }
        };
        holder.mMovieItemFrameLayout.setOutlineProvider(mProvider);

    }


    @Override
    public int getItemCount() {
        if(null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mMovieNameTextView;
        public final TextView mMovieRatingTextView;
        public final ImageView mMoviePosterImageView;
        public final FrameLayout mMovieItemFrameLayout;
        public final TextView mMovieDateTextView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieNameTextView =(TextView)itemView.findViewById(R.id.tv_movie_name);
            mMovieRatingTextView = (TextView)itemView.findViewById(R.id.tv_movie_rating);
            mMoviePosterImageView = (ImageView)itemView.findViewById(R.id.iv_movie_poster);
            mMovieItemFrameLayout = (FrameLayout)itemView.findViewById(R.id.layout_of_movie_item) ;
            mMovieDateTextView = (TextView)itemView.findViewById(R.id.tv_movie_date) ;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition =getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String movieId = mCursor.getString(MainActivity.INDEX_MOVIE_ID);
            mClickHandler.onClick(movieId);
        }
    }
}
