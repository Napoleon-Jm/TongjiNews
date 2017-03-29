package com.tongji.wangjimin.tongjinews.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tongji.wangjimin.tongjinews.NewsApplication;
import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.net.News;

import java.util.List;

/**
 * Created by wangjimin on 17/2/24.
 * ImportNewsAdapter.
 */

public class ImportNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;

    private class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView date;
        private TextView readNum;
        private SimpleDraweeView image;
        private ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.item_title);
            readNum = (TextView)itemView.findViewById(R.id.item_readnum);
            date = (TextView)itemView.findViewById(R.id.item_date);
            image = (SimpleDraweeView)itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(v -> {
                if(mListener != null)
                    mListener.onClick(getAdapterPosition());
            });
        }
    }

    private class FootHolder extends RecyclerView.ViewHolder{
        private ProgressBar progressBar;
        private FootHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar)itemView.findViewById(R.id.importnews_item_progressbar);
        }
    }

    private Context mContext;
    private List<News> mData;
    private ClickListener mListener;

    public ImportNewsAdapter(Context context){
        mContext = context;
        mData = NewsApplication.getInstance().getNewsList();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root;
        switch (viewType){
            case TYPE_NORMAL:
                root = LayoutInflater.from(mContext).inflate(R.layout.importnews_recyclerview_item, parent, false);
                return new ViewHolder(root);
            case TYPE_FOOTER:
                root = LayoutInflater.from(mContext).inflate(R.layout.importnews_recyclerview_item_footer, parent, false);
                return new FootHolder(root);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case TYPE_NORMAL:
                ViewHolder h = (ViewHolder)holder;
                h.title.setText(mData.get(position).getTitle());
                h.date.setText(mData.get(position).getDate());
                h.readNum.setText("Read: " + mData.get(position).getReadNum());
                List<String> images = mData.get(position).getImages();
                if(images == null || images.size() < 1){
//                    h.image.setBackgroundResource(R.color.green);
                    h.image.setImageResource(R.color.green);
                    return;
                }
                h.image.setImageURI(images.get(0));
                break;
            case TYPE_FOOTER:
        }
    }

    @Override
    public int getItemCount() {
        //3 is on screen to show.
        if(mData.size() < 3)
            return mData.size();
        return mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mData.size())
            return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    public void setDataAndNotify(List<News> data){
        if(data == null)
            return;
        mData = data;
        notifyDataSetChanged();
        Log.d("wjm", data.size() + "");
    }

    public void addAll(List<News> data){
//        mData.addAll(data);
        NewsApplication.getInstance().addNews(data);
        notifyDataSetChanged();
//        List<News> debug = NewsApplication.getInstance().getNewsList();
//        int size = debug.size();
    }

    @Nullable
    public News getNews(int i){
        if(i < mData.size()){
            return mData.get(i);
        }
        return null;
    }

    public void removeData(int position){
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(ClickListener listener){
        mListener = listener;
    }

    public interface ClickListener{
        void onClick(int position);
    }
}
