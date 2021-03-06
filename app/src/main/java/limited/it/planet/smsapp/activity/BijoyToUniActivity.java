package limited.it.planet.smsapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import limited.it.planet.smsapp.R;
import limited.it.planet.smsapp.util.Constant;
import limited.it.planet.smsapp.util.FontCustomization;
import limited.it.planet.smsapp.util.SessionClear;
import me.drakeet.materialdialog.MaterialDialog;

import static limited.it.planet.smsapp.util.SaveValueSharedPreference.getValueFromSharedPreferences;
import static limited.it.planet.smsapp.util.SaveValueSharedPreference.saveBoleanValueSharedPreferences;

public class BijoyToUniActivity extends DemoActivity{
    Toolbar toolbar;
    private ProgressDialog progressBar;
    AlertDialog alertDialog;
    WebView webView;
    TextView txvHeadBjoToUni;

    String bijoyToUniAPI = "";
    String groupId = "";
    public static String TAG = "cookie";
    public static String TAG_PRO = "progress";
    ImageView imgvHome;
    MaterialDialog alert;
    FontCustomization fontCustomization;

    SessionClear sessionClear;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bijoy_to_uni);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_bijoy_to_uni);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onBackPressed();
                Intent intent = new Intent(BijoyToUniActivity.this,ToolsDashboard.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(BijoyToUniActivity.this);
            }
        });

        sessionClear = new SessionClear(BijoyToUniActivity.this);
        webView = (WebView) findViewById(R.id.webview);
        alertDialog = new AlertDialog.Builder(this).create();
      //  groupId =  getValueFromSharedPreferences("group_id",BijoyToUniActivity.this);
        bijoyToUniAPI = Constant.bijoyToUniAPI;
        txvHeadBjoToUni = (TextView)findViewById(R.id.txv_head_bjo_uni);
        fontCustomization = new FontCustomization(BijoyToUniActivity.this);
        txvHeadBjoToUni.setTypeface(fontCustomization.getTexgyreHerosBold());

        loadBijoyToWebView();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void loadBijoyToWebView(){
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true); // enable javascript
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        //webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {

//                if (progressBar.isShowing()) {
//                    progressBar.dismiss();
//
//                }
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // progressBar.isShowing();
                startProgress();
            }
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.loadData("<html>!Your Device is Offline.Please Connect Internet.</html>",  "text/html", "UTF-8");
            }
        });


        webView.loadUrl(bijoyToUniAPI);
    }


    //menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bijoy_to_uni, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Sudip: 20170212
        switch (item.getItemId()) {

            case R.id.bijoy_uni_logout:

                clearCookies(BijoyToUniActivity.this);
                saveBoleanValueSharedPreferences("islogin",false,BijoyToUniActivity.this);

                Intent intent = new Intent(BijoyToUniActivity.this,LoginActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(BijoyToUniActivity.this);
                break;


            default:
                return super.onOptionsItemSelected(item);
        }


        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            // Log.d(C.TAG, "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else
        {
            //Log.d(C.TAG, "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }
    private void startProgress() {

        progressON("Loading....");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressOFF();
            }
        }, 5000);

    }


}
