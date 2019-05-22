package com.javadi.youtube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.javadi.youtube.adapters.LazyLoadAdapter;
import com.javadi.youtube.adapters.VideoListAdapter;
import com.javadi.youtube.models.Videos;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgSearch=(ImageView)findViewById(R.id.img_search);
        etSearch=(EditText)findViewById(R.id.et_search);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
            requestVideoInfo(URLEncoder.encode(etSearch.getText().toString()));
        }
    }


    public void requestVideoInfo(String query){
        String url="http://v3-json.herokuapp.com/?query=";
        AndroidNetworking.get(url+query)
                .setPriority(Priority.MEDIUM)
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
                                String url_path="http://javadi.herokuapp.com/?q="+object4.getString("url");
                                videos.setVideo_title(title);
                                videos.setVideo_id(video_id);
                                videos.setImage_url_path(url_path);
                                videosList.add(videos);
                            }
                            videoListAdapter=new VideoListAdapter(MainActivity.this,videosList);
                            videoListAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(videoListAdapter);
                            check=true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //progressDialog1.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private boolean checkVideo(String video_id){
        String url2="http://fetchurl.herokuapp.com/?id=";
        AndroidNetworking.get(url2+video_id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("false")){
                            check=false;
                        }
                    }
                    @Override
                    public void onError(ANError anError) {

                    }
                });
        return check;
    }
}

