package com.javadi.youtube;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.javadi.youtube.adapters.LazyLoadAdapter;
import com.javadi.youtube.adapters.VideoListAdapter;
import com.javadi.youtube.models.Videos;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //views
    ImageView imgSearch;
    Picasso picasso;
    EditText etSearch;
    RecyclerView recyclerView;
    static VideoListAdapter videoListAdapter;
    LazyLoadAdapter lazyLoadAdapter;
    static List<Videos> videosList=new ArrayList<>();
    ProgressDialog progressDialog;
    //for distribute servers
    public static int distributed=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init views
        imgSearch=(ImageView)findViewById(R.id.img_search);
        etSearch=(EditText)findViewById(R.id.et_search);
        picasso=Picasso.get();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //add listener for recyclerview for detect more laod
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    search(videosList.size());
                }
            }
        });

        progressDialog = new ProgressDialog(this);

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    search(0);
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
        clickManager();
    }

    private void clickManager(){
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(0);
                hideKeyboard();
            }
        });
    }

    //hide keyboard
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void search(final int s){
        if(!etSearch.getText().toString().equals("")){
            if(s==0){
                videosList.clear();
                videoListAdapter=new VideoListAdapter(MainActivity.this,videosList,picasso);
                recyclerView.setAdapter(videoListAdapter);
            }
            //if(distributed==0){
                try {
                    jsoupRequestVideoInfo(URLEncoder.encode(etSearch.getText().toString(),"UTF-8"),s);
                    progressDialog.setMessage("در حال دریافت اطلاعات ...");
                    progressDialog.show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
        }
    }

    public void jsoupRequestVideoInfo(final String query,final int s){
        new Thread(){
            @Override
            public void run() {
                //super.run();
                String url = "https://www.google.com/search?q="+query+"+site:youtube.com&tbm=vid&start="+s;
                Document document = null;
                try {
                    document = Jsoup
                            .connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                            .get();
                    final Elements divs=document.select("div[class=g]");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0;i<divs.size();i=i+1){
                                if(divs.get(i).select("div[class=rc]").select("div[class=s]").select("div").hasClass("th N3nEGc i0PvJb")){
                                    Videos videos=new Videos();
                                    String title=divs.get(i).select("div[class=rc]").select("div[class=r]").select("a").select("h3").text();
                                    String temp=divs.get(i).select("div[class=rc]").select("div[class=r]").select("a").select("div[class=TbwUpd]").text();
                                    String duration=divs.get(i).select("div[class=rc]").select("div[class=s]").select("div").select("div[class=th N3nEGc i0PvJb]").select("a").select("span[class=vdur mWTy7c]").text();
                                    videos.setVideo_title(title);
                                    String video_id=temp.substring(temp.indexOf("?v=")+3);
                                    videos.setVideo_id(video_id);
                                    videos.setImage_url_path("http://javadimehr.ir/mini/?id="+video_id);
                                    videos.setVideo_duration(duration);
                                    videosList.add(videos);
                                }
                            }
                            videoListAdapter=new VideoListAdapter(MainActivity.this,videosList,picasso);
                            recyclerView.setAdapter(videoListAdapter);
                            recyclerView.scrollToPosition(videosList.size()-10);
                            progressDialog.dismiss();
                        }
                    });
                } catch (final IOException e) {
                    e.printStackTrace();
                    Log.e("error1",e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"خطا در برقراری ارتباط، لطفا از طریق گوگل کروم اقدام کنید",Toast.LENGTH_LONG).show();
                        }
                    });
                    progressDialog.dismiss();
                }
            }
        }.start();
    }

    //9m3usPP0ZoH1QyYW1LuNvE1hjq0XgdQn9IpnnoLucV5hhuQRl0KNxQ7V4T%2Fk7b7zmCJY8H0Sj%2Ff43fO7egUn6g%3D%3D&b=7

    public void jsoupRequestVideoInfo2(final String query,final int s){
        new Thread(){
            @Override
            public void run() {
                //super.run();
                String url = "https://www.google.com/search?q="+query+"+site:youtube.com&tbm=vid&start="+s;
                Document document = null;
                try {
                    document = Jsoup.connect(url).get();
                    final Elements divs=document.select("div[class=g]");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0;i<divs.size();i=i+1){
                                if(divs.get(i).select("div[class=rc]").select("div[class=s]").select("div").hasClass("th N3nEGc i0PvJb")){
                                    Videos videos=new Videos();
                                    String title=divs.get(i).select("div[class=rc]").select("div[class=r]").select("a").select("h3").text();
                                    String temp=divs.get(i).select("div[class=rc]").select("div[class=r]").select("a").select("div[class=TbwUpd]").text();
                                    String duration=divs.get(i).select("div[class=rc]").select("div[class=s]").select("div").select("div[class=th N3nEGc i0PvJb]").select("a").select("span[class=vdur mWTy7c]").text();
                                    videos.setVideo_title(title);
                                    String video_id=temp.substring(temp.indexOf("?v=")+3);
                                    videos.setVideo_id(video_id);
                                    videos.setImage_url_path("https://youtube-withoutfilter.herokuapp.com/?q=https://img.youtube.com/vi/"+video_id+"/mqdefault.jpg");
                                    videos.setVideo_duration(duration);
                                    videosList.add(videos);
                                }
                            }
                            videoListAdapter=new VideoListAdapter(MainActivity.this,videosList,picasso);
                            recyclerView.setAdapter(videoListAdapter);
                            recyclerView.scrollToPosition(videosList.size()-10);
                            progressDialog.dismiss();
                        }
                    });
                } catch (final IOException e) {
                    e.printStackTrace();
                    Log.e("error2",e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"خطا در برقراری ارتباط، لطفا بعدا وارد شوید",Toast.LENGTH_LONG).show();
                        }
                    });
                    progressDialog.dismiss();
                }
            }
        }.start();
    }
}

