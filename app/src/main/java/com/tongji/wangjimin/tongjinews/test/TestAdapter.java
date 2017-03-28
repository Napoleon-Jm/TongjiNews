package com.tongji.wangjimin.tongjinews.test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tongji.wangjimin.tongjinews.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangjimin on 17/3/28.
 */

public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mData;

    public TestAdapter(Context context){
        mContext = context;
        mData = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            mData.add("Item : " + i);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.test_item, parent, false);
        return new TestViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TestViewHolder testHolder = (TestViewHolder)holder;
        testHolder.textView.setText(mData.get(position));
        testHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "click " + mData.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class TestViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        private TestViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.test_textview_item);
        }
    }
}
