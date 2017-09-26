package com.moming.jml.starmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moming.jml.starmovie.entities.MovieEntity;
import com.squareup.picasso.Picasso;

/**
 * Created by jml on 2017/9/22.
 */

public class IndexMovieAdapter extends RecyclerView.Adapter<IndexMovieAdapter.MovieAdapterViewHolder> {

    private MovieEntity[] mMovieData;

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

        View view  = inflater.inflate(layoutIdForListItem,parent,shouldAttachToPartentImmediately);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Context context =holder.itemView.getContext();
        holder.mMovieNameTextView.setText(mMovieData[position].getMovie_name());
        holder.mMovieRatingTextView.setText(mMovieData[position].getMovie_rating());
        Picasso.with(context).load(mMovieData[position].getMovie_img_url()).into(holder.mMoviePosterImageView);

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

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieNameTextView =(TextView)itemView.findViewById(R.id.tv_movie_name);
            mMovieRatingTextView = (TextView)itemView.findViewById(R.id.tv_movie_rating);
            mMoviePosterImageView = (ImageView)itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition =getAdapterPosition();
            String movieId = mMovieData[adapterPosition].getMovie_id();
            mClickHandler.onClick(movieId);
        }
    }
    public void setMovieData(MovieEntity[] movieData){
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
