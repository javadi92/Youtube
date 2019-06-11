package com.javadi.youtube;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;


public class StatisticsActivity extends AppCompatActivity {

    ImageView imgThumbnail,imgPlay;
    ProgressDialog progressDialog;
    FrameLayout frameLayout;
    TextView tvTitle,tvPublisher,tvPublisherConst,tvViewsNumber,tvViewsNumberConst,tvChannel,tvLikes,tvDislikes,tvShare,tvDownload;
    String video_id;
    byte[] bytes;
    int size;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_statistics);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("در حال دریافت اطلاعات ...");
        progressDialog.show();

        frameLayout=(FrameLayout)findViewById(R.id.frameLayout);
        size=frameLayout.getLayoutParams().height;

        video_id=getIntent().getStringExtra("video_id");


        imgThumbnail=(ImageView)findViewById(R.id.img_thumbnail);


        imgPlay=(ImageView)findViewById(R.id.img_play);
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent=new Intent(StatisticsActivity.this,PlayActivity.class);
                playIntent.putExtra("video_id_2",video_id);
                startActivity(playIntent);
            }
        });

        tvTitle=(TextView)findViewById(R.id.tv_titile);
        tvViewsNumber=(TextView)findViewById(R.id.tv_views_number);
        tvLikes=(TextView)findViewById(R.id.tv_likes_number);
        tvDislikes=(TextView)findViewById(R.id.tv_dislikes_number);
        tvPublisher=(TextView)findViewById(R.id.tv_publisher);

        //distribute loade on servers base on hour time
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        if(currentHour>=0 && currentHour<8){
            scraper1(video_id);
        }
        else if(currentHour>=8 && currentHour<16){
            scraper2(video_id);
        }
        else if(currentHour>=16 && currentHour<24){
            scraper3(video_id);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            frameLayout.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;
            tvTitle.setVisibility(View.GONE);
        }
        else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            frameLayout.getLayoutParams().height= size;
            frameLayout.requestLayout();
            tvTitle.setVisibility(View.VISIBLE);
        }
    }

    void scraper1(final String id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document document = null;
                try {
                    document= Jsoup.connect("https://antifilter.herokuapp.com/?q=https://www.youtube.com/watch?v="+id)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                            .get();
                    final Element link=document.select("div[class=yt-user-info]").first();
                    final Elements divs=document.select("div[class=watch-view-count]");
                    final Elements divsLikes=document.select("div[id=watch8-sentiment-actions]");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(getIntent().getByteArrayExtra("thumbnail").length>0){
                                Bitmap bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("thumbnail"), 0, getIntent().getByteArrayExtra("thumbnail").length);
                                imgThumbnail.setImageBitmap(bitmap);
                            }
                            tvTitle.setText(getIntent().getCharSequenceExtra("title"));
                            tvViewsNumber.setText(divs.get(0).text());
                            String likes=divsLikes.get(0).select("span[class=yt-uix-clickcard]").get(0).select("span[class=yt-uix-button-content]").eachText().get(0);
                            tvLikes.setText(likes);
                            String dislikes=divsLikes.get(0).select("span[class=yt-uix-clickcard]").get(2).select("span[class=yt-uix-button-content]").eachText().get(0);
                            tvDislikes.setText(dislikes);
                            String publisher=link.select("a").text();
                            tvPublisher.setText(publisher);
                            progressDialog.dismiss();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void scraper2(final String id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document document = null;
                try {
                    document= Jsoup.connect("https://antifilter2.herokuapp.com/?q=https://www.youtube.com/watch?v="+id)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                            .get();
                    final Element link=document.select("div[class=yt-user-info]").first();
                    final Elements divs=document.select("div[class=watch-view-count]");
                    final Elements divsLikes=document.select("div[id=watch8-sentiment-actions]");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(getIntent().getByteArrayExtra("thumbnail").length>0){
                                Bitmap bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("thumbnail"), 0, getIntent().getByteArrayExtra("thumbnail").length);
                                imgThumbnail.setImageBitmap(bitmap);
                            }
                            tvTitle.setText(getIntent().getCharSequenceExtra("title"));
                            tvViewsNumber.setText(divs.get(0).text());
                            String likes=divsLikes.get(0).select("span[class=yt-uix-clickcard]").get(0).select("span[class=yt-uix-button-content]").eachText().get(0);
                            tvLikes.setText(likes);
                            String dislikes=divsLikes.get(0).select("span[class=yt-uix-clickcard]").get(2).select("span[class=yt-uix-button-content]").eachText().get(0);
                            tvDislikes.setText(dislikes);
                            String publisher=link.select("a").text();
                            tvPublisher.setText(publisher);
                            progressDialog.dismiss();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void scraper3(final String id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document document = null;
                try {
                    document= Jsoup.connect("https://youtube-withoutfilter.herokuapp.com/index.php?q=https://www.youtube.com/watch?v="+id)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                            .get();
                    final Element link=document.select("div[class=yt-user-info]").first();
                    final Elements divs=document.select("div[class=watch-view-count]");
                    final Elements divsLikes=document.select("div[id=watch8-sentiment-actions]");
                    final Elements imagees=document.select("span[class=yt-thumb-square]");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(getIntent().getByteArrayExtra("thumbnail").length>0){
                                Bitmap bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("thumbnail"), 0, getIntent().getByteArrayExtra("thumbnail").length);
                                imgThumbnail.setImageBitmap(bitmap);
                            }
                            String temp=getIntent().getCharSequenceExtra("title").toString();
                            tvTitle.setText(temp);
                            tvViewsNumber.setText(divs.get(0).text());
                            String likes=divsLikes.get(0).select("span[class=yt-uix-clickcard]").get(0).select("span[class=yt-uix-button-content]").eachText().get(0);
                            tvLikes.setText(likes);
                            String dislikes=divsLikes.get(0).select("span[class=yt-uix-clickcard]").get(2).select("span[class=yt-uix-button-content]").eachText().get(0);
                            tvDislikes.setText(dislikes);
                            String publisher=link.select("a").text();
                            tvPublisher.setText(publisher);
                            progressDialog.dismiss();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }).start();
    }
}
