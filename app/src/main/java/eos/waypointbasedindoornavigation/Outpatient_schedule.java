package eos.waypointbasedindoornavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/*--

Module Name:

    OutpatienActivity.java

Abstract:
    臺大醫院門診資訊界接


Author:

    Chia-2019/08/26

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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Outpatient_schedule  extends AppCompatActivity implements View.OnClickListener {

    int API_type = 1; // 1 = "門診" ; 2 = "抽血進度"; 3 = "批價掛號進度"

    String nowtime = null;

    List<String> roomnum2_list = new ArrayList<>();
    List<String> divnname_list = new ArrayList<>();
    List<String> clinname_list = new ArrayList<>();
    List<String> drname_list = new ArrayList<>();
    List<String> callnum_list = new ArrayList<>();
    List<String> timetype_list = new ArrayList<>();
    List<String> itemname_list = new ArrayList<>();
    List<String> qnum_list = new ArrayList<>();
    List<String> counter_list = new ArrayList<>();
    private ListView listV;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outpatient);
        setTitle("臺大雲林分院室內導航系統");
        //Findview by ID
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

        //檢測網路權限
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) { //網路有開啟，檢查擷取php版本
            new Thread(runnable).start();//啟動執行序runnable
        }


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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_home) {
            Intent intent = new Intent();
            intent = new Intent(Outpatient_schedule.this, MainActivity.class);
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
        switch (v.getId()) {
            case R.id.button_Outpatient:
                //選單顏色更換
                btnOutoatient.setBackgroundColor(Color.BLACK);
                btnOutoatient.setTextColor(Color.WHITE);
                btnBlood_collect.setBackgroundColor(Color.LTGRAY);
                btnBlood_collect.setTextColor(Color.BLACK);
                btnCashier.setBackgroundColor(Color.LTGRAY);
                btnCashier.setTextColor(Color.BLACK);
                btnEmergency.setBackgroundColor(Color.LTGRAY);
                btnEmergency.setTextColor(Color.BLACK);
                API_type = 1;


                break;
            case R.id.button_Blood_collect:
                ////選單顏色更換
                btnOutoatient.setBackgroundColor(Color.LTGRAY);
                btnOutoatient.setTextColor(Color.BLACK);
                btnBlood_collect.setBackgroundColor(Color.BLACK);
                btnBlood_collect.setTextColor(Color.WHITE);
                btnCashier.setBackgroundColor(Color.LTGRAY);
                btnCashier.setTextColor(Color.BLACK);
                btnEmergency.setBackgroundColor(Color.LTGRAY);
                btnEmergency.setTextColor(Color.BLACK);
                API_type = 2;
                break;
            case R.id.button_Cashier:
                ////選單顏色更換
                btnOutoatient.setBackgroundColor(Color.LTGRAY);
                btnOutoatient.setTextColor(Color.BLACK);
                btnBlood_collect.setBackgroundColor(Color.LTGRAY);
                btnBlood_collect.setTextColor(Color.BLACK);
                btnCashier.setBackgroundColor(Color.BLACK);
                btnCashier.setTextColor(Color.WHITE);
                btnEmergency.setBackgroundColor(Color.LTGRAY);
                btnEmergency.setTextColor(Color.BLACK);
                API_type = 3;
                break;
            case R.id.button_Emergency:
                ////選單顏色更換
                btnOutoatient.setBackgroundColor(Color.LTGRAY);
                btnOutoatient.setTextColor(Color.BLACK);
                btnBlood_collect.setBackgroundColor(Color.LTGRAY);
                btnBlood_collect.setTextColor(Color.BLACK);
                btnCashier.setBackgroundColor(Color.LTGRAY);
                btnCashier.setTextColor(Color.BLACK);
                btnEmergency.setBackgroundColor(Color.BLACK);
                btnEmergency.setTextColor(Color.WHITE);
                break;

        }
        //檢測網路權限
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) { //網路有開啟，檢查擷取php版本
            new Thread(runnable).start();//啟動執行序runnable
        }
    }

    //PHP
    @SuppressLint("HandlerLeak")
    Handler handler_Success = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("key");//取出key中的字串存入val
            Toast.makeText(getApplicationContext(), val, Toast.LENGTH_LONG).show();
            //門診API
            if(API_type == 1) {
                try {
                    nowtime = getVersionFormphp(val);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("xxx_all_nowtime", "" + nowtime);
                parseJSONWithJSONObject(nowtime);
            }else if(API_type == 2 || API_type == 3){//掛號與抽血共用相同API，用split區分兩個字串
                String[] sArraylist = val.split("\\|");
                String lab = null, reg = null;
                int i = 0;
                for(String token:sArraylist) {
                    if (i == 0) {
                        reg = token;
                        i++;
                    }else if (i == 1) {
                        lab = token;
                    }
                }
                //reg 存 掛號資訊，lab存檢驗醫學部資訊
                Log.i("aaa45645", "reg" + reg);
                Log.i("aaa45645","lab" + lab);
                if(API_type == 2){
                    val = lab;
                }else if(API_type == 3){
                    val = reg;
                }
                try {
                    getjsonFormphp(val);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("xxx_all_nowtime", "" + nowtime);
                //建立ListView
                CreateListView();
            }
        }

    };

    Handler handler_Error = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("key");
            Log.i("xxx_update", "" + val);
        }
    };

    Handler handler_Nodata = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("key");
            Log.i("xxx_update", "" + val);
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //
            // TODO: http request.
            //
            Message msg = new Message();
            Bundle data = new Bundle();
            msg.setData(data);
            if(API_type == 1) {
                try {
                    URL url = new URL("http://yldepweb.ylh.gov.tw/room_num/opd_json.php");
                    HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
                    mUrlConnection.setDoInput(true);

                    InputStream is = new BufferedInputStream(mUrlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

                    String tempStr;
                    StringBuffer stringBuffer = new StringBuffer();

                    while ((tempStr = bufferedReader.readLine()) != null) {
                        stringBuffer.append(tempStr);
                    }
                    bufferedReader.close();
                    is.close();

                    String responseString = stringBuffer.toString();

                    if (responseString != null) {
                        data.putString("key", responseString);//如果成功將網頁內容存入key
                        handler_Success.sendMessage(msg);
                    } else {
                        data.putString("key", "無資料");
                        handler_Nodata.sendMessage(msg);
                    }
                } catch (Exception e) {
                    data.putString("key", "連線失敗");
                    handler_Error.sendMessage(msg);
                }
            }else if(API_type == 3 || API_type == 2)
                try {
                    URL url = new URL("http://yldepweb.ylh.gov.tw/room_num/reg_lab_json.php");
                    HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
                    mUrlConnection.setDoInput(true);

                    InputStream is = new BufferedInputStream(mUrlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

                    String tempStr;
                    StringBuffer stringBuffer = new StringBuffer();

                    while ((tempStr = bufferedReader.readLine()) != null) {
                        stringBuffer.append(tempStr);
                    }
                    bufferedReader.close();
                    is.close();

                    String responseString = stringBuffer.toString();

                    if (responseString != null) {
                        data.putString("key", responseString);//如果成功將網頁內容存入key
                        handler_Success.sendMessage(msg);
                    } else {
                        data.putString("key", "無資料");
                        handler_Nodata.sendMessage(msg);
                 }
                } catch (Exception e) {
                    data.putString("key", "連線失敗");
                    handler_Error.sendMessage(msg);
                }
            }
    };

    public String getVersionFormphp(String all) throws JSONException {

        JSONObject jsonObject = new JSONObject(all);
        Log.i("jsonOBJ ", "JSONOBJ = " + jsonObject);
        String version = jsonObject.getString("opd_all");

        return version;
    }
    public void getjsonFormphp(String all) throws JSONException {

        JSONObject jsonObject = new JSONObject(all);
        int i;
        String counter1_list = null;
        itemname_list.clear();
        qnum_list.clear();
        counter_list.clear();
        for(i = 1; i < 7 ;i++) {//標籤為1～6
            counter1_list = null;
            try {
                Log.i("executetime", "" + i);
                String x = String.valueOf(i);
                counter1_list = jsonObject.getString(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("executetime", "counterlist1 = " + counter1_list);
            if(counter1_list != null){//建立List清單
                Log.i("xxx456", "counterlist1 = " + counter1_list);
                JSONObject jsonObject2 = new JSONObject(counter1_list);
                String item1 = jsonObject2.getString("itemname");
                String qnum1 = jsonObject2.getString("qnum");
                String counter1 = jsonObject2.getString("counter");
                itemname_list.add(item1);
                qnum_list.add(qnum1);
                counter_list.add(counter1);
            }
        }

        return;
    }

    public void CreateListView(){
        //將Array資訊傳給Adapter並建立List
        List<outpatient_list_class> outpatient_list = new ArrayList<outpatient_list_class>();
        listV = (ListView) findViewById(R.id.List_View_outpaient);
        adapter = new MyAdapter(Outpatient_schedule.this, outpatient_list);
        listV.setAdapter(adapter);
        outpatient_list.add(new outpatient_list_class("型態", "", "櫃檯號碼", "", "現在號碼"));
        for (int i = 0; i < itemname_list.size(); i++) {
            outpatient_list.add(new outpatient_list_class(itemname_list.get(i),null, counter_list.get(i), null, qnum_list.get(i)));
        }
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    if(API_type == 2)
                    {
                        Intent intent = new Intent(Outpatient_schedule.this, NavigationActivity.class);
                        intent.putExtra("destinationName", "檢驗醫學部");
                        intent.putExtra("destinationID", "00000015-0000-0000-0505-000000000505");
                        intent.putExtra("destinationRegion", "region1");
                        startActivity(intent);
                        finish();
                    }
                    else if(API_type == 3)
                    {
                        Intent intent = new Intent(Outpatient_schedule.this, NavigationActivity.class);
                        intent.putExtra("destinationName", "批價掛號櫃臺");
                        intent.putExtra("destinationID", "00000015-0000-0010-1011-000000101011");
                        intent.putExtra("destinationRegion", "region1");
                        startActivity(intent);
                        finish();
                    }

                }
            }
        });


        return;

    }

    private void parseJSONWithJSONObject(String JsonData) {
            List<outpatient_list_class> outpatient_list = new ArrayList<outpatient_list_class>();
            listV = (ListView) findViewById(R.id.List_View_outpaient);
            adapter = new MyAdapter(Outpatient_schedule.this, outpatient_list);
            listV.setAdapter(adapter);
            try {
                Log.i("xxx_all", "JsonData" + JsonData);
                JSONArray jsonArray = new JSONArray(JsonData);
                Log.i("xxx_all", "JsonDataSize" + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    //診間號碼
                    String roomnum2 = jsonArray.getJSONObject(i).getString("roomnum2");
                    roomnum2_list.add(roomnum2);
                    //部
                    String divnname = jsonArray.getJSONObject(i).getString("divnname");
                    divnname_list.add(divnname);
                    //科
                    String clinname = jsonArray.getJSONObject(i).getString("clinname");
                    clinname_list.add(clinname);
                    //醫師姓名
                    String drname = jsonArray.getJSONObject(i).getString("drname");
                    drname_list.add(drname);
                    //目前燈號
                    String callnum = jsonArray.getJSONObject(i).getString("callnum");
                    callnum_list.add(callnum);
                    //門診時段
                    String timetype = jsonArray.getJSONObject(i).getString("timetype");
                    timetype_list.add(timetype);
                }
                outpatient_list.add(new outpatient_list_class("診間號碼", "部別名稱", "科別名稱", "醫生姓名", "目前叫號"));
                for (int i = 0; i < roomnum2_list.size(); i++) {
                    outpatient_list.add(new outpatient_list_class(roomnum2_list.get(i), divnname_list.get(i), clinname_list.get(i), drname_list.get(i), callnum_list.get(i)));
                }
                listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //讀診間號碼判斷目的地
                        if (position != 0) {
                            int i = Integer.parseInt(roomnum2_list.get(position - 1));
                            if (i < 11) { // 去1~10診
                                Intent intent = new Intent(Outpatient_schedule.this, NavigationActivity.class);
                                intent.putExtra("destinationName", "心臟內科/內科/體檢區(1~10診)");
                                intent.putExtra("destinationID", "00000015-0000-0010-1002-000000101002");
                                intent.putExtra("destinationRegion", "region1");
                                startActivity(intent);
                                finish();
                            } else if (11 <= i && i < 20) {//去11~20診
                                Intent intent = new Intent(Outpatient_schedule.this, NavigationActivity.class);
                                intent.putExtra("destinationName", "耳鼻喉科/小兒科/婦產科(11~20診)");
                                intent.putExtra("destinationID", "00000015-0000-0010-1014-000000101014");
                                intent.putExtra("destinationRegion", "region1");
                                startActivity(intent);
                                finish();
                            } else if (21 <= i && i < 25) {//去21~25診
                                Intent intent = new Intent(Outpatient_schedule.this, NavigationActivity.class);
                                intent.putExtra("destinationName", "腎臟科/腎膽腸內科/新陳代謝分泌科(21~25診)");
                                intent.putExtra("destinationID", "00000015-0000-0010-1022-000000101022");
                                intent.putExtra("destinationRegion", "region1");
                                startActivity(intent);
                                finish();
                            } else if (26 <= i && i < 29) {//去26~29診
                                Intent intent = new Intent(Outpatient_schedule.this, NavigationActivity.class);
                                intent.putExtra("destinationName", "精神科/神經內科(26~29診)");
                                intent.putExtra("destinationID", "00000015-0000-0010-1012-000000101012");
                                intent.putExtra("destinationRegion", "region1");
                                startActivity(intent);
                                finish();
                            } else if (30 <= i && i < 41) {//去30~41診
                                Intent intent = new Intent(Outpatient_schedule.this, NavigationActivity.class);
                                intent.putExtra("destinationName", "外科/骨科/牙科(30~41診)");
                                intent.putExtra("destinationID", "00000015-0000-0010-1020-000000101020");
                                intent.putExtra("destinationRegion", "region1");
                                startActivity(intent);
                                finish();
                            } else if (42 <= i && i < 49) {//去42~49診
                                Intent intent = new Intent(Outpatient_schedule.this, NavigationActivity.class);
                                intent.putExtra("destinationName", "眼科/皮膚科(42~49診)");
                                intent.putExtra("destinationID", "00000015-0000-0010-1026-000000101026");
                                intent.putExtra("destinationRegion", "region1");
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}


