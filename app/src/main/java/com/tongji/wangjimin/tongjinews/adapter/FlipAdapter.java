package com.tongji.wangjimin.tongjinews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tongji.wangjimin.tongjinews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjimin on 17/3/29.
 */

public class FlipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mImages;
    private Context mContext;

    public FlipAdapter(Context context){
        mContext = context;
        mImages = new ArrayList<>(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.flip_item, parent, false);
        return new FlipViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FlipViewHolder fHolder = (FlipViewHolder)holder;
        fHolder.image.setImageURI(mImages.get(position));
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    private class FlipViewHolder extends RecyclerView.ViewHolder{

        private SimpleDraweeView image;

        private FlipViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView)itemView.findViewById(R.id.flip_image);
        }
    }

    public void setData(List<String> data){
        mImages = data;
        notifyDataSetChanged();
    }
}
