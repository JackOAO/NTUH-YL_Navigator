package eos.waypointbasedindoornavigation.Find_loc;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class siganl_data_type implements Comparable<siganl_data_type>{
    private int sort_ways = 0;
    private List<Integer> rssi = new ArrayList<>();
    private List<Integer> rssi_delete_extreme_value = new ArrayList<>();
    private String uuid = null;
    private int parameter;
    public siganl_data_type(String s, int i){
        uuid = s;
        rssi.add(i);
    }
    public siganl_data_type(String s, int i, int j){
        uuid = s;
        rssi.add(i);
        parameter = j;
    }
    public void setvalue(String s){
        uuid = s;
    }
    public void setvalue(int i){
        List<Integer> tmp_list = new ArrayList<>();
        tmp_list.add(i);
        tmp_list.addAll(rssi);
        rssi.clear();
        rssi.addAll(tmp_list);
        tmp_list.clear();
        setvalue_extreme_value();
    }

    public void setvalue_extreme_value(){
        List<Integer> tmp_list = new ArrayList<>();
        tmp_list.addAll(rssi);
        Collections.sort(tmp_list);
        tmp_list.remove(0);
        tmp_list.remove((tmp_list.size()-1));
        rssi_delete_extreme_value.clear();
        rssi_delete_extreme_value.addAll(tmp_list);
        tmp_list.clear();
    }
    public String getUuid(){
        return uuid;
    }

    public String getUuid_Name(){
      String Name = null;

      switch (uuid){
          case "00000015-0000-0010-1001-000000101001":
              return  "1~10診走廊出口(A1)";
          case "00000015-0000-0010-1002-000000101002":
              return "心臟內科/內科/體檢區(A2)";
          case "00000015-0000-0010-1003-000000101003":
              return "心臟內科/內科/體檢區(A3)";
          case "00000015-0000-0010-1004-000000101004":
              return "服務台(A4)";
          case "00000015-0000-0010-1005-000000101005":
              return "服務台(A5)";
          case "00020015-0000-0000-0001-000000000001":
              return "前門出口(A6)";
          case "00020015-0000-0000-0001-000000000002":
              return "前門出口(A7)";
          case "00000015-0000-0010-1011-000000101011":
              return "批價櫃檯(A11)";
          case "00020015-0000-0000-0001-000000000003":
              return "前門出口(A9)";
          case "00000015-0000-0010-1012-000000101012":
              return "精神科/神經內科(A12)";
          case "00000015-0000-0010-1013-000000101013":
              return "26~29診走廊出口(A13)";
          case "00000015-0000-0010-1010-000000101010":
              return "中央走廊(A8)";
          case "00000015-0000-0010-1008-000000101008":
              return "自動繳費機(A10)";
          case "00000015-0000-0010-1014-000000101014":
              return "小兒科(A14)";
          case "00000015-0000-0010-1015-000000101015":
              return "小兒科(A15)";
          case "00000015-0000-0010-1016-000000101016":
              return "健康教育中心(A16)";
          case "00000015-0000-0010-1017-000000101017":
              return "健康教育中心(A17)";
          case "00020015-0000-0000-0001-000000000004":
              return "新大樓一樓樓梯(A18)";
          case "00000015-0000-0010-1019-000000101019":
              return "30~41診走廊交叉口(A19)";
          case "00000015-0000-0010-1020-000000101020":
              return "外科/骨科/牙科(A20)";
          case "00000015-0000-0010-1021-000000101021":
              return "外科/骨科/牙科(A21)";
          case "00000015-0000-0010-1022-000000101022":
              return "腎臟科/腎膽腸內科/新陳代謝分泌科(A22)";
          case "00000015-0000-0010-1023-000000101023":
              return "無障礙領藥窗口(A23)";
          case "0x0454bd410x0155f142":
              return "志工服務台(A25)";
          case "00000015-0000-0010-1025-000000101025":
              return "42~49診走廊交叉口(A26)";
          case "00000015-0000-0010-1026-000000101026":
              return "眼科/皮膚科(A27)";
          case "00000015-0000-0010-1027-000000101027":
              return "眼科/皮膚科(A28)";
          case "00000015-0000-0010-1028-000000101028":
              return "42~49診走廊出口(A29)";
          case "0x0054bd410x0055f142":
              return "藥局(A24)";
          case "0x3b43ba410xcf90f042":
              return "藥局(裡面)(A30)";
          case "00000015-0000-0010-1029-000000101029":
              return "超商左(A31)";
          case "00000015-0000-0010-1030-000000101030":
              return "超商右(A32)";
          case "00000015-0000-0000-0101-000000000101":
              return "新舊大樓連接走廊(C1)";
          case "00170015-0000-0000-0001-000000000001":
              return "樓梯(舊大樓靠新大樓)(C2)";
          case "00000015-0000-0001-8500-000000019000":
              return "樓梯(舊大樓靠電梯)(C3)";
          case "00000015-0000-0000-0404-000000000404":
              return "核子醫學部(C4)";
          case "00000015-0000-0000-0505-000000000505":
              return "抽血(C5)";
          case "00180016-0000-0000-0002-000000000002":
              return "大廳(病歷室前)(C8)";
          case "00000015-0000-0000-0909-000000000909":
              return "大廳(電腦斷層室前)(C11)";
          case "00170015-0000-0000-0001-000000000002":
              return "後門出口(後)(C10)";
          case "00180016-0000-0000-0002-000000000001":
              return "樓梯(舊大樓2F電梯旁)(D1)";
          case "00000016-0000-0002-4000-000000017000":
              return "神經部檢查室/心臟血管功能檢查室岔路(D4)";
          case "00000016-0000-0001-9500-000000017000":
              return "神經部檢查室(D2)";
          case "00000015-0000-0000-0606-000000000606":
              return "迴轉樓梯(神經部前)(D3)";
          case "00000016-0000-0002-8500-000000017000":
              return "心臟血管功能檢查室(D5)";
          case "00180016-0000-0000-0002-000000000003":
              return "迴轉樓梯(心臟血管功能檢查室)(D6)";
          case "00010014-0000-0000-0000-000000000001":
              return "樓梯(新大樓B1)(B1)";
          case "00000014-0000-0010-1103-000000101103":
              return "X光報到處(B3)";
          case "00000014-0000-0010-1102-000000101102":
              return "電梯(B2)";
          default:
              return uuid;
      }

    }

    public int getrssi(int i){
        return rssi.get(i);
    }
    public String getrssilist(){
        return rssi.toString();
    }
    public String getrssilist_delete_extremevalue(){
        return rssi_delete_extreme_value.toString();
    }
    public String getrssilistsize(){
        return String.valueOf(rssi.size());
    }
    public int getrssi(){
        return rssi.get(rssi.size()-1);
    }
    public int countsum(){
        int count=0;
        for (int i:rssi){
            count += i;
        }
        return count;
    }
    public float countavg(){
        float count=0,num=0;
        for (int i:rssi){
           count += i;
           num ++;
        }
        return (count/num);
    }

    public float getmiddlenum(){
        int middlenum = 0, middlesize = 0;
        List<Integer> tmp_list = new ArrayList<>();
        tmp_list.clear();
        tmp_list.addAll(rssi);
        Collections.sort(tmp_list);
        middlesize = tmp_list.size()/2;
        middlenum = tmp_list.get(middlesize);
        return  middlenum;
    }

    public float countStandard_Deviation(){
        float count=0,num=0,avg=0,standard=0,base=0;
        for (int i:rssi){
            count += i;
            num ++;
        }
        avg = count/num;
        for (int i:rssi){
            base = (float) Math.pow(i-avg,2);
            standard = standard + base;
        }
        standard = (float) Math.sqrt((standard/num));

        return standard;
    }


    public void set_sort_way(int sort_way){
        this.sort_ways = sort_way;
    }



    @Override
    public int compareTo(@NonNull siganl_data_type f) {
        switch (sort_ways) {
            case 1:
                if (this.countavg() < f.countavg()) {
                    return 1;
                } else if (this.countavg() > f.countavg()) {
                    return -1;
                } else {
                    return 0;
                }
            default:
                if (this.countsum() < f.countsum()) {
                    return 1;
                } else if (this.countsum() > f.countsum()) {
                    return -1;
                } else {
                    return 0;
                }
        }
    }
}
