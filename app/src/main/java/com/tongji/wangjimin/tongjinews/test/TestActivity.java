package com.tongji.wangjimin.tongjinews.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.view.FlipRecyclerView;

public class TestActivity extends AppCompatActivity {

    private FlipRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mRecyclerView = (FlipRecyclerView)findViewById(R.id.fliprecyclerview);
        TestAdapter adapter = new TestAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
