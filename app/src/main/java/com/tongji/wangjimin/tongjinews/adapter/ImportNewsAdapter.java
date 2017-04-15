package com.tongji.wangjimin.tongjinews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tongji.wangjimin.tongjinews.NewsApplication;
import com.tongji.wangjimin.tongjinews.R;
import com.tongji.wangjimin.tongjinews.data.NewsReaderContract;
import com.tongji.wangjimin.tongjinews.data.NewsReaderDbHelper;
import com.tongji.wangjimin.tongjinews.net.News;
import com.tongji.wangjimin.tongjinews.utils.Utils;

import java.util.List;

/**
 * Created by wangjimin on 17/2/24.
 * ImportNewsAdapter, for
 * {@link com.tongji.wangjimin.tongjinews.fragment.ImportNewsFragment}'s
 * mRecyclerView.
 */

public class ImportNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int ONE_SCREEN_NEWS_NUM = 3;

    /* Import news view holder. */
    private class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView date;
        private TextView readNum;
        private SimpleDraweeView image;
        private ImageView fav;
        private ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.item_title);
            readNum = (TextView)itemView.findViewById(R.id.item_readnum);
            date = (TextView)itemView.findViewById(R.id.item_date);
            image = (SimpleDraweeView)itemView.findViewById(R.id.item_image);
            fav = (ImageView)itemView.findViewById(R.id.item_fav);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null)
                        mListener.onClick(getAdapterPosition());
                }
            });
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isCollect = false;
                    News news = mData.get(getAdapterPosition());
                    int drawableId = R.drawable.ic_favorite_border_black_24dp;
                    NewsReaderDbHelper dbHelper = NewsReaderDbHelper.getInstance(mContext);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    if(!dbHelper.isExist(db, NewsReaderContract.NewsEntry.TABLE_FAV_NAME, news,
                            NewsReaderDbHelper.MODE_INSERT)){
                        drawableId = R.drawable.ic_favorite_black_24dp;
                        Toast.makeText(mContext, R.string.collect_msg, Toast.LENGTH_SHORT).show();
                        isCollect = true;
                    } else {
                        dbHelper.deleteNews(db, NewsReaderContract.NewsEntry.TABLE_FAV_NAME, news);
                        Toast.makeText(mContext, R.string.remove_fav_msg, Toast.LENGTH_SHORT).show();
                    }
                    if(mFavListener != null){
                        mFavListener.onClick(isCollect, getAdapterPosition());
                    }
                    fav.setImageDrawable(Utils.getDrawable(mContext, drawableId));
                }
            });
        }
    }

    /* Load more indicator view holder. */
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
    private FavoritesClickListener mFavListener;
    private boolean mIsRefresh = false;

    public ImportNewsAdapter(Context context){
        this(context, false);
    }

    public ImportNewsAdapter(Context context, boolean isRefresh){
        mContext = context;
        mData = NewsApplication.getInstance().getNewsList();
        mIsRefresh = isRefresh;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root;
        switch (viewType){
            case TYPE_NORMAL:
                root = LayoutInflater.from(mContext)
                        .inflate(R.layout.importnews_recyclerview_item, parent, false);
                return new ViewHolder(root);
            case TYPE_FOOTER:
                root = LayoutInflater.from(mContext)
                        .inflate(R.layout.importnews_recyclerview_item_footer, parent, false);
                return new FootHolder(root);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case TYPE_NORMAL:
                News data = mData.get(position);
                ViewHolder h = (ViewHolder)holder;
                h.title.setText(data.getTitle());
                h.date.setText(data.getDate());
                h.readNum.setText(mContext.getString(R.string.read_num, data.getReadNum()));
                List<String> images = data.getImages();
                if(images == null || images.size() < 1){
                    //noinspection deprecation
                    h.image.setImageResource(R.color.green);
                    return;
                }
                h.image.setImageURI(images.get(0));
                NewsReaderDbHelper dbHelper = NewsReaderDbHelper.getInstance(mContext);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int drawableId = R.drawable.ic_favorite_border_black_24dp;
                if(dbHelper.isExist(db, NewsReaderContract.NewsEntry.TABLE_FAV_NAME,
                        data, NewsReaderDbHelper.MODE_NO_OP)) {
                    drawableId = R.drawable.ic_favorite_black_24dp;
                }
                h.fav.setImageDrawable(Utils.getDrawable(mContext, drawableId));
                break;
            case TYPE_FOOTER:
        }
    }

    @Override
    public int getItemCount() {
        if(mData == null){
            return 0;
        }
        if(mIsRefresh){
            //3 is on screen to show.
            if(mData.size() < ONE_SCREEN_NEWS_NUM)
                return mData.size();
            return mData.size() + 1;
        } else {
            return mData.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mIsRefresh){
            if(position == mData.size())
                return TYPE_FOOTER;
            return TYPE_NORMAL;
        } else {
            return TYPE_NORMAL;
        }
    }

    public void setDataAndNotify(List<News> data){
        if(data == null)
            return;
        mData = data;
        notifyDataSetChanged();
    }

    public void addAll(List<News> data){
        NewsApplication.getInstance().addNews(data);
        notifyDataSetChanged();
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

    public void clearAdapterOwnData(){
        mData = null;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ClickListener listener){
        mListener = listener;
    }

    public interface ClickListener{
        void onClick(int position);
    }

    public void setOnItemFavoritesClickListener(FavoritesClickListener listener){
        mFavListener = listener;
    }

    public interface FavoritesClickListener{
        void onClick(boolean isCollect, int pos);
    }
}
