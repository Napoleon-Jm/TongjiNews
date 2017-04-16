package com.tongji.wangjimin.tongjinews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.net.search.NewsItemDigest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjimin on 17/4/16.
 * SearchAdapter.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /* Search item holder. */
    private class SearchItemHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView text;

        private SearchItemHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.search_item_title);
            text = (TextView)itemView.findViewById(R.id.search_item_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    private Context mContext;
    private List<NewsItemDigest> mData;
    private SearchItemClickListener mListener;

    public SearchAdapter(Context context){
        mContext = context;
        mData = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.search_item, parent, false);
        return new SearchItemHolder(root);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsItemDigest data = mData.get(position);
        SearchItemHolder sHolder = (SearchItemHolder)holder;
        sHolder.title.setText(data.getTitle());
        sHolder.text.setText(data.getDigest());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public List<NewsItemDigest> getData(){
        return mData;
    }

    public void setDataAndNotify(List<NewsItemDigest> data){
        mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(SearchItemClickListener listener){
        mListener = listener;
    }

    public interface SearchItemClickListener{
        void onClick(int pos);
    }
}
