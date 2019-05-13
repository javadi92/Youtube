package com.javadi.youtube.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.javadi.youtube.PlayActivity;
import com.javadi.youtube.R;
import com.javadi.youtube.models.Videos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.mViewHolder> {

    List<Videos> videosList =new ArrayList<>();
    Context mContext;

    public VideoListAdapter(Context context,List<Videos> videosList){
        this.mContext=context;
        this.videosList = videosList;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.video_holder,viewGroup,false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder mViewHolder, final int i) {
        Picasso.get().load(videosList.get(i).getImage_url_path()).fit().centerInside().into(mViewHolder.imgVideoholder);
        mViewHolder.tvVideoHolderTitle.setText(videosList.get(i).getVideo_title());
        mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPlay=new Intent(mContext, PlayActivity.class);
                intentPlay.putExtra("video_id",videosList.get(i).getVideo_id());
                mContext.startActivity(intentPlay);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videosList.size();
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
