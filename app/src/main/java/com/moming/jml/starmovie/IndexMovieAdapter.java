package com.moming.jml.starmovie;

import android.content.Context;
import android.graphics.Outline;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.moming.jml.starmovie.entities.NewMovieEntity;
import com.squareup.picasso.Picasso;

/**
 * Created by jml on 2017/9/22.
 */

public class IndexMovieAdapter extends RecyclerView.Adapter<IndexMovieAdapter.MovieAdapterViewHolder> {

    private NewMovieEntity[] mMovieData;

    private final IndexMovieAdapterOnClickHandler mClickHandler;

    public interface IndexMovieAdapterOnClickHandler{
        void onClick(String movieId);
    }

    public IndexMovieAdapter(IndexMovieAdapterOnClickHandler clickHandler){
        mClickHandler=clickHandler;
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater =LayoutInflater.from(context);
        boolean shouldAttachToPartentImmediately = false;

        View view  = inflater.inflate(layoutIdForListItem,parent,false);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Context context =holder.itemView.getContext();
        String img_base_url="http://image.tmdb.org/t/p/w300/";
        holder.mMovieNameTextView.setText(mMovieData[position].getTitle());
        holder.mMovieRatingTextView.setText(mMovieData[position].getVote());
        String movie_img_url=img_base_url+mMovieData[position].getPoster_path();
        Picasso.with(context).load(movie_img_url).into(holder.mMoviePosterImageView);
        ViewOutlineProvider mProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                view.setClipToOutline(true);
                outline.setRoundRect(16,16,view.getWidth(),view.getHeight(),10);
            }
        };
        holder.mMovieItemFrameLayout.setOutlineProvider(mProvider);

    }


    @Override
    public int getItemCount() {
        if(null == mMovieData) return 0;
        return mMovieData.length;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mMovieNameTextView;
        public final TextView mMovieRatingTextView;
        public final ImageView mMoviePosterImageView;
        public final FrameLayout mMovieItemFrameLayout;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieNameTextView =(TextView)itemView.findViewById(R.id.tv_movie_name);
            mMovieRatingTextView = (TextView)itemView.findViewById(R.id.tv_movie_rating);
            mMoviePosterImageView = (ImageView)itemView.findViewById(R.id.iv_movie_poster);
            mMovieItemFrameLayout = (FrameLayout)itemView.findViewById(R.id.layout_of_movie_item) ;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition =getAdapterPosition();
            String movieId = mMovieData[adapterPosition].getId();
            mClickHandler.onClick(movieId);
        }
    }
    public void setMovieData(NewMovieEntity[] movieData){
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
