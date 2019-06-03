package eos.waypointbasedindoornavigation;

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


public class InitDirectionImage extends AppCompatActivity{
    private ImageView image;
    int passedDegree;
    String nowWaypointID;
    String nextWaypointID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_direction_image);
        image = (ImageView) findViewById(R.id.init_image);
        TextView faceto = (TextView) findViewById(R.id.faceto);
        //getpassDegree
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            nowWaypointID = bundle.getString("nowID");
            nextWaypointID = bundle.getString("nextID");
        }
        Log.i("degree","degree = " + passedDegree);
        //find ID

        Context appContext = GetApplicationContext.getAppContext();
        SharedPreferences languagePref = PreferenceManager.getDefaultSharedPreferences(appContext);
        String language_option = languagePref.getString("language","繁體中文");
        if(language_option.equals("繁體中文")) {
            setTitle("台大雲林分院室內導航系統");
            faceto.setText("請面向圖中方向開始導航");
        }else if(language_option.equals("English")){
            setTitle("NTUH - Yunlin");
            faceto.setText("Please face to the direction in picture and start navugation.");
        }

        Log.i("test123", nowWaypointID + " next " + nextWaypointID);

        switch (nowWaypointID) {
            case "0xf853bd410xfe54f142": //A1(1~10診走廊出口)
                if (nextWaypointID.equals("0x6029b8410x580af042") || nextWaypointID.equals("0x0f3eb8410x3921f342")) //心臟內科/內科/體檢區(A2 & A3)
                    image.setImageResource(R.drawable.a1_1);
                break;
            case "0x6029b8410x580af042": //A2(心臟內科/內科/體檢區)
                if (nextWaypointID.equals("0xbf52c8410x3323f542") || nextWaypointID.equals("0xeb57ca410x0e21f342")) //服務台(A4 & A5)
                    image.setImageResource(R.drawable.a2_1);
                else if (nextWaypointID.equals("0xf853bd410xfe54f142"))
                    image.setImageResource(R.drawable.a2_2);
                break;
            case "0x0f3eb8410x3921f342": //A3(心臟內科/內科/體檢區)
                if (nextWaypointID.equals("0xbf52c8410x3323f542") || nextWaypointID.equals("0xeb57ca410x0e21f342")) //服務台(A4 & A5)
                    image.setImageResource(R.drawable.a3_1);
                else if (nextWaypointID.equals("0xf853bd410xfe54f142"))
                    image.setImageResource(R.drawable.a2_2);
                break;
            case "0xbf52c8410x3323f542": //A4(服務台)
                if (nextWaypointID.equals("0x12f6af410x5442f242") || nextWaypointID.equals("0x4b81ca410x0e21f342")) //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a4_1);
                else if (nextWaypointID.equals("0xff53bd410x0055f142") || nextWaypointID.equals("0xbb3fc8410x0721f342")) //健康教育中心(A16 & A17)
                    image.setImageResource(R.drawable.a4_2);
                else if (nextWaypointID.equals("0x6029b8410x580af042") || nextWaypointID.equals("0x0f3eb8410x3921f342")) //心臟內科/內科/體檢區(A2 & A3)
                    image.setImageResource(R.drawable.a4_3);
                break;
            case "0xeb57ca410x0e21f342": //A5(服務台)
                if (nextWaypointID.equals("0x12f6af410x5442f242") || nextWaypointID.equals("0x4b81ca410x0e21f342")) //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a4_1);
                else if (nextWaypointID.equals("0xff53bd410x0055f142") || nextWaypointID.equals("0xbb3fc8410x0721f342")) //健康教育中心(A16 & A17)
                    image.setImageResource(R.drawable.a4_2);
                else if (nextWaypointID.equals("0x6029b8410x580af042") || nextWaypointID.equals("0x0f3eb8410x3921f342")) //心臟內科/內科/體檢區(A2 & A3)
                    image.setImageResource(R.drawable.a4_3);
                break;
            case "0xfa53bd410xff54f142": //A6 大門
                if (nextWaypointID.equals("0x12f6af410x5442f242") || nextWaypointID.equals("0x4b81ca410x0e21f342")) //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a6_1);
                else if (nextWaypointID.equals("0xbf52c8410x3323f542") || nextWaypointID.equals("0xeb57ca410x0e21f342")) //服務台(A4 & A5)
                    image.setImageResource(R.drawable.a6_2);
                break;
            case "0xeaaac2410xc1a9f042": //A7 大門
                if (nextWaypointID.equals("0x12f6af410x5442f242") || nextWaypointID.equals("0x4b81ca410x0e21f342")) //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a7_1);
                break;
            case "0x12f6af410x5442f242": //A8 中央走廊
                if (nextWaypointID.equals("0xcf90b8410x3424f042")) //A11(批價櫃檯)
                    image.setImageResource(R.drawable.a8_1);
                else if (nextWaypointID.equals("0x4d36b9410x934df042")) //A18(樓梯)
                    image.setImageResource(R.drawable.a7_1);
                else if (nextWaypointID.equals("0xbf52c8410x3323f542") || nextWaypointID.equals("0xeb57ca410x0e21f342")) //服務台(A4 & A5)
                    faceto.setText("台大基層聯合示範中心");
                break;
            case "0x0400c8410x0721f342": //A9大門
                if (nextWaypointID.equals("0x4b81ca410x0e21f342"))  //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a9_1);
                break;
            case "0x4b81ca410x0e21f342": //A10 中央走廊(自動繳費機)
                if (nextWaypointID.equals("0xcf90b8410x3424f042")) //A11(批價櫃檯)
                    image.setImageResource(R.drawable.a8_1);
                else if (nextWaypointID.equals("0x4d36b9410x934df042")) //A18(樓梯)
                    image.setImageResource(R.drawable.a7_1);
                else if (nextWaypointID.equals("0xbf52c8410x3323f542") || nextWaypointID.equals("0xeb57ca410x0e21f342")) //服務台(A4 & A5)
                    faceto.setText("請面對台大基層聯合示範中心");
                break;
            case "0xcf90b8410x3424f042": //A11批價
                if (nextWaypointID.equals("0x0154bd410x0055f142")) //A12(30~41診走廊交叉口)
                    image.setImageResource(R.drawable.a11_1);
                else if (nextWaypointID.equals("0x0154bd410x0055f142")) //A19(41診走廊交叉口)
                    image.setImageResource(R.drawable.a11_2);
                if (nextWaypointID.equals("0x12f6af410x5442f242") || nextWaypointID.equals("0x4b81ca410x0e21f342")) //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a11_3);
                break;
            case "0x2ebab8410x8c2ef042": // A12 精神科/神經內科
                if (nextWaypointID.equals("0xdeceb8410xb833f042")) //A13(26~29診走廊出口)
                    faceto.setText("請面對26~29診走廊出口");
                else if (nextWaypointID.equals("0xcf90b8410x3424f042")) //A11批價
                    faceto.setText("請面對醫院大廳");
                break;
            case "0xdeceb8410xb833f042": //A13 26~29診走廊出口
                if (nextWaypointID.equals("0x2ebab8410x8c2ef042")) //精神科
                    image.setImageResource(R.drawable.a13_1);
                break;
            case "0x3ef8b8410x0f3ef042": //A14耳鼻喉科
                if (nextWaypointID.equals("0xff53bd410x0055f142") || nextWaypointID.equals("0xbb3fc8410x0721f342")) //健康教育中心(A16 & A17)
                    image.setImageResource(R.drawable.a14_1);
                break;
            case "0xee0cb9410x3b43f042": //A15耳鼻喉科
                if (nextWaypointID.equals("0xff53bd410x0055f142") || nextWaypointID.equals("0xbb3fc8410x0721f342"))  //健康教育中心(A16 & A17)
                    image.setImageResource(R.drawable.a15_1);
                break;
            case "0xff53bd410x0055f142": //A16健康教育中心
                if (nextWaypointID.equals("0xbf52c8410x3323f542") || nextWaypointID.equals("0xeb57ca410x0e21f342")) //服務台(A4 & A5)
                    image.setImageResource(R.drawable.a16_1);
                else if (nextWaypointID.equals("0x1cc7b9410xc771f042")) //無障礙領藥窗口 (A23)
                    image.setImageResource(R.drawable.a16_2);
                else if (nextWaypointID.equals("0x3ef8b8410x0f3ef042") || nextWaypointID.equals("0xee0cb9410x3b43f042")) //耳鼻喉科(A14 & A15)
                    image.setImageResource(R.drawable.a16_3);
                break;
            case "0xbb3fc8410x0721f342": //A17健康教育中心
                if (nextWaypointID.equals("0xbf52c8410x3323f542") || nextWaypointID.equals("0xeb57ca410x0e21f342")) //服務台(A4 & A5)
                    image.setImageResource(R.drawable.a16_1);
                else if (nextWaypointID.equals("0x1cc7b9410xc771f042")) //無障礙領藥窗口 (A23)
                    image.setImageResource(R.drawable.a16_2);
                else if (nextWaypointID.equals("0x3ef8b8410x0f3ef042") || nextWaypointID.equals("0xee0cb9410x3b43f042")) //耳鼻喉科(A14 & A15)
                    image.setImageResource(R.drawable.a17_3);
                break;
            case "0x4d36b9410x934df042": //A18 樓梯
                if (nextWaypointID.equals("0x12f6af410x5442f242") || nextWaypointID.equals("0x4b81ca410x0e21f342")) //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a18_1);
                else if (nextWaypointID.equals(" 0x0154bd410x0055f142"))  //A19 30~41診走廊交叉口
                    image.setImageResource(R.drawable.a18_2);
                else if (nextWaypointID.equals("0x0454bd410x0155f142")) //A24 (領藥處大廳)
                    image.setImageResource(R.drawable.a18_3);
                else if (nextWaypointID.equals("0x0193bd410x780df142")) //B1 (B1樓梯)
                    image.setImageResource(R.drawable.a18_s);
                break;
            case "0x0154bd410x0055f142": //A19 30~41診走廊交叉口
                Log.i("test123", "1");
                if (nextWaypointID.equals("0xcf90b8410x3424f042")) //A11 批價
                    image.setImageResource(R.drawable.a19_1);
                else if (nextWaypointID.equals("0x0254bd410x0055f142") || nextWaypointID.equals("0x0254bd410x0155f142")) //外科/骨科/牙科(A20 & A21)
                    image.setImageResource(R.drawable.a19_2);
                else if (nextWaypointID.equals("0x7cf0b9410x1f7cf042")) //42~49診走廊交叉口
                    image.setImageResource(R.drawable.a19_3);
                else if(nextWaypointID.equals("0x4d36b9410x934df042")) //樓梯
                    image.setImageResource(R.drawable.a19_4);
                break;
            case "0x0254bd410x0055f142": //A20 外科/骨科/牙科
                if(nextWaypointID.equals("0x0154bd410x0055f142")) //A19 30~41診走廊交叉口
                    image.setImageResource(R.drawable.a20_1);
                break;
            case "0x0254bd410x0155f142": //A21 外科/骨科/牙科
                if(nextWaypointID.equals("0x0154bd410x0055f142")) //A19 30~41診走廊交叉口
                    image.setImageResource(R.drawable.a21_1);
                break;
            case "0xed4cc8410x0e21f342": //A22 腎臟科/腎膽腸內科/新陳代謝分泌科
                if(nextWaypointID.equals("0x1cc7b9410xc771f042")) //A23 無障礙領藥窗口
                    image.setImageResource(R.drawable.a22_1);
                break;
            case "0x1cc7b9410xc771f042": //A23 無障礙領藥窗口
                if(nextWaypointID.equals("0xff53bd410x0055f142") || nextWaypointID.equals("0xbb3fc8410x0721f342")) //A16 & A17 健康教育中心
                    image.setImageResource(R.drawable.a23_1);
                else if(nextWaypointID.equals("0x0454bd410x0155f142")) // A24 領藥處大廳
                    image.setImageResource(R.drawable.a23_2);
                else if(nextWaypointID.equals("0xed4cc8410x0e21f342"))  //A22 腎臟科/腎膽腸內科/新陳代謝分泌科
                    image.setImageResource(R.drawable.a23_3);
                break;
            case "0x0454bd410x0155f142": // A24 領藥處大廳
                if(nextWaypointID.equals("0x4d36b9410x934df042")) // A18 樓梯
                    image.setImageResource(R.drawable.a24_1);
                else if(nextWaypointID.equals("0x7cf0b9410x1f7cf042")) //A25 42~49診走廊交叉口
                    image.setImageResource(R.drawable.a24_2);
                else if(nextWaypointID.equals("0x4993bd410x640df142") || nextWaypointID.equals("0x6793bd410x5d0df142")) //A29 & A30 超商
                    image.setImageResource(R.drawable.a24_3);
                else if(nextWaypointID.equals("0x1cc7b9410xc771f042")) // A23 無障礙領藥窗口
                    image.setImageResource(R.drawable.a24_4);
                break;
            case "0x7cf0b9410x1f7cf042": //A25 42~49診走廊交叉口
                if(nextWaypointID.equals("0x0154bd410x0055f142")) //A19 30~41診交叉口
                    image.setImageResource(R.drawable.a25_1);
                else if(nextWaypointID.equals("0x2c05ba410x4b81f042") || nextWaypointID.equals("0xdc19ba410x7786f042")) //A26 & A27 眼科/皮膚科
                    image.setImageResource(R.drawable.a25_2);
                else if(nextWaypointID.equals("0x0454bd410x0155f142")) // A24 領藥處大廳
                    image.setImageResource(R.drawable.a25_3);
                break;
            case "0x2c05ba410x4b81f042": //A26 眼科/皮膚科
                if(nextWaypointID.equals("0x8b2eba410xa38bf042")) //A28 42~49診走廊出口
                    image.setImageResource(R.drawable.a26_1);
                else if(nextWaypointID.equals("0x7cf0b9410x1f7cf042")) //A25 42~49診走廊交叉口
                    image.setImageResource(R.drawable.a26_2);
                break;
            case "0xdc19ba410x7786f042": //A27 眼科/皮膚科
                if(nextWaypointID.equals("0x8b2eba410xa38bf042")) //A28 42~49診走廊出口
                    image.setImageResource(R.drawable.a26_1);
                else if(nextWaypointID.equals("0x7cf0b9410x1f7cf042")) //A25 42~49診走廊交叉口
                    image.setImageResource(R.drawable.a27_2);
                break;
            case "0x8b2eba410xa38bf042": //A28 42~49診走廊出口
                if(nextWaypointID.equals("0x2c05ba410x4b81f042") || nextWaypointID.equals("0xdc19ba410x7786f042")) //A27 & A28眼科/皮膚科
                    image.setImageResource(R.drawable.a28_1);
                break;
            case "0x4993bd410x640df142": //A29 超商
                if(nextWaypointID.equals("0x0454bd410x0155f142"))  //A24 領藥處大廳
                    image.setImageResource(R.drawable.a29_1);
                else if(nextWaypointID.equals("0x283ff0420x00000000"))  //C1 新舊大樓連接走廊
                    faceto.setText("請面對舊醫療大樓");
                break;
            case "0x6793bd410x5d0df142": //A30 超商
                if(nextWaypointID.equals("0x0454bd410x0155f142"))  //A24 領藥處大廳
                    image.setImageResource(R.drawable.a29_1);
                else if(nextWaypointID.equals("0x283ff0420x00000000"))  //C1 新舊大樓連接走廊
                    faceto.setText("請面對舊醫療大樓");
                break;
            case "0x0193bd410x780df142": //B1 樓梯
                if(nextWaypointID.equals("0x5c93bd410x4f0df142")) //B3 X光報到處
                    image.setImageResource(R.drawable.b1_2);
                else if(nextWaypointID.equals("0x4d36b9410x934df042")) //A18 (1F樓梯)
                    faceto.setText("請面對樓梯");
                break;
            case "0x5519b8410x5506f042": //B2 電梯
                if(nextWaypointID.equals("0x0193bd410x780df142"))  //B1 樓梯
                    image.setImageResource(R.drawable.b2_1);
                break;
            case "0x5c93bd410x4f0df142": //B3 X光報到處
                if(nextWaypointID.equals("0x0193bd410x780df142"))  //B1 樓梯
                    image.setImageResource(R.drawable.b3_1);
                break;
            case "0x283ff0420x00000000": //C1 新舊大樓連接走廊
                if(nextWaypointID.equals("0x4993bd410x640df142") || nextWaypointID.equals("0x6793bd410x5d0df142")) //A29 & A30 超商
                    image.setImageResource(R.drawable.c1_1);
                else if(nextWaypointID.equals("0xf295c2410x63a8f042") || nextWaypointID.equals("0x8193bd410x540df142")) //C2 & C3 樓梯
                    image.setImageResource(R.drawable.c1_2);
                break;
            case "0xf295c2410x63a8f042": //C2 樓梯
                if(nextWaypointID.equals("0x283ff0420x00000000")) //C1 新舊大樓連接走廊
                    image.setImageResource(R.drawable.c2_1);
                else if(nextWaypointID.equals("0xde57c8410x0721f342")) //C5 抽血
                    image.setImageResource(R.drawable.c2_2);
                else if(nextWaypointID.equals("0xc43af3420x00000000")) //C4 核子醫學部
                    image.setImageResource(R.drawable.c2_3);
                else if(nextWaypointID.equals("0x3319b8410x4d06f042")) //D1 2F樓梯
                    image.setImageResource(R.drawable.c2_s);
                break;
            case "0x8193bd410x540df142": //C3 樓梯
                if(nextWaypointID.equals("0x283ff0420x00000000")) //C1 新舊大樓連接走廊
                    image.setImageResource(R.drawable.c2_1);
                else if(nextWaypointID.equals("0xde57c8410x0721f342")) //C5 抽血
                    image.setImageResource(R.drawable.c2_2);
                else if(nextWaypointID.equals("0xc43af3420x00000000")) //C4 核子醫學部
                    image.setImageResource(R.drawable.c2_3);
                else if(nextWaypointID.equals("0x3319b8410x4d06f042")) //D1 2F樓梯
                    image.setImageResource(R.drawable.c2_s);
                break;
            case "0xc43af3420x00000000": //C4 核子醫學部
                if(nextWaypointID.equals("0xf295c2410x63a8f042") || nextWaypointID.equals("0x8193bd410x540df142")) //C2 & C3 樓梯
                    image.setImageResource(R.drawable.c4_1);
                else if(nextWaypointID.equals("0xde57c8410x0721f342")) //C5 抽血
                    image.setImageResource(R.drawable.c4_2);
                else if(nextWaypointID.equals("0x553bc2410x2d44f042") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                    image.setImageResource(R.drawable.c4_3);
                break;
            case "0xde57c8410x0721f342": //C5 抽血
                if(nextWaypointID.equals("0xf295c2410x63a8f042") || nextWaypointID.equals("0x8193bd410x540df142")) //C2 & C3 樓梯
                    image.setImageResource(R.drawable.c5_1);
                else if(nextWaypointID.equals("0xc43af3420x00000000")) //C4 核子醫學部
                    image.setImageResource(R.drawable.c5_3);
                else if(nextWaypointID.equals("0x3519b8410x4d06f042") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                    image.setImageResource(R.drawable.c5_2);
                break;
            case "0x553bc2410x2d44f042": //C6 大廳(病歷室前)
                if(nextWaypointID.equals("0xc43af3420x00000000")) //C4 核子醫學部
                    image.setImageResource(R.drawable.c6_1);
                else if(nextWaypointID.equals("0x3519b8410x4d06f042") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                    image.setImageResource(R.drawable.c6_2);
                else if(nextWaypointID.equals("0x0800b8410x0200f042"))  //D3 迴轉樓梯(神經部)
                    image.setImageResource(R.drawable.c6_s);
                break;
            case "0x7b9812120x00020000": //C6 大廳(病歷室前)
                if(nextWaypointID.equals("0xc43af3420x00000000")) //C4 核子醫學部
                    image.setImageResource(R.drawable.c6_1);
                else if(nextWaypointID.equals("0x3519b8410x4d06f042") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                    image.setImageResource(R.drawable.c6_2);
                else if(nextWaypointID.equals("0x0800b8410x0200f042"))  //D3 迴轉樓梯(神經部)
                    image.setImageResource(R.drawable.c6_s);
                break;
            case "0xeb57ba410xfb95f042": //C7 後門
                if(nextWaypointID.equals("0x553bc2410x2d44f042") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                    image.setImageResource(R.drawable.c6_1);
                else if(nextWaypointID.equals("0x3519b8410x4d06f042") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                    image.setImageResource(R.drawable.c8_1);
                break;
            case "0x3519b8410x4d06f042": //C8 大廳(電腦斷層室前)
                if(nextWaypointID.equals("0xde57c8410x0721f342")) //C5 抽血
                    image.setImageResource(R.drawable.c8_1);
                else if(nextWaypointID.equals("0x553bc2410x2d44f042") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                    image.setImageResource(R.drawable.c8_2);
                else if(nextWaypointID.equals("0xa37dbe410x44d4f042"))  //D6 迴轉樓梯(心臟血管功能)
                    image.setImageResource(R.drawable.c8_s);
                break;
            case "0x7b9551660x00020000": //C8 大廳(電腦斷層室前)
                if(nextWaypointID.equals("0xde57c8410x0721f342")) //C5 抽血
                    image.setImageResource(R.drawable.c8_1);
                else if(nextWaypointID.equals("0x553bc2410x2d44f042") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                    image.setImageResource(R.drawable.c8_2);
                else if(nextWaypointID.equals("0xa37dbe410x44d4f042"))  //D6 迴轉樓梯(心臟血管功能)
                    image.setImageResource(R.drawable.c8_s);
                break;
            case "0x3319b8410x4d06f042": //D1  樓梯
                if(nextWaypointID.equals("0x3219b8410x4d06f042")) //D4 岔路
                    image.setImageResource(R.drawable.d1_1);
                else if(nextWaypointID.equals("0xf295c2410x63a8f042") || nextWaypointID.equals("0x8193bd410x540df142")) //C2 & C3 樓梯
                    faceto.setText("請面對樓梯");
                break;
            case "0x021234110x00020000": //D2 神經部檢查室
                if(nextWaypointID.equals("0x3219b8410x4d06f042")) //D4 岔路
                    image.setImageResource(R.drawable.d2_1);
                else if(nextWaypointID.equals("0x0800b8410x0200f042")) //D3 迴轉樓梯(神經部)
                    image.setImageResource(R.drawable.d3_s);
                break;
            case "0x0800b8410x0200f042": //D3  迴轉樓梯(神經部)
                if(nextWaypointID.equals("0x021234110x00020000")) //D2 神經部檢查室
                    image.setImageResource(R.drawable.d3_1);
                else if(nextWaypointID.equals("0x553bc2410x2d44f042")) //D3 迴轉樓梯(神經部)
                    image.setImageResource(R.drawable.d3_s);
                break;
            case "0x3219b8410x4d06f042": //D4 神經部檢查室/心臟血管功能檢查室岔路
                if(nextWaypointID.equals("0x3319b8410x4d06f042")) //D1  樓梯
                    image.setImageResource(R.drawable.d4_1);
                else if(nextWaypointID.equals("0x006317170x00020000")) //D5 心臟血管
                    image.setImageResource(R.drawable.d4_2);
                else if(nextWaypointID.equals("0x021234110x00020000")) //D2  神經部檢查室
                    image.setImageResource(R.drawable.d4_3);
                break;
            case "0x006317170x00020000": //D5  心臟血管
                if(nextWaypointID.equals("0x3219b8410x4d06f042")) //D4 神經部檢查室/心臟血管功能檢查室岔路
                    image.setImageResource(R.drawable.d5_2);
                else if(nextWaypointID.equals("0xa37dbe410x44d4f042")) //D6  迴轉樓梯(心臟血管)
                    image.setImageResource(R.drawable.d5_1);
                break;
            case "0xa37dbe410x44d4f042": //D6  迴轉樓梯(心臟血管)
                if(nextWaypointID.equals("0x006317170x00020000")) //D5  心臟血管
                    image.setImageResource(R.drawable.d6_1);
                else if(nextWaypointID.equals("0x3519b8410x4d06f042")) //C8  大廳(電腦斷層)
                    image.setImageResource(R.drawable.d6_s);
                break;

        }



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void goNavigation(View view) {
        image.setImageDrawable(null);
        finish();
    }



}
