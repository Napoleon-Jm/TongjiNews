package com.tongji.wangjimin.tongjinews.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tongji.wangjimin.tongjinews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjimin on 17/2/25.
 * NewsContentAdapter, for
 * {@link com.tongji.wangjimin.tongjinews.NewsContentActivity}.
 */

public class NewsContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_TEXT = 0;
    private final int TYPE_IMAGE = 1;

    /**
     * Outer class could access inner class private member.
     */
    private class TextViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private TextViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.newscontent_item_title);
        }
    }

    /**
     * Image ViewHolder
     */
    private class ImageViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView image;
        public ImageViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView)itemView.findViewById(R.id.newscontent_item_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(getAdapterPosition());
                }
            });
        }
    }

    private List<String> mData;
    private Context mContext;
    private ClickListener mListener;

    public NewsContentAdapter(Context context){
        mContext = context;
        mData = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root;
        switch (viewType){
            case TYPE_IMAGE:
                root = LayoutInflater.from(mContext).inflate(R.layout.newscontent_recyclerview_item_image,
                    parent, false);
                return new ImageViewHolder(root);
            case TYPE_TEXT:
                root = LayoutInflater.from(mContext).inflate(R.layout.newscontent_recyclerview_item_text,
                        parent, false);
                return new TextViewHolder(root);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case TYPE_IMAGE:{
                //test
                int startIndex = mData.get(position).indexOf("http");
                int endIndex = mData.get(position).lastIndexOf("jpg");
                if(endIndex == -1){
                    endIndex = mData.get(position).lastIndexOf("png");
                }
                endIndex += 3;
                if(startIndex < endIndex){
                    //substring, 不包含endIndex位置上的字符.
                    String url = mData.get(position).substring(startIndex, endIndex);
                    ((ImageViewHolder)holder).image.setImageURI(url);
                }
                break;
            }
            case TYPE_TEXT:{
                /**
                 * Make {@link TextView} show html.
                 */
                Spanned result;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    result = Html.fromHtml(mData.get(position),Html.FROM_HTML_MODE_LEGACY);
                } else {
                    result = Html.fromHtml(mData.get(position));
                }
                ((TextViewHolder)holder).textView.setText(result);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setDataAndNotify(List<String> data){
        mData = data;
        notifyDataSetChanged();
    }

    @Nullable
    public String getItemData(int pos){
        if(pos < mData.size())
            return mData.get(pos);
        return null;
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        return mData.get(position).contains("src")? TYPE_IMAGE: TYPE_TEXT;
    }

    public void setClickListener(ClickListener listener){
        mListener = listener;
    }

    public interface ClickListener{
        void onClick(int position);
    }
}
