package com.javadi.youtube;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.javadi.youtube.server.VolleyRequest;

public class PlayActivity extends AppCompatActivity {

    //public static WebView webView;
    public static VideoView videoView;
    public static MediaController videoMediaController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        /*webView = (WebView) findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSaveFormData(true);*/

        videoView=(VideoView)findViewById(R.id.videoView);

        videoMediaController = new MediaController(this);
        videoMediaController.setAnchorView(videoView);
        //videoMediaController.setMediaPlayer(videoView);
        videoView.setMediaController(videoMediaController);
        videoView.requestFocus();

        String video_id=getIntent().getStringExtra("video_id");
        new VolleyRequest(this).requestVideoStream(video_id);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //webView.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;
            videoView.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;

        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //webView.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT/2;
            videoView.getLayoutParams().height= ViewGroup.LayoutParams.WRAP_CONTENT;

        }
    }
}
