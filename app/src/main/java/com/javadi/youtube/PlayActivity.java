package com.javadi.youtube;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.Calendar;

//import com.javadi.youtube.server.VolleyRequest;

public class PlayActivity extends AppCompatActivity {

    public static WebView webView;
    CatLoadingView mView;
    //public static VideoView videoView;
    //public static MediaController videoMediaController;
    public static ProgressDialog progressDialog2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        mView = new CatLoadingView();
        mView.setCanceledOnTouchOutside(false);
        mView.setText("> > > > > > > > > > > > > > > ");
        mView.setShowsDialog(false);
        mView.show(getSupportFragmentManager(), "");
        //progressDialog2 = new ProgressDialog(this);
        //progressDialog2.setMessage("در حال دریافت اطلاعات ...");
        //progressDialog2.show();

        webView = (WebView) findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //progressDialog2.dismiss();
                mView.dismiss();
            }

        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //increase speed performance of webview
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setEnableSmoothTransition(true);



        /*videoView=(VideoView)findViewById(R.id.videoView);

        videoMediaController = new MediaController(this);
        videoMediaController.setAnchorView(videoView);
        //videoMediaController.setMediaPlayer(videoView);
        videoView.setMediaController(videoMediaController);
        videoView.requestFocus();*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }*/

        String video_id=getIntent().getStringExtra("video_id");
        //new VolleyRequest(this).requestVideoStream(video_id);

        //distribute loade on servers base on hour time
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        if(currentHour>=0 && currentHour<8){
            requestVideoStream(video_id);
            //Toast.makeText(PlayActivity.this,currentHour+"",Toast.LENGTH_LONG).show();
        }
        else if(currentHour>=8 && currentHour<16){
            requestVideoStream2(video_id);
        }
        else if(currentHour>=16 && currentHour<24){
            requestVideoStream3(video_id);
            //Toast.makeText(PlayActivity.this,currentHour+"",Toast.LENGTH_LONG).show();
        }

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            webView.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;
        }



        //videoView.start();

        /*videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialog2.dismiss();
            }
        });*/

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            webView.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;
            //videoView.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;

        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            webView.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT/2;
            //videoView.getLayoutParams().height= ViewGroup.LayoutParams.WRAP_CONTENT;

        }
    }

    private void requestVideoStream(final String query){
        final String url="https://fetchurls.herokuapp.com/?id=";
        AndroidNetworking.get(url+query)
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        webView.loadUrl(response);
                        /*if(response.equals("error") || response.equals("") || response.equals(null)){
                            webView.loadUrl("https://antifilter.herokuapp.com/?q=https://prx3.genmirror.com/embed/embed.php?vid="+query);
                            //https://sitenable.asia
                            //Toast.makeText(PlayActivity.this,"خطا در بارگذاری ویدئو",Toast.LENGTH_LONG).show();
                            //webView.destroy();
                            //PlayActivity.this.finish();;
                        }
                        else{
                            webView.loadUrl("https://antifilter.herokuapp.com/?q="+response);
                        }*/
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog2.dismiss();
                    }
                });
    }

    private void requestVideoStream2(final String query){
        final String url="https://fetchurls2.herokuapp.com/?id=";
        AndroidNetworking.get(url+query)
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        webView.loadUrl(response);
                        /*if(response.equals("error") || response.equals("") || response.equals(null)){
                            webView.loadUrl("https://antifilter2.herokuapp.com/?q=https://prx3.genmirror.com/embed/embed.php?vid="+query);
                            //https://sitenable.asia
                            //Toast.makeText(PlayActivity.this,"خطا در بارگذاری ویدئو",Toast.LENGTH_LONG).show();
                            //webView.destroy();
                            //PlayActivity.this.finish();;
                        }
                        else{
                            webView.loadUrl("https://antifilter2.herokuapp.com/?q="+response);
                        }*/
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog2.dismiss();
                    }
                });
    }

    private void requestVideoStream3(final String query){
        final String url="https://url-fetch.herokuapp.com/?id=";
        AndroidNetworking.get(url+query)
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        webView.loadUrl(response);
                        /*if(response.equals("error") || response.equals("") || response.equals(null)){
                            webView.loadUrl("https://youtube-withoutfilter.herokuapp.com/index.php?q=https://prx3.genmirror.com/embed/embed.php?vid="+query);
                            //https://sitenable.asia
                            //Toast.makeText(PlayActivity.this,"خطا در بارگذاری ویدئو",Toast.LENGTH_LONG).show();
                            //webView.destroy();
                            //PlayActivity.this.finish();;
                        }
                        else{
                            webView.loadUrl("https://youtube-withoutfilter.herokuapp.com/index.php?q="+response);
                        }*/
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog2.dismiss();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.destroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
