package limited.it.planet.smsapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
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

import static limited.it.planet.smsapp.util.SaveValueSharedPreference.saveBoleanValueSharedPreferences;

public class NewCampActivity extends DemoActivity {
    Toolbar toolbar;
    private ProgressDialog progressBar;
    AlertDialog alertDialog;
    WebView webView;
    //ImageView imgvHome;
    MaterialDialog alert;;
    String newCampaignAPI = "";
    public static String TAG = "cookie";
    public static String TAG_PRO = "progress";
    TextView txvHeadlineToolbar;
    FontCustomization fontCustomization;

    SessionClear sessionClear;
    WebSettings webSettings ;

    private final static int FCR = 1;
    private ValueCallback<Uri[]> mUMA;
    private String mCM;
    private ValueCallback<Uri> mUM;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_camp);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);

        toolbar = (Toolbar)findViewById(R.id.toolbar_new_camp_activity);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onBackPressed();

                Intent intent = new Intent(NewCampActivity.this,SMSDashboard.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(NewCampActivity.this);
            }
        });

        initViews();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initViews(){
        webView = (WebView) findViewById(R.id.webview);
        webSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(NewCampActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }
        assert webView != null;


        sessionClear = new SessionClear(NewCampActivity.this);

        txvHeadlineToolbar = (TextView)findViewById(R.id.txv_toolbar_head);
        fontCustomization = new FontCustomization(NewCampActivity.this);

        alertDialog = new AlertDialog.Builder(this).create();
        newCampaignAPI = Constant.newCampaignsAPI;

        // listDialogue();
        txvHeadlineToolbar.setTypeface(fontCustomization.getTexgyreHerosBold());


        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(NewCampActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }
        assert webView != null;

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.setWebViewClient(new Callback());
        //webView.loadUrl("https://infeeds.com/");
        webView.loadUrl("http://client.planetgroupbd.com/sms/create-campaign");
        webView.setWebChromeClient(new WebChromeClient() {

            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                NewCampActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
            }

            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                NewCampActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FCR);
            }

            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                NewCampActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), NewCampActivity.FCR);
            }

            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {

                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }

                mUMA = filePathCallback;
                // Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");
//                Intent[] intentArray;
//
//                if (takePictureIntent != null) {
//                    intentArray = new Intent[]{takePictureIntent};
//                } else {
//                    intentArray = new Intent[0];
//                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                // chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                // chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);

                return true;
            }
        });

    }


    //menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_campaign, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Sudip: 20170212
        switch (item.getItemId()) {

            case R.id.new_campaign_logout:

                clearCookies(NewCampActivity.this);
                saveBoleanValueSharedPreferences("islogin",false,NewCampActivity.this);

                Intent intent = new Intent(NewCampActivity.this,LoginActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(NewCampActivity.this);
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
    public class Callback extends WebViewClient {
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
        }
    }
}
