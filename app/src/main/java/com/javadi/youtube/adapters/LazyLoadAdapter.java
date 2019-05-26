package com.javadi.youtube.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.javadi.youtube.OnLoadMoreListener;
import com.javadi.youtube.PlayActivity;
import com.javadi.youtube.R;
import com.javadi.youtube.models.Videos;
import com.squareup.picasso.Picasso;
import java.util.List;


public class LazyLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    public static boolean isLoading;
    private List<Videos> videosList;
    Context mContext;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public LazyLoadAdapter(RecyclerView recyclerView, List<Videos> videosList, Context context) {
        this.videosList = videosList;
        this.mContext=context;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return videosList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.video_holder, parent, false);
            return new mViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof mViewHolder) {
            final Videos videos = videosList.get(position);
            mViewHolder mViewHolder = (mViewHolder) holder;
            Picasso.get().load(videos.getImage_url_path()).fit().centerInside().into(mViewHolder.imgVideoholder);
            mViewHolder.tvVideoHolderTitle.setText(videos.getVideo_title());
            mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentPlay=new Intent(mContext, PlayActivity.class);
                    intentPlay.putExtra("video_id",videos.getVideo_id());
                    mContext.startActivity(intentPlay);
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return videosList == null ? 0 : videosList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    class mViewHolder extends RecyclerView.ViewHolder{

        ImageView imgVideoholder;
        TextView tvVideoHolderTitle;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            imgVideoholder=(ImageView)itemView.findViewById(R.id.img_video_holder);
            tvVideoHolderTitle=(TextView)itemView.findViewById(R.id.tv_video_holder_title);
        }
    }
}
