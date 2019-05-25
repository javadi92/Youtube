package com.javadi.youtube;

import android.app.ProgressDialog;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.javadi.youtube.adapters.VideoListAdapter;
import com.javadi.youtube.models.Videos;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ImageView imgSearch;
    EditText etSearch;
    RecyclerView recyclerView;
    VideoListAdapter videoListAdapter;
    List<Videos> videosList=new ArrayList<>();
    private static boolean check=true;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgSearch=(ImageView)findViewById(R.id.img_search);
        etSearch=(EditText)findViewById(R.id.et_search);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER){
                    search();
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
                search();
                hideKeyboard();
            }
        });
    }

    //hide keyboard
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void search(){
        if(!etSearch.getText().toString().equals("")){
            //new VolleyRequest(MainActivity.this).requestVideoInfo(URLEncoder.encode(etSearch.getText().toString()));
            //requestVideoInfo(URLEncoder.encode(etSearch.getText().toString()));
            //requestVideoInfoScraping(URLEncoder.encode(etSearch.getText().toString()));
            jsoupRequestVideoInfo(URLEncoder.encode(etSearch.getText().toString()));
            progressDialog.setMessage("در حال دریافت اطلاعات ...");
            progressDialog.show();
        }
    }

    public void requestVideoInfo(String query){
        String url="https://v3json.herokuapp.com/?query=";
        AndroidNetworking.get(url+query)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray=response.getJSONArray("items");
                            videosList.clear();
                            for(int i=0;i<jsonArray.length();i++){
                                Videos videos=new Videos();
                                JSONObject object1= (JSONObject) jsonArray.get(i);
                                JSONObject object_id=object1.getJSONObject("id");
                                String video_id=object_id.getString("videoId");
                                JSONObject object2=object1.getJSONObject("snippet");
                                String title=object2.getString("title");
                                JSONObject object3=object2.getJSONObject("thumbnails");
                                JSONObject object4=object3.getJSONObject("default");
                                String url_path="https://antifilter.herokuapp.com/?q="+object4.getString("url");
                                videos.setVideo_title(title);
                                videos.setVideo_id(video_id);
                                videos.setImage_url_path(url_path);
                                videosList.add(videos);
                            }
                            videoListAdapter=new VideoListAdapter(MainActivity.this,videosList);
                            recyclerView.setAdapter(videoListAdapter);
                            //progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        //progressDialog.dismiss();
                    }
                });
    }

    public void requestVideoInfoScraping(String query){
        String url="https://scrape-youtube.herokuapp.com/?search=";
        AndroidNetworking.get(url+query)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            videosList.clear();
                            for(int i=0;i<response.length();i++){
                                Videos videos=new Videos();
                                JSONArray jsonArray=response.getJSONArray(i);
                                String title=jsonArray.getString(0);
                                String video_id=jsonArray.getString(1);
                                videos.setVideo_id(video_id);
                                videos.setVideo_title(title);
                                videos.setImage_url_path("https://antifilter.herokuapp.com/?q="+"https://img.youtube.com/vi/"+video_id+"/default.jpg");
                                videosList.add(videos);
                            }
                            videoListAdapter=new VideoListAdapter(MainActivity.this,videosList);
                            recyclerView.setAdapter(videoListAdapter);
                            progressDialog.dismiss();
                        }catch (JSONException e){
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                    }
                });
    }
    public void jsoupRequestVideoInfo(final String query){
        new Thread(){
            @Override
            public void run() {
                //super.run();
                String url = "https://www.google.com/search?q="+query+"+site:youtube.com&tbm=vid&start=10";
                Document document = null;
                try {
                    videosList.clear();
                    document = Jsoup.connect(url).get();
                    final Elements links = document.select("a[href]");
                    final Elements titles=document.select("h3");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(MainActivity.this,title.toString(),Toast.LENGTH_LONG).show();
                            int count=0;
                            for(int i=0;i<links.size();i=i+2){
                                if(links.get(i).attr("href").contains("https://www.youtube.com/watch?v=")){
                                    Videos videos=new Videos();
                                    String temp=links.get(i).attr("href");
                                    videos.setVideo_title(titles.get(count).text());
                                    String video_id=temp.substring(temp.indexOf("?v=")+3);
                                    videos.setVideo_id(video_id);
                                    videos.setImage_url_path("https://antifilter.herokuapp.com/?q=https://img.youtube.com/vi/"+video_id+"/default.jpg");
                                    videosList.add(videos);
                                    count++;
                                }
                            }
                            videoListAdapter=new VideoListAdapter(MainActivity.this,videosList);
                            recyclerView.setAdapter(videoListAdapter);
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

