package eos.waypointbasedindoornavigation;

/*--

Module Name:
    Init Picture

Abstract:
    起始照片方向顯示

Author:

    Chia 2019/08/26

--*/


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
            setTitle("臺大雲林分院室內導航系統");
            faceto.setText("請面向圖中方向開始導航");
        }else if(language_option.equals("English")){
            setTitle("NTUH - Yunlin");
            faceto.setText("Please face to the direction in picture and start navigation.");
        }

        Log.i("test123", nowWaypointID + " next " + nextWaypointID);

        switch (nowWaypointID) {
            case "00000015-0000-0010-1001-000000101001": //A1(1~10診走廊出口)
                if (nextWaypointID.equals("00000015-0000-0010-1002-000000101002") || nextWaypointID.equals("00000015-0000-0010-1003-000000101003")) //心臟內科/內科/體檢區(A2 & A3)
                    image.setImageResource(R.drawable.a1_1);
                break;
            case "00000015-0000-0010-1002-000000101002": //A2(心臟內科/內科/體檢區)
                if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                    image.setImageResource(R.drawable.a2_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1001-000000101001"))
                    image.setImageResource(R.drawable.a2_2);
                break;
            case "00000015-0000-0010-1003-000000101003": //A3(心臟內科/內科/體檢區)
                if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                    image.setImageResource(R.drawable.a3_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1001-000000101001"))
                    image.setImageResource(R.drawable.a2_2);
                break;
            case "00000015-0000-0010-1004-000000101004": //A4(服務台)
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a4_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017")) //健康教育中心(A16 & A17)
                    image.setImageResource(R.drawable.a4_2);
                else if (nextWaypointID.equals("00000015-0000-0010-1002-000000101002") || nextWaypointID.equals("00000015-0000-0010-1003-000000101003")) //心臟內科/內科/體檢區(A2 & A3)
                    image.setImageResource(R.drawable.a4_3);
                break;
            case "00000015-0000-0010-1005-000000101005": //A5(服務台)
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a4_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017")) //健康教育中心(A16 & A17)
                    image.setImageResource(R.drawable.a4_2);
                else if (nextWaypointID.equals("00000015-0000-0010-1002-000000101002") || nextWaypointID.equals("00000015-0000-0010-1003-000000101003")) //心臟內科/內科/體檢區(A2 & A3)
                    image.setImageResource(R.drawable.a4_3);
                break;
            case "00000015-0000-0010-1006-000000101006": //A6 大門
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a6_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                    image.setImageResource(R.drawable.a6_2);
                break;
            case "00000015-0000-0010-1007-000000101007": //A7 大門
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a7_1);
                break;
            case "00000015-0000-0010-1008-000000101008": //A8 中央走廊
                if (nextWaypointID.equals("00000015-0000-0010-1011-000000101011")) //A11(批價櫃檯)
                    image.setImageResource(R.drawable.a8_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //A18(樓梯)
                    image.setImageResource(R.drawable.a7_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                    image.setImageResource(R.drawable.a8_2);
                break;
            case "00000015-0000-0010-1009-000000101009": //A9大門
                if (nextWaypointID.equals("00000015-0000-0010-1010-000000101010"))  //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a9_1);
                break;
            case "00000015-0000-0010-1010-000000101010": //A10 中央走廊(自動繳費機)
                if (nextWaypointID.equals("00000015-0000-0010-1011-000000101011")) //A11(批價櫃檯)
                    image.setImageResource(R.drawable.a8_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //A18(樓梯)
                    image.setImageResource(R.drawable.a7_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                    image.setImageResource(R.drawable.a8_2);;
                break;
            case "00000015-0000-0010-1011-000000101011": //A11批價
                if (nextWaypointID.equals("00000015-0000-0010-1012-000000101012")) //A12(精神內科)
                    image.setImageResource(R.drawable.a11_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //A19(41診走廊交叉口)
                    image.setImageResource(R.drawable.a11_2);
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a11_3);
                break;
            case "00000015-0000-0010-1012-000000101012": // A12 精神科/神經內科
                if (nextWaypointID.equals("00000015-0000-0010-1013-000000101013")) //A13(26~29診走廊出口)
                    faceto.setText("請面對26~29診走廊出口");
                else if (nextWaypointID.equals("00000015-0000-0010-1011-000000101011")) //A11批價
                    faceto.setText("請面對醫院大廳");
                break;
            case "00000015-0000-0010-1013-000000101013": //A13 26~29診走廊出口
                if (nextWaypointID.equals("00000015-0000-0010-1012-000000101012")) //精神科
                    image.setImageResource(R.drawable.a13_1);
                break;
            case "00000015-0000-0010-1014-000000101014": //A14耳鼻喉科
                if (nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017")) //健康教育中心(A16 & A17)
                    image.setImageResource(R.drawable.a14_1);
                break;
            case "00000015-0000-0010-1015-000000101015": //A15耳鼻喉科
                if (nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017"))  //健康教育中心(A16 & A17)
                    image.setImageResource(R.drawable.a15_1);
                break;
            case "00000015-0000-0010-1016-000000101016": //A16健康教育中心
                if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                    image.setImageResource(R.drawable.a16_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1023-000000101023")) //無障礙領藥窗口 (A23)
                    image.setImageResource(R.drawable.a16_2);
                else if (nextWaypointID.equals("00000015-0000-0010-1014-000000101014") || nextWaypointID.equals("00000015-0000-0010-1015-000000101015")) //耳鼻喉科(A14 & A15)
                    image.setImageResource(R.drawable.a16_3);
                break;
            case "00000015-0000-0010-1017-000000101017": //A17健康教育中心
                if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                    image.setImageResource(R.drawable.a16_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1023-000000101023")) //無障礙領藥窗口 (A23)
                    image.setImageResource(R.drawable.a16_2);
                else if (nextWaypointID.equals("00000015-0000-0010-1014-000000101014") || nextWaypointID.equals("00000015-0000-0010-1015-000000101015")) //耳鼻喉科(A14 & A15)
                    image.setImageResource(R.drawable.a17_3);
                break;
            case "00000015-0000-0010-1018-000000101018": //A18 樓梯
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                    image.setImageResource(R.drawable.a18_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1019-000000101019"))  //A19 30~41診走廊交叉口
                    image.setImageResource(R.drawable.a18_2);
                else if (nextWaypointID.equals("00000015-0000-0010-1024-000000101024")) //A24 (領藥處大廳)
                    image.setImageResource(R.drawable.a18_3);
                else if (nextWaypointID.equals("00000014-0000-0010-1101-000000101101")) //B1 (B1樓梯)
                    image.setImageResource(R.drawable.a18_s);
                break;
            case "00000015-0000-0010-1019-000000101019": //A19 30~41診走廊交叉口
                Log.i("test123", "1");
                if (nextWaypointID.equals("00000015-0000-0010-1011-000000101011")) //A11 批價
                    image.setImageResource(R.drawable.a19_1);
                else if (nextWaypointID.equals("00000015-0000-0010-1020-000000101020") || nextWaypointID.equals("00000015-0000-0010-1021-000000101021")) //外科/骨科/牙科(A20 & A21)
                    image.setImageResource(R.drawable.a19_2);
                else if (nextWaypointID.equals("00000015-0000-0010-1025-000000101025")) //42~49診走廊交叉口
                    image.setImageResource(R.drawable.a19_3);
                else if(nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //樓梯
                    image.setImageResource(R.drawable.a19_4);
                break;
            case "00000015-0000-0010-1020-000000101020": //A20 外科/骨科/牙科
                if(nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //A19 30~41診走廊交叉口
                    image.setImageResource(R.drawable.a20_1);
                break;
            case "00000015-0000-0010-1021-000000101021": //A21 外科/骨科/牙科
                if(nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //A19 30~41診走廊交叉口
                    image.setImageResource(R.drawable.a21_1);
                break;
            case "00000015-0000-0010-1022-000000101022": //A22 腎臟科/腎膽腸內科/新陳代謝分泌科
                if(nextWaypointID.equals("00000015-0000-0010-1023-000000101023")) //A23 無障礙領藥窗口
                    image.setImageResource(R.drawable.a22_1);
                break;
            case "00000015-0000-0010-1023-000000101023": //A23 無障礙領藥窗口
                if(nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017")) //A16 & A17 健康教育中心
                    image.setImageResource(R.drawable.a23_1);
                else if(nextWaypointID.equals("00000015-0000-0010-1024-000000101024")) // A24 領藥處大廳
                    image.setImageResource(R.drawable.a23_2);
                else if(nextWaypointID.equals("00000015-0000-0010-1022-000000101022"))  //A22 腎臟科/腎膽腸內科/新陳代謝分泌科
                    image.setImageResource(R.drawable.a23_3);
                break;
            case "00000015-0000-0010-1024-000000101024": // A24 領藥處大廳
                if(nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) // A18 樓梯
                    image.setImageResource(R.drawable.a24_1);
                else if(nextWaypointID.equals("00000015-0000-0010-1025-000000101025")) //A25 42~49診走廊交叉口
                    image.setImageResource(R.drawable.a24_2);
                else if(nextWaypointID.equals("00000015-0000-0010-1029-000000101029") || nextWaypointID.equals("00000015-0000-0010-1030-000000101030")) //A29 & A30 超商
                    image.setImageResource(R.drawable.a24_3);
                else if(nextWaypointID.equals("00000015-0000-0010-1023-000000101023")) // A23 無障礙領藥窗口
                    image.setImageResource(R.drawable.a24_4);
                break;
            case "00000015-0000-0010-1025-000000101025": //A25 42~49診走廊交叉口
                if(nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //A19 30~41診交叉口
                    image.setImageResource(R.drawable.a25_1);
                else if(nextWaypointID.equals("00000015-0000-0010-1026-000000101026") || nextWaypointID.equals("00000015-0000-0010-1027-000000101027")) //A26 & A27 眼科/皮膚科
                    image.setImageResource(R.drawable.a25_2);
                else if(nextWaypointID.equals("00000015-0000-0010-1024-000000101024")) // A24 領藥處大廳
                    image.setImageResource(R.drawable.a25_3);
                break;
            case "00000015-0000-0010-1026-000000101026": //A26 眼科/皮膚科
                if(nextWaypointID.equals("00000015-0000-0010-1028-000000101028")) //A28 42~49診走廊出口
                    image.setImageResource(R.drawable.a26_1);
                else if(nextWaypointID.equals("00000015-0000-0010-1025-000000101025")) //A25 42~49診走廊交叉口
                    image.setImageResource(R.drawable.a26_2);
                break;
            case "00000015-0000-0010-1027-000000101027": //A27 眼科/皮膚科
                if(nextWaypointID.equals("00000015-0000-0010-1028-000000101028")) //A28 42~49診走廊出口
                    image.setImageResource(R.drawable.a26_1);
                else if(nextWaypointID.equals("00000015-0000-0010-1025-000000101025")) //A25 42~49診走廊交叉口
                    image.setImageResource(R.drawable.a27_2);
                break;
            case "00000015-0000-0010-1028-000000101028": //A28 42~49診走廊出口
                if(nextWaypointID.equals("00000015-0000-0010-1026-000000101026") || nextWaypointID.equals("00000015-0000-0010-1027-000000101027")) //A27 & A26眼科/皮膚科
                    image.setImageResource(R.drawable.a28_1);
                break;
            case "00000015-0000-0010-1029-000000101029": //A29 超商
                if(nextWaypointID.equals("00000015-0000-0010-1024-000000101024"))  //A24 領藥處大廳
                    image.setImageResource(R.drawable.a29_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0101-000000000101"))  //C1 新舊大樓連接走廊
                    image.setImageResource(R.drawable.a29_2);
                break;
            case "00000015-0000-0010-1030-000000101030": //A30 超商
                if(nextWaypointID.equals("00000015-0000-0010-1024-000000101024"))  //A24 領藥處大廳
                    image.setImageResource(R.drawable.a29_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0101-000000000101"))  //C1 新舊大樓連接走廊
                    image.setImageResource(R.drawable.a29_2);
                break;
            case "00000014-0000-0010-1101-000000101101": //B1 樓梯
                if(nextWaypointID.equals("00000014-0000-0010-1103-000000101103")) //B3 X光報到處
                    image.setImageResource(R.drawable.b1_2);
                else if(nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //A18 (1F樓梯)
                {
                    image.setImageResource(R.drawable.b2_1);
                    //faceto.setText("請面對樓梯");
                }
                break;
            case "00000014-0000-0010-1102-000000101102": //B2 電梯
                if(nextWaypointID.equals("00000014-0000-0010-1101-000000101101"))  //B1 樓梯
                    image.setImageResource(R.drawable.b2_1);
                break;
            case "00000014-0000-0010-1103-000000101103": //B3 X光報到處
                if(nextWaypointID.equals("00000014-0000-0010-1101-000000101101"))  //B1 樓梯
                    image.setImageResource(R.drawable.b3_1);
                break;
            case "00000015-0000-0000-0101-000000000101": //C1 新舊大樓連接走廊
                if(nextWaypointID.equals("00000015-0000-0010-1029-000000101029") || nextWaypointID.equals("00000015-0000-0010-1030-000000101030")) //A29 & A30 超商
                    image.setImageResource(R.drawable.c1_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0202-000000000202") || nextWaypointID.equals("00000015-0000-0001-8500-000000019000")) //C2 & C3 樓梯
                    image.setImageResource(R.drawable.c1_2);
                break;
            case "00000015-0000-0000-0202-000000000202": //C2 樓梯
                if(nextWaypointID.equals("00000015-0000-0000-0101-000000000101")) //C1 新舊大樓連接走廊
                    image.setImageResource(R.drawable.c2_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                    image.setImageResource(R.drawable.c2_2);
                else if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                    image.setImageResource(R.drawable.c2_3);
                else if(nextWaypointID.equals("00000016-0000-0002-4000-000000026000")) //D1 2F樓梯
                    image.setImageResource(R.drawable.c2_s);
                break;
            case "00000015-0000-0001-8500-000000019000": //C3 樓梯
                if(nextWaypointID.equals("00000015-0000-0000-0101-000000000101")) //C1 新舊大樓連接走廊
                    image.setImageResource(R.drawable.c2_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                    image.setImageResource(R.drawable.c2_2);
                else if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                    image.setImageResource(R.drawable.c2_3);
                else if(nextWaypointID.equals("00000016-0000-0002-4000-000000026000")) //D1 2F樓梯
                    image.setImageResource(R.drawable.c2_s);
                break;
            case "00000015-0000-0000-0404-000000000404": //C4 核子醫學部
                if(nextWaypointID.equals("00000015-0000-0000-0202-000000000202") || nextWaypointID.equals("00000015-0000-0001-8500-000000019000")) //C2 & C3 樓梯
                    image.setImageResource(R.drawable.c4_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                    image.setImageResource(R.drawable.c4_2);
                else if(nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                    image.setImageResource(R.drawable.c4_3);
                else if(nextWaypointID.equals("00000015-0000-0000-0707-000000000707") || nextWaypointID.equals("0x7b6913130x00020000"))  //C7 C8 後門
                    image.setImageResource(R.drawable.c4_3);
                break;
            case "00000015-0000-0000-0505-000000000505": //C5 抽血
                if(nextWaypointID.equals("00000015-0000-0000-0202-000000000202") || nextWaypointID.equals("00000015-0000-0001-8500-000000019000")) //C2 & C3 樓梯
                    image.setImageResource(R.drawable.c5_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                    image.setImageResource(R.drawable.c5_3);
                else if(nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                    image.setImageResource(R.drawable.c5_2);
                else if(nextWaypointID.equals("00000015-0000-0000-0707-000000000707") || nextWaypointID.equals("0x7b6913130x00020000"))  //C7 C8 後門
                    image.setImageResource(R.drawable.c5_2);
                break;
            case "00000015-0000-0000-0606-000000000606": //C6 大廳(病歷室前)
                if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                    image.setImageResource(R.drawable.c6_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                    image.setImageResource(R.drawable.c6_2);
                else if(nextWaypointID.equals("00000016-0000-0001-9500-000000014500"))  //D3 迴轉樓梯(神經部)
                    image.setImageResource(R.drawable.c6_s);
                break;
            case "0x7b9812120x00020000": //C6 大廳(病歷室前)
                if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                    image.setImageResource(R.drawable.c6_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                    image.setImageResource(R.drawable.c6_2);
                else if(nextWaypointID.equals("00000016-0000-0001-9500-000000014500"))  //D3 迴轉樓梯(神經部)
                    image.setImageResource(R.drawable.c6_s);
                break;
            case "00000015-0000-0000-0707-000000000707": //C7 後門
                if(nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                    image.setImageResource(R.drawable.c6_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                    image.setImageResource(R.drawable.c8_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                    image.setImageResource(R.drawable.c6_1);
                else  if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                    image.setImageResource(R.drawable.c8_1);
                break;
            case "0x7b6913130x00020000": //C7 後門
                if(nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                    image.setImageResource(R.drawable.c6_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                    image.setImageResource(R.drawable.c8_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                    image.setImageResource(R.drawable.c6_1);
                else  if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                    image.setImageResource(R.drawable.c8_1);
                break;
            case "00000015-0000-0000-0909-000000000909": //C8 大廳(電腦斷層室前)
                if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                    image.setImageResource(R.drawable.c8_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                    image.setImageResource(R.drawable.c8_2);
                else if(nextWaypointID.equals("00000016-0000-0002-8500-000000014500"))  //D6 迴轉樓梯(心臟血管功能)
                    image.setImageResource(R.drawable.c8_s);
                break;
            case "0x7b9551660x00020000": //C8 大廳(電腦斷層室前)
                if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                    image.setImageResource(R.drawable.c8_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                    image.setImageResource(R.drawable.c8_2);
                else if(nextWaypointID.equals("00000016-0000-0002-8500-000000014500"))  //D6 迴轉樓梯(心臟血管功能)
                    image.setImageResource(R.drawable.c8_s);
                break;
            case "00000016-0000-0002-4000-000000026000": //D1  樓梯
                if(nextWaypointID.equals("00000016-0000-0002-4000-000000017000")) //D4 岔路
                    image.setImageResource(R.drawable.d1_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0202-000000000202") || nextWaypointID.equals("00000015-0000-0001-8500-000000019000")) //C2 & C3 樓梯
                    image.setImageResource(R.drawable.d1_s);
                break;
            case "00000016-0000-0001-9500-000000017000": //D2 神經部檢查室
                if(nextWaypointID.equals("00000016-0000-0002-4000-000000017000")) //D4 岔路
                    image.setImageResource(R.drawable.d2_1);
                else if(nextWaypointID.equals("00000016-0000-0001-9500-000000014500")) //D3 迴轉樓梯(神經部)
                    image.setImageResource(R.drawable.d3_s);
                break;
            case "00000016-0000-0001-9500-000000014500": //D3  迴轉樓梯(神經部)
                if(nextWaypointID.equals("00000016-0000-0001-9500-000000017000")) //D2 神經部檢查室
                    image.setImageResource(R.drawable.d3_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0606-000000000606")) //C6 病歷室
                    image.setImageResource(R.drawable.d3_s);
                break;
            case "00000016-0000-0002-4000-000000017000": //D4 神經部檢查室/心臟血管功能檢查室岔路
                if(nextWaypointID.equals("00000016-0000-0002-4000-000000026000")) //D1  樓梯
                    image.setImageResource(R.drawable.d4_1);
                else if(nextWaypointID.equals("00000016-0000-0002-8500-000000017000")) //D5 心臟血管
                    image.setImageResource(R.drawable.d4_2);
                else if(nextWaypointID.equals("00000016-0000-0001-9500-000000017000")) //D2  神經部檢查室
                    image.setImageResource(R.drawable.d4_3);
                break;
            case "00000016-0000-0002-8500-000000017000": //D5  心臟血管
                if(nextWaypointID.equals("00000016-0000-0002-4000-000000017000")) //D4 神經部檢查室/心臟血管功能檢查室岔路
                    image.setImageResource(R.drawable.d5_2);
                else if(nextWaypointID.equals("00000016-0000-0002-8500-000000014500")) //D6  迴轉樓梯(心臟血管)
                    image.setImageResource(R.drawable.d5_1);
                break;
            case "00000016-0000-0002-8500-000000014500": //D6  迴轉樓梯(心臟血管)
                if(nextWaypointID.equals("00000016-0000-0002-8500-000000017000")) //D5  心臟血管
                    image.setImageResource(R.drawable.d6_1);
                else if(nextWaypointID.equals("00000015-0000-0000-0909-000000000909")) //C8  大廳(電腦斷層)
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
