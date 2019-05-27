package com.javadi.youtube;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.javadi.youtube.adapters.LazyLoadAdapter;
import com.javadi.youtube.adapters.VideoListAdapter;
import com.javadi.youtube.models.Videos;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ImageView imgSearch;
    EditText etSearch;
    RecyclerView recyclerView;
    static VideoListAdapter videoListAdapter;
    LazyLoadAdapter lazyLoadAdapter;
    static List<Videos> videosList=new ArrayList<>();
    ProgressDialog progressDialog;
    int distributed=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgSearch=(ImageView)findViewById(R.id.img_search);
        etSearch=(EditText)findViewById(R.id.et_search);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                if(keyCode==KeyEvent.KEYCODE_ENTER){
                    search(0);
                    hideKeyboard();
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
                videosList.clear();
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
            if(distributed==0){
                try {
                    jsoupRequestVideoInfo(URLEncoder.encode(etSearch.getText().toString(),"UTF-8"),s);
                    progressDialog.setMessage("در حال دریافت اطلاعات ...");
                    progressDialog.show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                distributed=1;
            }
            else if(distributed==1){
                try {
                    jsoupRequestVideoInfo2(URLEncoder.encode(etSearch.getText().toString(),"UTF-8"),0);
                    progressDialog.setMessage("در حال دریافت اطلاعات ...");
                    progressDialog.show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                distributed=0;
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
                    document = Jsoup.connect(url).get();
                    final Elements links = document.select("a[href]");
                    final Elements link = document.select("div[class=TbwUpd]");
                    final Elements titles=document.select("h3");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0;i<link.size();i=i+1){
                                Videos videos=new Videos();
                                String temp=link.get(i).text();
                                String video_id=temp.substring(temp.indexOf("?v=")+3);
                                videos.setVideo_title(Jsoup.parse(titles.get(i).text(),"UTF-8").text());
                                videos.setVideo_id(video_id);
                                videos.setImage_url_path("https://antifilter.herokuapp.com/?q=https://img.youtube.com/vi/"+video_id+"/default.jpg");
                                videosList.add(videos);
                            }
                            videoListAdapter=new VideoListAdapter(MainActivity.this,videosList);
                            recyclerView.setAdapter(videoListAdapter);
                            recyclerView.scrollToPosition(videosList.size()-10);
                            progressDialog.dismiss();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void jsoupRequestVideoInfo2(final String query,final int s){
        new Thread(){
            @Override
            public void run() {
                //super.run();
                String url = "https://www.google.com/search?q="+query+"+site:youtube.com&tbm=vid&start="+s;
                Document document = null;
                try {
                    document = Jsoup.connect(url).get();
                    final Elements links = document.select("a[href]");
                    final Elements link = document.select("div[class=TbwUpd]");
                    final Elements titles=document.select("h3");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0;i<link.size();i=i+1){
                                Videos videos=new Videos();
                                String temp=link.get(i).text();
                                String video_id=temp.substring(temp.indexOf("?v=")+3);
                                videos.setVideo_title(Jsoup.parse(titles.get(i).text(),"UTF-8").text());
                                videos.setVideo_id(video_id);
                                videos.setImage_url_path("https://youtube-withoutfilter.herokuapp.com/?q=https://img.youtube.com/vi/"+video_id+"/default.jpg");
                                videosList.add(videos);
                            }
                            videoListAdapter=new VideoListAdapter(MainActivity.this,videosList);
                            recyclerView.setAdapter(videoListAdapter);
                            recyclerView.scrollToPosition(videosList.size()-10);
                            progressDialog.dismiss();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

