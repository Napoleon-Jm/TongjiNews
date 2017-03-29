package com.tongji.wangjimin.tongjinews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tongji.wangjimin.tongjinews.NewsApplication;
import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.net.News;

import java.util.List;

/**
 * Created by wangjimin on 17/3/17.
 */

public class DigestImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private class ImageViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView image;
        private ImageViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView)itemView.findViewById(R.id.simpledraweeview_item);
        }
    }

    private Context mContext;
    private ItemClickListener mListener;

    public DigestImageAdapter(Context context){
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.digestimage_item, parent, false);
        return new ImageViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder imageHolder = (ImageViewHolder)holder;
        List<String> images = getDataSet().get(position);
        if(images != null && images.size() > 0){
            imageHolder.image.setImageURI(images.get(0));
            imageHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onClick(v, imageHolder.getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getDataSet().size();
    }

    public List<List<String> > getDataSet(){
        return NewsApplication.getInstance().getImageList();
    }

    public void addAll(List<News> news){
        NewsApplication.getInstance().addNews(news);
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener listener){
        mListener = listener;
    }

    public interface ItemClickListener{
        void onClick(View v, int pos);
    }
}
