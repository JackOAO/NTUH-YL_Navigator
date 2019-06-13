package eos.waypointbasedindoornavigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/*--

Module Name:

    CompassActivity.java

Abstract:

    This module emulates the embedded compass of the device
    which is used for heading correction

Author:

    Phil Wu 01-Feb-2018

--*/


import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Search_Schedule  extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchschedule);
        setTitle("台大雲林分院室內導航系統");

        Button btnOutoatient = (Button) findViewById(R.id.button_Outpatient);
        btnOutoatient.setOnClickListener(this);
        Button btnBlood_collect = (Button) findViewById(R.id.button_Blood_collect);
        btnBlood_collect.setOnClickListener(this);
        Button btnCashier = (Button) findViewById(R.id.button_Cashier);
        btnCashier.setOnClickListener(this);
        Button btnEmergency = (Button) findViewById(R.id.button_Emergency);
        btnEmergency.setOnClickListener(this);


        btnOutoatient.setBackgroundColor(Color.BLACK);
        btnOutoatient.setTextColor(Color.WHITE);

        WebView webview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("https://yldepweb.ylh.gov.tw/room_num/opd_all_view.php");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_home){
            Intent intent = new Intent();
            intent = new Intent(Search_Schedule.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Button btnOutoatient = (Button) findViewById(R.id.button_Outpatient);
        Button btnBlood_collect = (Button) findViewById(R.id.button_Blood_collect);
        Button btnCashier = (Button) findViewById(R.id.button_Cashier);
        Button btnEmergency = (Button) findViewById(R.id.button_Emergency);
        WebView webview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webview.setWebViewClient(new WebViewClient());
        switch(v.getId()){
            case R.id.button_Outpatient:
                btnOutoatient.setBackgroundColor(Color.BLACK);
                btnOutoatient.setTextColor(Color.WHITE);
                btnBlood_collect.setBackgroundColor(Color.LTGRAY);
                btnBlood_collect.setTextColor(Color.BLACK);
                btnCashier.setBackgroundColor(Color.LTGRAY);
                btnCashier.setTextColor(Color.BLACK);
                btnEmergency.setBackgroundColor(Color.LTGRAY);
                btnEmergency.setTextColor(Color.BLACK);
                webview.loadUrl("https://yldepweb.ylh.gov.tw/room_num/opd_all_view.php");
                break;
            case R.id.button_Blood_collect:
                btnOutoatient.setBackgroundColor(Color.LTGRAY);
                btnOutoatient.setTextColor(Color.BLACK);
                btnBlood_collect.setBackgroundColor(Color.BLACK);
                btnBlood_collect.setTextColor(Color.WHITE);
                btnCashier.setBackgroundColor(Color.LTGRAY);
                btnCashier.setTextColor(Color.BLACK);
                btnEmergency.setBackgroundColor(Color.LTGRAY);
                btnEmergency.setTextColor(Color.BLACK);
                webview.loadUrl("https://yldepweb.ylh.gov.tw/room_num/opd_view_reglab.php?mode=lab");
                break;
            case R.id.button_Cashier:
                btnOutoatient.setBackgroundColor(Color.LTGRAY);
                btnOutoatient.setTextColor(Color.BLACK);
                btnBlood_collect.setBackgroundColor(Color.LTGRAY);
                btnBlood_collect.setTextColor(Color.BLACK);
                btnCashier.setBackgroundColor(Color.BLACK);
                btnCashier.setTextColor(Color.WHITE);
                btnEmergency.setBackgroundColor(Color.LTGRAY);
                btnEmergency.setTextColor(Color.BLACK);
                webview.loadUrl("https://yldepweb.ylh.gov.tw/room_num/opd_view_reglab_2.php?mode=reg");
                break;
            case R.id.button_Emergency:
                btnOutoatient.setBackgroundColor(Color.LTGRAY);
                btnOutoatient.setTextColor(Color.BLACK);
                btnBlood_collect.setBackgroundColor(Color.LTGRAY);
                btnBlood_collect.setTextColor(Color.BLACK);
                btnCashier.setBackgroundColor(Color.LTGRAY);
                btnCashier.setTextColor(Color.BLACK);
                btnEmergency.setBackgroundColor(Color.BLACK);
                btnEmergency.setTextColor(Color.WHITE);
                webview.loadUrl("https://reg.ntuh.gov.tw/EmgInfoBoard/Y0NTUHEmgInfo.aspx");
                break;
        }
    }
}
