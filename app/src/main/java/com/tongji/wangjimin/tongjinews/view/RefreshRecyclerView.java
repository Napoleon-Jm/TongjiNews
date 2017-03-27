package com.tongji.wangjimin.tongjinews.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by wangjimin on 17/3/27.
 * RefreshRecyclerView.
 */

public class RefreshRecyclerView extends RecyclerView{

    private boolean mIsLoading = false;
    private Refresher mRefresher;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem = -1;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItem + 1 == recyclerView.getAdapter().getItemCount())) {
                    if (!mIsLoading) {
                        //loading next page.
                        mIsLoading = true;
                        if(mRefresher != null){
                            //must set mIsLoading false when refresh is done. @setLoadingDone
                            mRefresher.refresh();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager)recyclerView.getLayoutManager();
                lastVisibleItem = manager.findLastVisibleItemPosition();
            }
        });
    }

    public void setLoadingDone(){
        mIsLoading = false;
    }

    public void setRefreshWork(Refresher ref){
        mRefresher = ref;
    }

    public interface Refresher{
        void refresh();
    }

}
