package com.overlab.overflow.tonighteat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Button btn_tr, btn_sr, btn_rv;
    private WebView mainWeb;

    final private String MAIN_URL = "http://tonightrecipe.parseapp.com/index.html";
    final private String TODAY_RECIPE = "http://tonightrecipe.parseapp.com/recipeview.html?objectId=today";
    final private String SERACH_RECIPE = "http://tonightrecipe.parseapp.com/search.html";
    final private String REVIEW_RECIPE = "http://tonightrecipe.parseapp.com/review.html";

    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri> mUploadMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, LogoActivity.class));
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        mainWeb = (WebView) findViewById(R.id.webView);
        mainWeb.getSettings().setJavaScriptEnabled(true);
        mainWeb.getSettings().setUseWideViewPort(true);
        mainWeb.getSettings().setPluginState(WebSettings.PluginState.ON);
        mainWeb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }
        });

        mainWeb.loadUrl(MAIN_URL);

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode,
                                     Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setTitle("종료").setMessage("어플을 종료할까요?").setNegativeButton("아니오", null).setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && mainWeb.canGoBack()) {

            mainWeb.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_navi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            mainWeb.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            mainWeb.setWebChromeClient(new WebChromeClient()
            {
                @SuppressWarnings("unused")
                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                    mUploadMessage = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("*/*");


                    MainActivity.this.startActivityForResult(
                            Intent.createChooser(i, "사진을 선택하세요"),
                            FILECHOOSER_RESULTCODE);
                }
            });

            btn_tr = (Button) findViewById(R.id.btn_todayrecipe);
            btn_tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mainWeb.loadUrl(TODAY_RECIPE);
                    toolbar.setTitle(R.string.todayrecipe_name);
                    drawerLayout.closeDrawers();
                }
            });

            btn_sr = (Button) findViewById(R.id.btn_searchrecipe);
            btn_sr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mainWeb.loadUrl(SERACH_RECIPE);
                    toolbar.setTitle(R.string.searchrecipe_name);
                    drawerLayout.closeDrawers();
                }
            });

            btn_rv = (Button) findViewById(R.id.btn_review);
            btn_rv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mainWeb.loadUrl(REVIEW_RECIPE);
                    toolbar.setTitle(R.string.review_name);
                    drawerLayout.closeDrawers();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
