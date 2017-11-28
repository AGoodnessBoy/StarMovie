package com.moming.jml.starmovie;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moming.jml.starmovie.entities.ReviewsEntity;

/**
 * Created by admin on 2017/11/28.
 */

public class ReviewsAdapter extends BaseAdapter {


    private LayoutInflater mInflater = null;
    public ReviewsAdapter(Context context,ReviewsEntity[] mRe){
        this.mInflater =
                LayoutInflater.from(context);
        this.mRe = mRe;
    }

    public ReviewsEntity[] mRe;
    static class ViewHolder{
        public TextView name;
        public TextView content;
    }


    @Override
    public int getCount() {
        return mRe.length;
    }

    @Override
    public Object getItem(int position) {
        return  mRe[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null){

            holder = new ViewHolder();
            convertView = mInflater
                    .inflate(R.layout.reviews_list_item,null);

            holder.name =(TextView) convertView.findViewById(R.id.tv_review_name);
            holder.content = (TextView) convertView.findViewById(R.id.tv_review_content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        Log.v("adapter",Integer.toString(position));
        holder.content.setText(mRe[position].getReviews_content());
        holder.name.setText(mRe[position].getAuthor());

        return  convertView;
    }
}
