package com.javadi.youtube.server;

import android.content.Context;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javadi.youtube.MainActivity;
import com.javadi.youtube.PlayActivity;
import com.javadi.youtube.adapters.VideoListAdapter;
import com.javadi.youtube.models.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VolleyRequest {

    Context mContext;
    RequestQueue requestQueue;
    static List<Videos> videosList=new ArrayList<>();

    public VolleyRequest(Context context){
        this.mContext=context;
    }

    public void requestVideoStream(String id){
        String url="https://fetchurl.herokuapp.com/?id=";
        StringRequest stringRequest=new StringRequest(Request.Method.GET,url+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String ur=response;
                PlayActivity.webView.loadUrl("http://javadi.herokuapp.com/?q="+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext,error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(mContext).add(stringRequest);
    }

    public void requestVideoInfo(String query){
        String url="http://v3-json.herokuapp.com/?query=";

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url+query, null, new Response.Listener<JSONObject>() {
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
                        JSONObject object4=object3.getJSONObject("high");
                        String url_path="http://javadi.herokuapp.com/?q="+object4.getString("url");
                        videos.setVideo_title(title);
                        videos.setVideo_id(video_id);
                        videos.setImage_url_path(url_path);
                        videosList.add(videos);
                    }
                    MainActivity.videoListAdapter=new VideoListAdapter(mContext,videosList);
                    MainActivity.recyclerView.setAdapter(MainActivity.videoListAdapter);
                    //Toast.makeText(mContext,videosList.size()+"",Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);
    }

}
