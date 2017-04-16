package com.tongji.wangjimin.tongjinews.activity;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.utils.Utils;
import com.tongji.wangjimin.tongjinews.view.HideSoftInputLinearLayout;

public class SearchActivity extends AppCompatActivity {

    private HideSoftInputLinearLayout mContainer;
    private EditText mSearchEditText;
    private TextView mCanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContainer = (HideSoftInputLinearLayout)findViewById(R.id.container_search_activity);
        mContainer.setOnInterceptTouchEvent(new HideSoftInputLinearLayout.OnInterceptTouchEvent() {
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                int[] location = new int[2];
                mSearchEditText.getLocationOnScreen(location);
                Rect area = new Rect(location[0], location[1],
                        location[0] + mSearchEditText.getWidth(),
                        location[1] + mSearchEditText.getHeight());
                if(!area.contains((int)ev.getRawX(), (int)ev.getRawY())){
                    mSearchEditText.clearFocus();
                    mSearchEditText.setCursorVisible(false);
                    Utils.hideSoftKeyboard(SearchActivity.this);
                }
                return false;
            }
        });
        mSearchEditText = (EditText)findViewById(R.id.search_edittext);
        /* 将回车按键显示为放大镜 */
        mSearchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEditText.setCursorVisible(true);
            }
        });
        mCanel = (TextView)findViewById(R.id.canel);
        mCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
