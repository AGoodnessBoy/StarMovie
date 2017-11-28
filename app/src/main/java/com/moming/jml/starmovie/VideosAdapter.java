package com.moming.jml.starmovie;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moming.jml.starmovie.entities.VideosEntity;

/**
 * Created by admin on 2017/11/28.
 */

public class VideosAdapter extends BaseAdapter{

    private LayoutInflater mInflater =null;
    public VideosEntity[] mVes;

    public VideosAdapter(Context context,VideosEntity[] mVes){
        this.mInflater =LayoutInflater.from(context);
        this.mVes=mVes;
    }

    static class ViewHolder{
        private TextView name;
    }

    @Override
    public int getCount() {
        return mVes.length;
    }

    @Override
    public Object getItem(int position) {
        return mVes[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        convertView = mInflater.inflate(
                R.layout.videos_list_item,null);
        holder.name = (TextView) convertView.findViewById(R.id.tv_video_name);


        holder.name.setText(mVes[position].getVideo_name());
        convertView.setTag(mVes[position].getVideo_key());
        Log.v("tag",convertView.getTag().toString());


        return  convertView;
    }
}
