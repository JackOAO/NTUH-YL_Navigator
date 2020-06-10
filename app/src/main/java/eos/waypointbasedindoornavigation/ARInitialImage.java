package eos.waypointbasedindoornavigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;


public class ARInitialImage {
    int predictDirection;

    ARInitialImage()
    {
        predictDirection = -1;
    }
    int getPredict(String nowWaypointID, String nextWaypointID)
    {
        int predict = -1;
        switch (nowWaypointID) {
            case "first"://虛擬點1
                if(nextWaypointID.equals("00000015-0000-0010-1018-000000101018"))//A18樓梯
                    predict = 6;//west
                else if(nextWaypointID.equals("00010014-0000-0000-0000-000000000001"))//B1電梯
                    predict = 6;//west
                break;
            case "second"://虛擬點2
                if(nextWaypointID.equals("00160015-0000-0000-0001-000000000001") || nextWaypointID.equals("00000015-0000-0001-8500-000000019000"))//C2 & C3
                    predict = 2;//east
                if(nextWaypointID.equals("00190016-0000-0000-0002-000000000001"))//D1
                    predict = 2;//east
                break;
            case "third"://虛擬點3
                if(nextWaypointID.equals("00000015-0000-0000-0909-000000000909"))//電腦斷層室
                    predict = 0;//north
                if(nextWaypointID.equals("00210016-0000-0000-0002-000000000003"))//D6心血管迴轉樓梯
                    predict = 4;//south
                break;
            case "fourth"://虛擬點4
                if(nextWaypointID.equals("00000015-0000-0000-0606-000000000606"))//病歷室
                    predict = 0;//north
                if(nextWaypointID.equals("00200016-0000-0000-0002-000000000002"))//D3神經檢查室迴轉樓梯
                    predict = 4;//south
                break;
            case "fifth"://虛擬點5
                if(nextWaypointID.equals("00000015-0000-0001-8500-000000011500"))//新大樓一樓電梯
                    predict = 0;//north
                if(nextWaypointID.equals("00030018-0000-0003-1500-000000033000"))//5B電梯
                    predict = 6;//west
                break;

            case "00000015-0000-0010-1001-000000101001": //A1(1~10診走廊出口)
                if (nextWaypointID.equals("00000015-0000-0010-1002-000000101002") || nextWaypointID.equals("00000015-0000-0010-1003-000000101003")) //心臟內科/內科/體檢區(A2 & A3)
                    predict = 6;//west
                break;
            case "00000015-0000-0010-1002-000000101002": //A2(心臟內科/內科/體檢區)
                if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1001-000000101001")) {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1003-000000101003": //A3(心臟內科/內科/體檢區)
                if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1001-000000101001")) {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1004-000000101004": //A4(服務台)
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017")) //健康教育中心(A16 & A17)
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00000015-0000-0010-1002-000000101002") || nextWaypointID.equals("00000015-0000-0010-1003-000000101003")) //心臟內科/內科/體檢區(A2 & A3)
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1005-000000101005": //A5(服務台)
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017")) //健康教育中心(A16 & A17)
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00000015-0000-0010-1002-000000101002") || nextWaypointID.equals("00000015-0000-0010-1003-000000101003")) //心臟內科/內科/體檢區(A2 & A3)
                {
                    predict = 2;//east
                }
                break;
            case "00020015-0000-0000-0001-000000000001": //A6 大門
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                {
                    predict = 7;//westnorth
                } else if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    predict = 1;//eastnorth
                }
                break;
            case "00020015-0000-0000-0001-000000000002": //A7 大門
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                {
                    predict = 0;//north
                }
                break;
            case "00000015-0000-0010-1008-000000101008": //A8 中央走廊
                if (nextWaypointID.equals("00000015-0000-0010-1011-000000101011")) //A11(批價櫃檯)
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //A18(樓梯)
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    predict = 2;//east
                }
                break;
            case "00020015-0000-0000-0001-000000000003": //A9大門
                if (nextWaypointID.equals("00000015-0000-0010-1010-000000101010"))  //中央走廊(A8 & A10)
                {
                    predict = 0;//north
                }
                break;
            case "00000015-0000-0010-1010-000000101010": //A10 中央走廊(自動繳費機)
                if (nextWaypointID.equals("00000015-0000-0010-1011-000000101011")) //A11(批價櫃檯)
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //A18(樓梯)
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1011-000000101011": //A11批價
                if (nextWaypointID.equals("00000015-0000-0010-1012-000000101012")) //A12(精神內科)
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //A19(41診走廊交叉口)
                {
                    predict = 0;//north
                }
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1012-000000101012": // A12 精神科/神經內科
                if (nextWaypointID.equals("00000015-0000-0010-1013-000000101013")) //A13(26~29診走廊出口)
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1011-000000101011")) //A11批價
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1013-000000101013": //A13 26~29診走廊出口
                if (nextWaypointID.equals("00000015-0000-0010-1012-000000101012")) //精神科
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1014-000000101014": //A14耳鼻喉科
                if (nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017")) //健康教育中心(A16 & A17)
                {
                    predict = 6;//west
                }
                break;
            case "00000015-0000-0010-1015-000000101015": //A15耳鼻喉科
                if (nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017"))  //健康教育中心(A16 & A17)
                {
                    predict = 6;//west
                }
                break;
            case "00000015-0000-0010-1016-000000101016": //A16健康教育中心
                if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0010-1023-000000101023")) //無障礙領藥窗口 (A23)
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00000015-0000-0010-1014-000000101014") || nextWaypointID.equals("00000015-0000-0010-1015-000000101015")) //耳鼻喉科(A14 & A15)
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1017-000000101017": //A17健康教育中心
                if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0010-1023-000000101023")) //無障礙領藥窗口 (A23)
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00000015-0000-0010-1014-000000101014") || nextWaypointID.equals("00000015-0000-0010-1015-000000101015")) //耳鼻喉科(A14 & A15)
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1018-000000101018": //A18 樓梯
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0010-1019-000000101019"))  //A19 30~41診走廊交叉口
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0001-8500-000000011500"))  //1樓電梯
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1024-000000101024")) //A24 (領藥處大廳)
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00010014-0000-0000-0000-000000000001")) //B1 (B1樓梯)
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0001-8500-000000011500"://C10 1樓電梯
                if (nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //A18 樓梯
                {
                    predict = 2;//east
                } else if (nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //42~49診交叉口
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00030018-0000-0003-1500-000000033000"))//5B電梯
                {
                    predict = 4;//south
                    // TODO: 2020/3/23  maybe need the direction
                }
                break;
            case "00030018-0000-0003-1500-000000033000"://5B電梯
                if (nextWaypointID.equals("00030018-0000-0002-9500-000000027000")) //配膳室
                {
                    predict = 4;//south
                }
                break;
            case "00030018-0000-0002-9500-000000027000"://配膳室
                if (nextWaypointID.equals("00030018-0000-0003-1500-000000033000")) //5B電梯
                {
                    predict = 0;//north
                }
                else if(nextWaypointID.equals("00030018-0000-0002-8000-000000021000") || nextWaypointID.equals("00030018-0000-0003-3500-000000023500"))//中庭或輪椅放置區
                {
                    predict = 4;//south
                }
                break;
            case "00030018-0000-0002-8000-000000021000"://中庭
                if(nextWaypointID.equals("00030018-0000-0002-9500-000000027000"))//配膳室
                {
                    predict = 0;//north
                }
                else if(nextWaypointID.equals("00030018-0000-0003-3500-000000023500"))//輪椅放置區
                {
                    predict = 2;//east
                }
                break;
            case "0030018-0000-0003-3500-000000023500"://輪椅放置區
                if(nextWaypointID.equals("00030018-0000-0002-9500-000000027000"))//配膳室
                {
                    predict = 0;//north
                }
                else if(nextWaypointID.equals("00030018-0000-0002-8000-000000021000"))//中庭
                {
                    predict = 6;//west
                }
                break;

            case "00000015-0000-0010-1019-000000101019": //A19 30~41診走廊交叉口
                if (nextWaypointID.equals("00000015-0000-0010-1011-000000101011")) //A11 批價
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0010-1020-000000101020") || nextWaypointID.equals("00000015-0000-0010-1021-000000101021")) //外科/骨科/牙科(A20 & A21)
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1025-000000101025")) //42~49診走廊交叉口
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //樓梯
                {
                    predict = 2;//east
                }else if(nextWaypointID.equals("00000015-0000-0001-8500-000000011500")) //電梯
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1020-000000101020": //A20 外科/骨科/牙科
                if (nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //A19 30~41診走廊交叉口
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1021-000000101021": //A21 外科/骨科/牙科
                if (nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //A19 30~41診走廊交叉口
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1022-000000101022": //A22 腎臟科/腎膽腸內科/新陳代謝分泌科
                if (nextWaypointID.equals("00000015-0000-0010-1023-000000101023")) //A23 無障礙領藥窗口
                {
                    predict = 6;//west
                }
                break;
            case "00000015-0000-0010-1023-000000101023": //A23 無障礙領藥窗口
                if (nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017")) //A16 & A17 健康教育中心
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0010-1024-000000101024")) // A24 領藥處大廳
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1022-000000101022"))  //A22 腎臟科/腎膽腸內科/新陳代謝分泌科
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1024-000000101024": // A24 領藥處大廳
                if (nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) // A18 樓梯
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0010-1025-000000101025")) //A25 42~49診走廊交叉口
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1029-000000101029") || nextWaypointID.equals("00000015-0000-0010-1030-000000101030")) //A29 & A30 超商
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00000015-0000-0010-1023-000000101023")) // A23 無障礙領藥窗口
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1025-000000101025": //A25 42~49診走廊交叉口
                if (nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //A19 30~41診交叉口
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0010-1026-000000101026") || nextWaypointID.equals("00000015-0000-0010-1027-000000101027")) //A26 & A27 眼科/皮膚科
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1024-000000101024")) // A24 領藥處大廳
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1026-000000101026": //A26 眼科/皮膚科
                if (nextWaypointID.equals("00000015-0000-0010-1028-000000101028")) //A28 42~49診走廊出口
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1025-000000101025")) //A25 42~49診走廊交叉口
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1027-000000101027": //A27 眼科/皮膚科
                if (nextWaypointID.equals("00000015-0000-0010-1028-000000101028")) //A28 42~49診走廊出口
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0010-1025-000000101025")) //A25 42~49診走廊交叉口
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1028-000000101028": //A28 42~49診走廊出口
                if (nextWaypointID.equals("00000015-0000-0010-1026-000000101026") || nextWaypointID.equals("00000015-0000-0010-1027-000000101027")) //A27 & A26眼科/皮膚科
                {
                    predict = 2;//east
                }
                break;
            case "00000015-0000-0010-1029-000000101029": //A29 超商
                if (nextWaypointID.equals("00000015-0000-0010-1024-000000101024"))  //A24 領藥處大廳
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0101-000000000101"))  //C1 新舊大樓連接走廊
                {
                    predict = 0;//north
                }
                break;
            case "00000015-0000-0010-1030-000000101030": //A30 超商
                if (nextWaypointID.equals("00000015-0000-0010-1024-000000101024"))  //A24 領藥處大廳
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0101-000000000101"))  //C1 新舊大樓連接走廊
                {
                    predict = 0;//north
                }
                break;
            case "00010014-0000-0000-0000-000000000001": //B1 樓梯
                if (nextWaypointID.equals("00000014-0000-0010-1103-000000101103")) //B3 X光報到處
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //A18 (1F樓梯)
                {
                    predict = 2;//east
                    //faceto.setText("請面對樓梯");
                } else if (nextWaypointID.equals("00000014-0000-0010-1102-000000101102")) //B2電梯
                {
                    predict = 6;//west
                }
                break;
            case "00000014-0000-0010-1102-000000101102": //B2 電梯
                if (nextWaypointID.equals("00010014-0000-0000-0000-000000000001"))  //B1 樓梯
                {
                    predict = 2;//east
                }
                break;
            case "00000014-0000-0010-1103-000000101103": //B3 X光報到處
                if (nextWaypointID.equals("00010014-0000-0000-0000-000000000001"))  //B1 樓梯
                {
                    predict = 4;//south
                }
                break;
            case "00000015-0000-0000-0101-000000000101": //C1 新舊大樓連接走廊
                if (nextWaypointID.equals("00000015-0000-0010-1029-000000101029") || nextWaypointID.equals("00000015-0000-0010-1030-000000101030")) //A29 & A30 超商
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00160015-0000-0000-0001-000000000001") || nextWaypointID.equals("00000015-0000-0001-8500-000000019000")) //C2 & C3 樓梯
                {
                    predict = 0;//north
                }
                break;
            case "00160015-0000-0000-0001-000000000001": //C2 樓梯
                if (nextWaypointID.equals("00000015-0000-0000-0101-000000000101")) //C1 新舊大樓連接走廊
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00190016-0000-0000-0002-000000000001")) //D1 2F樓梯
                {
                    predict = 6;//west
                }
                break;
            case "00000015-0000-0001-8500-000000019000": //C3 樓梯
                if (nextWaypointID.equals("00000015-0000-0000-0101-000000000101")) //C1 新舊大樓連接走廊
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00190016-0000-0000-0002-000000000001")) //D1 2F樓梯
                {
                    predict = 6;//west
                }
                break;
            case "00000015-0000-0000-0404-000000000404": //C4 核子醫學部
                if (nextWaypointID.equals("00160015-0000-0000-0001-000000000001") || nextWaypointID.equals("00000015-0000-0001-8500-000000019000")) //C2 & C3 樓梯
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00170015-0000-0000-0001-000000000002") || nextWaypointID.equals("0x7b6913130x00020000"))  //C7 C8 後門
                {
                    predict = 0;//north
                }
                break;
            case "00000015-0000-0000-0505-000000000505": //C5 抽血
                if (nextWaypointID.equals("00160015-0000-0000-0001-000000000001") || nextWaypointID.equals("00000015-0000-0001-8500-000000019000")) //C2 & C3 樓梯
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    predict = 2;//east
                } else if (nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00170015-0000-0000-0001-000000000002") || nextWaypointID.equals("0x7b6913130x00020000"))  //C7 C8 後門
                {
                    predict = 0;//north
                }
                break;
            case "00000015-0000-0000-0606-000000000606": //C6 大廳(病歷室前)
                if (nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00200016-0000-0000-0002-000000000002"))  //D3 迴轉樓梯(神經部)
                {
                    predict = 4;//south
                }
                break;
            case "0x7b9812120x00020000": //C6 大廳(病歷室前)
                if (nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00200016-0000-0000-0002-000000000002"))  //D3 迴轉樓梯(神經部)
                {
                    predict = 4;//south
                }
                break;
            case "00170015-0000-0000-0001-000000000002": //C7 後門
                if (nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    predict= 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    predict = 4;//south
                }
                break;
            case "0x7b6913130x00020000": //C7 後門
                if (nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    predict = 4;//south
                }
                break;
            case "00000015-0000-0000-0909-000000000909": //C8 大廳(電腦斷層室前)
                if (nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                {
                    predict = 2;//east
                } else if (nextWaypointID.equals("00210016-0000-0000-0002-000000000003"))  //D6 迴轉樓梯(心臟血管功能)
                {
                    predict = 4;//south
                }
                break;
            case "0x7b9551660x00020000": //C8 大廳(電腦斷層室前)
                if (nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                {
                    predict = 2;//east
                } else if (nextWaypointID.equals("00210016-0000-0000-0002-000000000003"))  //D6 迴轉樓梯(心臟血管功能)
                {
                    predict = 4;//south
                }
                break;
            case "00190016-0000-0000-0002-000000000001": //D1  樓梯
                if (nextWaypointID.equals("00000016-0000-0002-4000-000000017000")) //D4 岔路
                {
                    predict = 0;//north
                } else if (nextWaypointID.equals("00160015-0000-0000-0001-000000000001") || nextWaypointID.equals("00000015-0000-0001-8500-000000019000")) //C2 & C3 樓梯
                {
                    predict = 6;//west
                }
                break;
            case "00000016-0000-0001-9500-000000017000": //D2 神經部檢查室
                if (nextWaypointID.equals("00000016-0000-0002-4000-000000017000")) //D4 岔路
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00200016-0000-0000-0002-000000000002")) //D3 迴轉樓梯(神經部)
                {
                    predict = 0;//north
                }
                break;
            case "00200016-0000-0000-0002-000000000002": //D3  迴轉樓梯(神經部)
                if (nextWaypointID.equals("00000016-0000-0001-9500-000000017000")) //D2 神經部檢查室
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0606-000000000606")) //C6 病歷室
                {
                    predict = 4;//south
                }
                break;
            case "00000016-0000-0002-4000-000000017000": //D4 神經部檢查室/心臟血管功能檢查室岔路
                if (nextWaypointID.equals("00190016-0000-0000-0002-000000000001")) //D1  樓梯
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000016-0000-0002-8500-000000017000")) //D5 心臟血管
                {
                    predict = 6;//west
                } else if (nextWaypointID.equals("00000016-0000-0001-9500-000000017000")) //D2  神經部檢查室
                {
                    predict = 2;//east
                }
                break;
            case "00000016-0000-0002-8500-000000017000": //D5  心臟血管
                if (nextWaypointID.equals("00000016-0000-0002-4000-000000017000")) //D4 神經部檢查室/心臟血管功能檢查室岔路
                {
                    predict = 2;//east
                } else if (nextWaypointID.equals("00210016-0000-0000-0002-000000000003")) //D6  迴轉樓梯(心臟血管)
                {
                    predict = 0;//north
                }
                break;
            case "00210016-0000-0000-0002-000000000003": //D6  迴轉樓梯(心臟血管)
                if (nextWaypointID.equals("00000016-0000-0002-8500-000000017000")) //D5  心臟血管
                {
                    predict = 4;//south
                } else if (nextWaypointID.equals("00000015-0000-0000-0909-000000000909")) //C8  大廳(電腦斷層)
                {
                    predict = 0;//north
                }
                break;
        }
        return predict;
    }
    int deciseImageToShow(String nowWaypointID, String nextWaypointID)
    {
        int image = 0;
        switch (nowWaypointID) {
            case "00000015-0000-0010-1001-000000101001": //A1(1~10診走廊出口)
                if (nextWaypointID.equals("00000015-0000-0010-1002-000000101002") || nextWaypointID.equals("00000015-0000-0010-1003-000000101003")) //心臟內科/內科/體檢區(A2 & A3)
                    image = R.drawable.a1_1;
                predictDirection = 6;//west
                break;
            case "00000015-0000-0010-1002-000000101002": //A2(心臟內科/內科/體檢區)
                if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    image = R.drawable.a2_1;
                    predictDirection = 6;//west
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1001-000000101001"))
                {
                    image = R.drawable.a2_2;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1003-000000101003": //A3(心臟內科/內科/體檢區)
                if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    image = R.drawable.a3_1;
                    predictDirection = 6;//west
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1001-000000101001"))
                {
                    image = R.drawable.a2_2;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1004-000000101004": //A4(服務台)
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                {
                    image = R.drawable.a4_1;
                    predictDirection = 6;//west
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017")) //健康教育中心(A16 & A17)
                {
                    image = R.drawable.a4_2;
                    predictDirection = 0;//north
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1002-000000101002") || nextWaypointID.equals("00000015-0000-0010-1003-000000101003")) //心臟內科/內科/體檢區(A2 & A3)
                {
                    image = R.drawable.a4_3;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1005-000000101005": //A5(服務台)
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                {
                    image = R.drawable.a4_1;
                    predictDirection = 6;//west
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017")) //健康教育中心(A16 & A17)
                {
                    image = R.drawable.a4_2;
                    predictDirection = 0;//north
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1002-000000101002") || nextWaypointID.equals("00000015-0000-0010-1003-000000101003")) //心臟內科/內科/體檢區(A2 & A3)
                {
                    image = R.drawable.a4_3;
                    predictDirection = 2;//east
                }
                break;
            case "00020015-0000-0000-0001-000000000001": //A6 大門
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                {
                    image = R.drawable.a6_1;
                    predictDirection = 7;//westnorth
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    image = R.drawable.a6_2;
                    predictDirection = 1;//eastnorth
                }
                break;
            case "00020015-0000-0000-0001-000000000002": //A7 大門
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                {
                    image = R.drawable.a7_1;
                    predictDirection = 0;//north
                }
                break;
            case "00000015-0000-0010-1008-000000101008": //A8 中央走廊
                if (nextWaypointID.equals("00000015-0000-0010-1011-000000101011")) //A11(批價櫃檯)
                {
                    image = R.drawable.a8_1;
                    predictDirection = 6;//west
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //A18(樓梯)
                {
                    image = R.drawable.a7_1;
                    predictDirection = 0;//north
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    image = R.drawable.a8_2;
                    predictDirection = 2;//east
                }
                break;
            case "00020015-0000-0000-0001-000000000003": //A9大門
                if (nextWaypointID.equals("00000015-0000-0010-1010-000000101010"))  //中央走廊(A8 & A10)
                {
                    image = R.drawable.a9_1;
                    predictDirection = 0;//north
                }
                break;
            case "00000015-0000-0010-1010-000000101010": //A10 中央走廊(自動繳費機)
                if (nextWaypointID.equals("00000015-0000-0010-1011-000000101011")) //A11(批價櫃檯)
                {
                    image = R.drawable.a8_1;
                    predictDirection = 6;//west
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //A18(樓梯)
                {
                    image = R.drawable.a7_1;
                    predictDirection = 0;//north
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    image = R.drawable.a8_2;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1011-000000101011": //A11批價
                if (nextWaypointID.equals("00000015-0000-0010-1012-000000101012")) //A12(精神內科)
                {
                    image = R.drawable.a11_1;
                    predictDirection = 6;//west
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //A19(41診走廊交叉口)
                {
                    image = R.drawable.a11_2;
                    predictDirection = 0;//north
                }
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                {
                    image = R.drawable.a11_3;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1012-000000101012": // A12 精神科/神經內科
                if (nextWaypointID.equals("00000015-0000-0010-1013-000000101013")) //A13(26~29診走廊出口)
                {
                    //faceto.setText("請面對26~29診走廊出口");
                    image = R.drawable.a12_2;
                    predictDirection = 6;//west
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1011-000000101011")) //A11批價
                {
                    //faceto.setText("請面對醫院大廳");
                    image = R.drawable.a12_1;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1013-000000101013": //A13 26~29診走廊出口
                if (nextWaypointID.equals("00000015-0000-0010-1012-000000101012")) //精神科
                {
                    image = R.drawable.a13_1;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1014-000000101014": //A14耳鼻喉科
                if (nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017")) //健康教育中心(A16 & A17)
                {
                    image = R.drawable.a14_1;
                    predictDirection = 6;//west
                }
                break;
            case "00000015-0000-0010-1015-000000101015": //A15耳鼻喉科
                if (nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017"))  //健康教育中心(A16 & A17)
                {
                    image = R.drawable.a15_1;
                    predictDirection = 6;//west
                }
                break;
            case "00000015-0000-0010-1016-000000101016": //A16健康教育中心
                if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    image = R.drawable.a16_1;
                    predictDirection = 4;//south
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1023-000000101023")) //無障礙領藥窗口 (A23)
                {
                    image = R.drawable.a16_2;
                    predictDirection = 0;//north
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1014-000000101014") || nextWaypointID.equals("00000015-0000-0010-1015-000000101015")) //耳鼻喉科(A14 & A15)
                {
                    image = R.drawable.a16_3;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1017-000000101017": //A17健康教育中心
                if (nextWaypointID.equals("00000015-0000-0010-1004-000000101004") || nextWaypointID.equals("00000015-0000-0010-1005-000000101005")) //服務台(A4 & A5)
                {
                    image = R.drawable.a16_1;
                    predictDirection = 4;//south
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1023-000000101023")) //無障礙領藥窗口 (A23)
                {
                    image = R.drawable.a16_2;
                    predictDirection = 0;//north
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1014-000000101014") || nextWaypointID.equals("00000015-0000-0010-1015-000000101015")) //耳鼻喉科(A14 & A15)
                {
                    image = R.drawable.a17_3;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1018-000000101018": //A18 樓梯
                if (nextWaypointID.equals("00000015-0000-0010-1008-000000101008") || nextWaypointID.equals("00000015-0000-0010-1010-000000101010")) //中央走廊(A8 & A10)
                {
                    image = R.drawable.a18_1;
                    predictDirection = 4;//south
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1019-000000101019"))  //A19 30~41診走廊交叉口
                {
                    image = R.drawable.a18_2;
                    predictDirection = 6;//west
                }
                else if (nextWaypointID.equals("00000015-0000-0001-8500-000000011500"))  //1樓電梯
                {
                    image = R.drawable.a18_2;
                    predictDirection = 6;//west
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1024-000000101024")) //A24 (領藥處大廳)
                {
                    image = R.drawable.a18_3;
                    predictDirection = 0;//north
                }
                else if (nextWaypointID.equals("00010014-0000-0000-0000-000000000001")) //B1 (B1樓梯)
                {
                    image = R.drawable.a18_s;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0001-8500-000000011500"://C10 1樓電梯
                if (nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //A18 樓梯
                {
                    image = R.drawable.a19_4;
                    predictDirection = 2;
                }
                else if(nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //42~49診交叉口
                {
                    image = R.drawable.a18_2;
                    predictDirection = 6;//west
                }
                else if(nextWaypointID.equals("00030018-0000-0003-1500-000000033000"))//5B電梯
                {
                    image = R.drawable.elevator;
                    predictDirection = 4;//south
                    // TODO: 2020/3/4  maybe need the direction
                }
                break;
            case "00000015-0000-0010-1019-000000101019": //A19 30~41診走廊交叉口
                if (nextWaypointID.equals("00000015-0000-0010-1011-000000101011")) //A11 批價
                {
                    image = R.drawable.a19_1;
                    predictDirection = 4;//south
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1020-000000101020") || nextWaypointID.equals("00000015-0000-0010-1021-000000101021")) //外科/骨科/牙科(A20 & A21)
                {
                    image = R.drawable.a19_2;
                    predictDirection = 6;//west
                }
                else if (nextWaypointID.equals("00000015-0000-0010-1025-000000101025")) //42~49診走廊交叉口
                {
                    image = R.drawable.a19_3;
                    predictDirection = 0;//north
                }
                else if(nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //樓梯
                {
                    image = R.drawable.a19_4;
                    predictDirection = 2;//east
                }
                else if(nextWaypointID.equals("00000015-0000-0001-8500-000000011500")) //電梯
                {
                    image = R.drawable.a19_4;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1020-000000101020": //A20 外科/骨科/牙科
                if(nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //A19 30~41診走廊交叉口
                {
                    image = R.drawable.a20_1;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1021-000000101021": //A21 外科/骨科/牙科
                if(nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //A19 30~41診走廊交叉口
                {
                    image = R.drawable.a21_1;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1022-000000101022": //A22 腎臟科/腎膽腸內科/新陳代謝分泌科
                if(nextWaypointID.equals("00000015-0000-0010-1023-000000101023")) //A23 無障礙領藥窗口
                {
                    image = R.drawable.a22_1;
                    predictDirection = 6;//west
                }
                break;
            case "00000015-0000-0010-1023-000000101023": //A23 無障礙領藥窗口
                if(nextWaypointID.equals("00000015-0000-0010-1016-000000101016") || nextWaypointID.equals("00000015-0000-0010-1017-000000101017")) //A16 & A17 健康教育中心
                {
                    image = R.drawable.a23_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0010-1024-000000101024")) // A24 領藥處大廳
                {
                    image = R.drawable.a23_2;
                    predictDirection = 6;//west
                }
                else if(nextWaypointID.equals("00000015-0000-0010-1022-000000101022"))  //A22 腎臟科/腎膽腸內科/新陳代謝分泌科
                {
                    image = R.drawable.a23_3;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1024-000000101024": // A24 領藥處大廳
                if(nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) // A18 樓梯
                {
                    image = R.drawable.a24_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0010-1025-000000101025")) //A25 42~49診走廊交叉口
                {
                    image = R.drawable.a24_2;
                    predictDirection = 6;//west
                }
                else if(nextWaypointID.equals("00000015-0000-0010-1029-000000101029") || nextWaypointID.equals("00000015-0000-0010-1030-000000101030")) //A29 & A30 超商
                {
                    image = R.drawable.a24_3;
                    predictDirection = 0;//north
                }
                else if(nextWaypointID.equals("00000015-0000-0010-1023-000000101023")) // A23 無障礙領藥窗口
                {
                    image = R.drawable.a24_4;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1025-000000101025": //A25 42~49診走廊交叉口
                if(nextWaypointID.equals("00000015-0000-0010-1019-000000101019")) //A19 30~41診交叉口
                {
                    image = R.drawable.a25_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0010-1026-000000101026") || nextWaypointID.equals("00000015-0000-0010-1027-000000101027")) //A26 & A27 眼科/皮膚科
                {
                    image = R.drawable.a25_2;
                    predictDirection = 6;//west
                }
                else if(nextWaypointID.equals("00000015-0000-0010-1024-000000101024")) // A24 領藥處大廳
                {
                    image = R.drawable.a25_3;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1026-000000101026": //A26 眼科/皮膚科
                if(nextWaypointID.equals("00000015-0000-0010-1028-000000101028")) //A28 42~49診走廊出口
                {
                    image = R.drawable.a26_1;
                    predictDirection = 6;//west
                }
                else if(nextWaypointID.equals("00000015-0000-0010-1025-000000101025")) //A25 42~49診走廊交叉口
                {
                    image = R.drawable.a26_2;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1027-000000101027": //A27 眼科/皮膚科
                if(nextWaypointID.equals("00000015-0000-0010-1028-000000101028")) //A28 42~49診走廊出口
                {
                    image = R.drawable.a26_1;
                    predictDirection = 6;//west
                }
                else if(nextWaypointID.equals("00000015-0000-0010-1025-000000101025")) //A25 42~49診走廊交叉口
                {
                    image = R.drawable.a27_2;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1028-000000101028": //A28 42~49診走廊出口
                if(nextWaypointID.equals("00000015-0000-0010-1026-000000101026") || nextWaypointID.equals("00000015-0000-0010-1027-000000101027")) //A27 & A26眼科/皮膚科
                {
                    image = R.drawable.a28_1;
                    predictDirection = 2;//east
                }
                break;
            case "00000015-0000-0010-1029-000000101029": //A29 超商
                if(nextWaypointID.equals("00000015-0000-0010-1024-000000101024"))  //A24 領藥處大廳
                {
                    image = R.drawable.a29_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0101-000000000101"))  //C1 新舊大樓連接走廊
                {
                    image = R.drawable.a29_2;
                    predictDirection = 0;//north
                }
                break;
            case "00000015-0000-0010-1030-000000101030": //A30 超商
                if(nextWaypointID.equals("00000015-0000-0010-1024-000000101024"))  //A24 領藥處大廳
                {
                    image = R.drawable.a29_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0101-000000000101"))  //C1 新舊大樓連接走廊
                {
                    image = R.drawable.a29_2;
                    predictDirection = 0;//north
                }
                break;
            case "00010014-0000-0000-0000-000000000001": //B1 樓梯
                if(nextWaypointID.equals("00000014-0000-0010-1103-000000101103")) //B3 X光報到處
                {
                    image = R.drawable.b1_2;
                    predictDirection = 0;//north
                }
                else if(nextWaypointID.equals("00000015-0000-0010-1018-000000101018")) //A18 (1F樓梯)
                {
                    image = R.drawable.b2_1;
                    predictDirection = 2;//east
                }
                else if(nextWaypointID.equals("00000014-0000-0010-1102-000000101102")) //B2電梯
                {
                    image = R.drawable.b1_1;
                    predictDirection = 6;//west
                }
                break;
            case "00000014-0000-0010-1102-000000101102": //B2 電梯
                if(nextWaypointID.equals("00010014-0000-0000-0000-000000000001"))  //B1 樓梯
                {
                    image = R.drawable.b2_1;
                    predictDirection = 2;//east
                }
                break;
            case "00000014-0000-0010-1103-000000101103": //B3 X光報到處
                if(nextWaypointID.equals("00010014-0000-0000-0000-000000000001"))  //B1 樓梯
                {
                    image = R.drawable.b3_1;
                    predictDirection = 4;//south
                }
                break;
            case "00000015-0000-0000-0101-000000000101": //C1 新舊大樓連接走廊
                if(nextWaypointID.equals("00000015-0000-0010-1029-000000101029") || nextWaypointID.equals("00000015-0000-0010-1030-000000101030")) //A29 & A30 超商
                {
                    image = R.drawable.c1_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00160015-0000-0000-0001-000000000001") || nextWaypointID.equals("00000015-0000-0001-8500-000000019000")) //C2 & C3 樓梯
                {
                    image = R.drawable.c1_2;
                    predictDirection = 0;//north
                }
                break;
            case "00160015-0000-0000-0001-000000000001": //C2 樓梯
                if(nextWaypointID.equals("00000015-0000-0000-0101-000000000101")) //C1 新舊大樓連接走廊
                {
                    image = R.drawable.c2_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    image = R.drawable.c2_2;
                    predictDirection = 0;//north
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    image = R.drawable.c2_3;
                    predictDirection = 0;//north
                }
                else if(nextWaypointID.equals("00190016-0000-0000-0002-000000000001")) //D1 2F樓梯
                {
                    image = R.drawable.c2_s;
                    predictDirection = 6;//west
                }
                break;
            case "00000015-0000-0001-8500-000000019000": //C3 樓梯
                if(nextWaypointID.equals("00000015-0000-0000-0101-000000000101")) //C1 新舊大樓連接走廊
                {
                    image = R.drawable.c2_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    image = R.drawable.c2_2;
                    predictDirection = 0;//north
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    image = R.drawable.c2_3;
                    predictDirection = 0;//north
                }
                else if(nextWaypointID.equals("00190016-0000-0000-0002-000000000001")) //D1 2F樓梯
                {
                    image = R.drawable.c2_s;
                    predictDirection = 6;//west
                }
                break;
            case "00000015-0000-0000-0404-000000000404": //C4 核子醫學部
                if(nextWaypointID.equals("00160015-0000-0000-0001-000000000001") || nextWaypointID.equals("00000015-0000-0001-8500-000000019000")) //C2 & C3 樓梯
                {
                    image = R.drawable.c4_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    image = R.drawable.c4_2;
                    predictDirection = 6;//west
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                {
                    image = R.drawable.c4_3;
                    predictDirection = 0;//north
                }
                else if(nextWaypointID.equals("00170015-0000-0000-0001-000000000002") || nextWaypointID.equals("0x7b6913130x00020000"))  //C7 C8 後門
                {
                    image = R.drawable.c4_3;
                    predictDirection = 0;//north
                }
                break;
            case "00000015-0000-0000-0505-000000000505": //C5 抽血
                if(nextWaypointID.equals("00160015-0000-0000-0001-000000000001") || nextWaypointID.equals("00000015-0000-0001-8500-000000019000")) //C2 & C3 樓梯
                {
                    image = R.drawable.c5_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    image = R.drawable.c5_3;
                    predictDirection = 2;//east
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                {
                    image = R.drawable.c5_2;
                    predictDirection = 0;//north
                }
                else if(nextWaypointID.equals("00170015-0000-0000-0001-000000000002") || nextWaypointID.equals("0x7b6913130x00020000"))  //C7 C8 後門
                {
                    image = R.drawable.c5_2;
                    predictDirection = 0;//north
                }
                break;
            case "00000015-0000-0000-0606-000000000606": //C6 大廳(病歷室前)
                if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    image = R.drawable.c6_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                {
                    image = R.drawable.c6_2;
                    predictDirection = 6;//west
                }
                else if(nextWaypointID.equals("00200016-0000-0000-0002-000000000002"))  //D3 迴轉樓梯(神經部)
                {
                    image = R.drawable.c6_s;
                    predictDirection = 4;//south
                }
                break;
            case "0x7b9812120x00020000": //C6 大廳(病歷室前)
                if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    image = R.drawable.c6_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                {
                    image = R.drawable.c6_2;
                    predictDirection = 6;//west
                }
                else if(nextWaypointID.equals("00200016-0000-0000-0002-000000000002"))  //D3 迴轉樓梯(神經部)
                {
                    image = R.drawable.c6_s;
                    predictDirection = 4;//south
                }
                break;
            case "00170015-0000-0000-0001-000000000002": //C7 後門
                if(nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                {
                    image = R.drawable.c6_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                {
                    image = R.drawable.c8_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    image = R.drawable.c6_1;
                    predictDirection = 4;//south
                }
                else  if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    image = R.drawable.c8_1;
                    predictDirection = 4;//south
                }
                break;
            case "0x7b6913130x00020000": //C7 後門
                if(nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                {
                    image = R.drawable.c6_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0909-000000000909") || nextWaypointID.equals("0x7b9551660x00020000"))  //C8 大廳(電腦斷層室前)
                {
                    image = R.drawable.c8_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0404-000000000404")) //C4 核子醫學部
                {
                    image = R.drawable.c6_1;
                    predictDirection = 4;//south
                }
                else  if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    image = R.drawable.c8_1;
                    predictDirection = 4;//south
                }
                break;
            case "00000015-0000-0000-0909-000000000909": //C8 大廳(電腦斷層室前)
                if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    image = R.drawable.c8_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                {
                    image = R.drawable.c8_2;
                    predictDirection = 2;//east
                }
                else if(nextWaypointID.equals("00210016-0000-0000-0002-000000000003"))  //D6 迴轉樓梯(心臟血管功能)
                {
                    image = R.drawable.c8_s;
                    predictDirection = 4;//south
                }
                break;
            case "0x7b9551660x00020000": //C8 大廳(電腦斷層室前)
                if(nextWaypointID.equals("00000015-0000-0000-0505-000000000505")) //C5 抽血
                {
                    image = R.drawable.c8_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0606-000000000606") || nextWaypointID.equals("0x7b9812120x00020000"))  //C6 大廳(病歷室前)
                {
                    image = R.drawable.c8_2;
                    predictDirection = 2;//east
                }
                else if(nextWaypointID.equals("00210016-0000-0000-0002-000000000003"))  //D6 迴轉樓梯(心臟血管功能)
                {
                    image = R.drawable.c8_s;
                    predictDirection = 4;//south
                }
                break;
            case "00190016-0000-0000-0002-000000000001": //D1  樓梯
                if(nextWaypointID.equals("00000016-0000-0002-4000-000000017000")) //D4 岔路
                {
                    image = R.drawable.d1_1;
                    predictDirection = 0;//north
                }
                else if(nextWaypointID.equals("00160015-0000-0000-0001-000000000001") || nextWaypointID.equals("00000015-0000-0001-8500-000000019000")) //C2 & C3 樓梯
                {
                    image = R.drawable.d1_s;
                    predictDirection = 6;//west
                }
                break;
            case "00000016-0000-0001-9500-000000017000": //D2 神經部檢查室
                if(nextWaypointID.equals("00000016-0000-0002-4000-000000017000")) //D4 岔路
                {
                    image = R.drawable.d2_1;
                    predictDirection = 6;//west
                }
                else if(nextWaypointID.equals("00200016-0000-0000-0002-000000000002")) //D3 迴轉樓梯(神經部)
                {
                    image = R.drawable.d3_s;
                    predictDirection = 0;//north
                }
                break;
            case "00200016-0000-0000-0002-000000000002": //D3  迴轉樓梯(神經部)
                if(nextWaypointID.equals("00000016-0000-0001-9500-000000017000")) //D2 神經部檢查室
                {
                    image = R.drawable.d3_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0606-000000000606")) //C6 病歷室
                {
                    image = R.drawable.d3_s;
                    predictDirection = 4;//south
                }
                break;
            case "00000016-0000-0002-4000-000000017000": //D4 神經部檢查室/心臟血管功能檢查室岔路
                if(nextWaypointID.equals("00190016-0000-0000-0002-000000000001")) //D1  樓梯
                {
                    image = R.drawable.d4_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000016-0000-0002-8500-000000017000")) //D5 心臟血管
                {
                    image = R.drawable.d4_2;
                    predictDirection = 6;//west
                }
                else if(nextWaypointID.equals("00000016-0000-0001-9500-000000017000")) //D2  神經部檢查室
                {
                    image = R.drawable.d4_3;
                    predictDirection = 2;//east
                }
                break;
            case "00000016-0000-0002-8500-000000017000": //D5  心臟血管
                if(nextWaypointID.equals("00000016-0000-0002-4000-000000017000")) //D4 神經部檢查室/心臟血管功能檢查室岔路
                {
                    image = R.drawable.d5_2;
                    predictDirection = 2;//east
                }
                else if(nextWaypointID.equals("00210016-0000-0000-0002-000000000003")) //D6  迴轉樓梯(心臟血管)
                {
                    image = R.drawable.d5_1;
                    predictDirection = 0;//north
                }
                break;
            case "00210016-0000-0000-0002-000000000003": //D6  迴轉樓梯(心臟血管)
                if(nextWaypointID.equals("00000016-0000-0002-8500-000000017000")) //D5  心臟血管
                {
                    image = R.drawable.d6_1;
                    predictDirection = 4;//south
                }
                else if(nextWaypointID.equals("00000015-0000-0000-0909-000000000909")) //C8  大廳(電腦斷層)
                {
                    image = R.drawable.d6_s;
                    predictDirection = 0;//north
                }
                break;
            case "00030018-0000-0003-1500-000000033000"://5B電梯
                if (nextWaypointID.equals("00030018-0000-0002-9500-000000027000")) //配膳室
                {
                    // TODO: 2020/4/16  need the image
                    predictDirection = 4;//south
                }
                break;
            case "00030018-0000-0002-9500-000000027000"://配膳室
                if(nextWaypointID.equals("00030018-0000-0003-1500-000000033000"))//5B電梯
                {
                    // TODO: 2020/4/16  need the image
                    predictDirection = 0;//north
                }
                else if(nextWaypointID.equals("00030018-0000-0002-8000-000000021000") || nextWaypointID.equals("00030018-0000-0003-3500-000000023500"))//中庭或輪椅放置區
                {
                    // TODO: 2020/4/16  need the image
                    predictDirection = 4;//south
                }
                break;

            case "00030018-0000-0002-8000-000000021000"://中庭
                if(nextWaypointID.equals("00030018-0000-0002-9500-000000027000"))//配膳室
                {
                    // TODO: 2020/4/16  need the image
                    predictDirection = 0;//north
                }
                else if(nextWaypointID.equals("00030018-0000-0003-3500-000000023500"))//輪椅放置區
                {
                    // TODO: 2020/4/16  need the image
                    predictDirection = 2;//east
                }
                break;

            case "00030018-0000-0003-3500-000000023500"://輪椅放置區
                if(nextWaypointID.equals("00030018-0000-0002-9500-000000027000"))
                {
                    // TODO: 2020/4/16  need the image
                    predictDirection = 0;//south
                }
                else if(nextWaypointID.equals("00030018-0000-0002-8000-000000021000"))
                {
                    // TODO: 2020/4/16  need the image
                    predictDirection = 6;
                }
                break;
        }
        return  image;
    }
    int getPredictDirection()
    {
        return predictDirection;
    }
}
