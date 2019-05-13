package com.javadi.youtube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.javadi.youtube.adapters.VideoListAdapter;
import com.javadi.youtube.models.Videos;
import com.javadi.youtube.server.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ImageView imgSearch;
    EditText etSearch;
    public static RecyclerView recyclerView;
    public static VideoListAdapter videoListAdapter;
    public static List<Videos> videosList=new ArrayList<>();
    List<Videos> videoList=new ArrayList<>();

    public static String key1="https://javadi.herokuapp.com/?q=https://www.googleapis.com/youtube/v3/search?part=snippet&q=";
    public static String key2="&maxResults=20&key=AIzaSyDJ7VjfdvL4KI3EhrIZXn1Mk9nzVz4a1rA";
    public static String url="http://v3-json.herokuapp.com/?query=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgSearch=(ImageView)findViewById(R.id.img_search);
        etSearch=(EditText)findViewById(R.id.et_search);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager llm=new LinearLayoutManager(MainActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        /*webView=(WebView)findViewById(R.id.web_view);
        button=(Button)findViewById(R.id.button);
        editText=(EditText)findViewById(R.id.edit_text);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());*/
        clickManager();
    }

    private void clickManager(){
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etSearch.getText().toString().equals("")){
                    //videoList=new VolleyRequest(MainActivity.this).requestVideoInfo(key1+etSearch.getText().toString()+key2);
                    //Toast.makeText(MainActivity.this,videoList.size()+"",Toast.LENGTH_LONG).show();
                    //videoListAdapter=new VideoListAdapter(MainActivity.this,videoList);
                    //recyclerView.setAdapter(videoListAdapter);
                    new VolleyRequest(MainActivity.this).requestVideoInfo(etSearch.getText().toString());
                }

            }
        });
    }


}

