package com.tongji.wangjimin.tongjinews.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.view.FlipRecyclerView;
import com.tongji.wangjimin.tongjinews.view.ScalePictureView;

public class TestActivity extends AppCompatActivity {

    private FlipRecyclerView mRecyclerView;
    private ScalePictureView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        mRecyclerView = (FlipRecyclerView)findViewById(R.id.fliprecyclerview);
//        TestAdapter adapter = new TestAdapter(this);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
    }
}
