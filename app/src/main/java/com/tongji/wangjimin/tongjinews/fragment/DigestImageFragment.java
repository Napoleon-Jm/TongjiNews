package com.tongji.wangjimin.tongjinews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.adapter.DigestImageAdapter;

/**
 * Created by wangjimin on 17/3/17.
 */

public class DigestImageFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private boolean isFirstVisibile;
    private DigestImageAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_digestimage, container, false);
        mRecyclerView = (RecyclerView)root.findViewById(R.id.recyclerview_digestimage);
        mAdapter = new DigestImageAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getUserVisibleHint() && isFirstVisibile){
            loadData();
            isFirstVisibile = false;
        }
    }

    private void loadData() {
        mAdapter.notifyDataSetChanged();
    }

    public void reloadData(){
        Log.d("wjm", "reload " + mAdapter.getItemCount());
        mAdapter.notifyDataSetChanged();
    }
}
