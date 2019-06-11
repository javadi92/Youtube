package com.javadi.youtube.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.javadi.youtube.PlayActivity;
import com.javadi.youtube.R;
import com.javadi.youtube.StatisticsActivity;
import com.javadi.youtube.models.Videos;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.mViewHolder> {

    List<Videos> videosList =new ArrayList<>();
    Context mContext;
    Picasso picasso;


    public VideoListAdapter(Context context,List<Videos> videosList,Picasso picasso){
        this.mContext=context;
        this.videosList = videosList;
        this.picasso=picasso;
    }


    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.video_holder,viewGroup,false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final mViewHolder mViewHolder, final int i) {
        picasso.get().load(videosList.get(i).getImage_url_path()).fit().centerInside().into(mViewHolder.imgVideoholder);
        //Glide.with(mContext).load(videosList.get(i).getImage_url_path()).into(mViewHolder.imgVideoholder);
        mViewHolder.tvVideoHolderTitle.setText(videosList.get(i).getVideo_title());
        mViewHolder.tvDuration.setText(videosList.get(i).getVideo_duration());
        mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPlay=new Intent(mContext, StatisticsActivity.class);
                intentPlay.putExtra("video_id",videosList.get(i).getVideo_id());
                if(mViewHolder.imgVideoholder.getDrawable()!=null){
                    Bitmap bm=((BitmapDrawable)mViewHolder.imgVideoholder.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    byte[] bytes=byteArrayOutputStream.toByteArray();
                    intentPlay.putExtra("thumbnail",bytes);
                }
                intentPlay.putExtra("title",mViewHolder.tvVideoHolderTitle.getText().toString());
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
        TextView tvVideoHolderTitle,tvDuration;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            imgVideoholder=(ImageView)itemView.findViewById(R.id.img_video_holder);
            tvVideoHolderTitle=(TextView)itemView.findViewById(R.id.tv_video_holder_title);
            tvDuration=(TextView)itemView.findViewById(R.id.tv_duration);
        }
    }
}
