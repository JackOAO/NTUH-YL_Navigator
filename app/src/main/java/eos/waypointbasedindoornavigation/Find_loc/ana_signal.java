package eos.waypointbasedindoornavigation.Find_loc;

import android.util.Log;
import android.util.LongSparseArray;

import eos.waypointbasedindoornavigation.GeoCalulation;
import eos.waypointbasedindoornavigation.Node;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.xml.transform.dom.DOMLocator;

import static java.lang.Math.pow;

public class ana_signal {
    private Queue<siganl_data_type> weight_queue = new LinkedList<>();
    private  List<siganl_data_type> data_list = new ArrayList<>();
    private int weight_size = 5;
    private boolean FirstTime = true;
    private static DeviceParameter dp = new DeviceParameter();
    private static ReadWrite_File wf = new ReadWrite_File();
    private static float distance = 0;
    private static Node[] tmp_path;
    private static HashMap<String, Node> allWaypointData = new HashMap<>();
    public void set_path(List<Node> tmp_path){
        this.tmp_path = new Node[tmp_path.size()];
        for (int i = 0; i < tmp_path.size(); i++) {
            this.tmp_path[i] = tmp_path.get(i);
            Log.i("path", tmp_path.get(i).getID());
        }
    }
    public void set_allWaypointData(HashMap<String, Node> allWaypointData){
        this.allWaypointData = allWaypointData;
    }
    public List<String> ana_signal(Queue q, int algo_Type, int weight_type, float remind_range, double offset) {
        List lq = new ArrayList<String>(q);
        List<String> tmp_data_list = new ArrayList<>();
        data_list.clear();
//        class the datalist to UUID,RSSIList
//        List<siganl_data_type> data_list = new ArrayList<>();
        for (int i = 0; i < q.size(); i++){
            if (tmp_data_list.indexOf(((List<String>) lq.get(i)).get(0)) == -1) {
                tmp_data_list.add(((List<String>) lq.get(i)).get(0));
                data_list.add(new siganl_data_type(((List<String>) lq.get(i)).get(0),
                        Integer.parseInt(((List<String>) lq.get(i)).get(1))));
            }
            else{
                data_list.get(tmp_data_list.indexOf(((List<String>) lq.get(i)).get(0))).
                        setvalue(Integer.parseInt(((List<String>) lq.get(i)).get(1)));
            }
        }
//        find difference between first and second higher RSSI of UUID
        List<String> location_range = new ArrayList<>();
        if (data_list.size() > 1) {
            for (int i = 0; i < data_list.size(); i++)
                data_list.get(i).set_sort_way(1);
            Collections.sort(data_list);

            for(int i = 0; i< data_list.size(); i++)
                Log.i("xxx_datalist","Value(" + i +") = " + data_list.get(i).getUuid() + " Rssilist = " + data_list.get(i).getrssilist() + " Rssi = " + data_list.get(i).countavg());
            List<Float> tmp_count_dif = ana_signal_6(data_list, remind_range, offset);
            if (tmp_count_dif != null)
                if (tmp_count_dif.size() > 2) {
                    float tmp_dif = Math.abs(data_list.get(0).countavg()
                            - data_list.get(Math.round(tmp_count_dif.get(2))).countavg());
                    Log.i("tmp_count_dif1",  data_list.get(0).getUuid_Name()+
                            data_list.get(0).getrssilist().toString() + " " +
                            String.valueOf(data_list.get(0).countavg()) + "\t" +
                            data_list.get(Math.round(tmp_count_dif.get(2))).getUuid_Name() +
                            data_list.get(Math.round(tmp_count_dif.get(2))).getrssilist().toString() + " " +
                            String.valueOf(data_list.get(1).countavg()));
                   if(data_list.get(0).countavg() > tmp_count_dif.get(1)) {
                       if(FirstTime == true &&  data_list.get(0).countavg() > tmp_count_dif.get(1)){
                           Log.i("def_range", "close " + data_list.get(0).getUuid());
                           Log.i("tmp_count", "threshold = " + tmp_count_dif.get(1));
                           location_range.add("close");
                           location_range.add(data_list.get(0).getUuid());
                           //SignalLog("Close Beacon");
                           FirstTime = false;
                       }else if (FirstTime == false && tmp_dif > tmp_count_dif.get(0) &&
                              data_list.get(0).countavg() > tmp_count_dif.get(1)) {
                           Log.i("def_range", "close " + data_list.get(0).getUuid());
                           Log.i("tmp_count", "threshold = " + tmp_count_dif.get(1));
                           location_range.add("close");
                           location_range.add(data_list.get(0).getUuid());

                       }
                   }
                    else {
                        Log.i("def_range", "near " + data_list.get(0).getUuid());
                        Log.i("tmp_count","threshold = " + tmp_count_dif.get(1));
                        location_range.add("near");
                        location_range.add(data_list.get(0).getUuid());
                    }
                }else {
                    int tmp_dif2 = Math.round(data_list.get(0).countavg());
                    if (tmp_dif2 > dp.get_Paramater(data_list.get(0).getUuid())) {
                        location_range.add("close");
                        location_range.add(data_list.get(0).getUuid());
                    }
                    else {
                        Log.i("def_range", "near " + data_list.get(0).getUuid());
                        location_range.add("near");
                        location_range.add(data_list.get(0).getUuid());
                    }
                }
        }
        else {
            int tmp_dif = Math.round(data_list.get(0).countavg());
            if (tmp_dif > -60) {
                location_range.add("close");
                location_range.add(data_list.get(0).getUuid());
            }
            else {
                Log.i("def_range", "near " + data_list.get(0).getUuid());
                location_range.add("near");
                location_range.add(data_list.get(0).getUuid());
            }
        }
        List<Float> weight_list = weight_type(weight_type);
        weight_queue.add(new siganl_data_type(
                data_list.get(0).getUuid(), Math.round(data_list.get(0).countavg())));
        if (weight_queue.size() > weight_size) {
            weight_queue.poll();
        }
        List<siganl_data_type> get_weight_data = new ArrayList<>(weight_queue);
        Collections.reverse(get_weight_data);
        List<siganl_data_type> count_data_weight = Positioning_Algorithm(get_weight_data, weight_list, algo_Type);
        List<String> tmp_return = new ArrayList<>();
        tmp_return.add(count_data_weight.get(0).getUuid());
        tmp_return.addAll(location_range);
        return tmp_return;
    }
    //    -------------------------------------------------------------------------------------


    //    -------------------------------------------------------------------------------------
//    weight type list
    private List<Float> weight_type(int T) {
        List<Float> weight_list = new ArrayList<>();
        switch (T) {
            case 1:
                for (int i = 0; i < weight_size + 2; i++) {
//                weight_list.add((int) Math.pow(2, i));
                    if (i < 2)
                        weight_list.add((float) 1);
                    else
                        weight_list.add(weight_list.get(i - 1) + weight_list.get(i - 2));
                }
                Log.i("weight1", weight_list.toString());
                return weight_list;
            case 2:
                for (int i = 0; i < weight_size + 2; i++)
                    weight_list.add((float) pow(2, i));
                return weight_list;
            case 3:
                for (int i = 0; i < weight_size + 2; i++)
                    weight_list.add((float)(Math.log10(i)*10));
                Log.i("weight3", weight_list.toString());
                return weight_list;
            default:
                for (int i = 2; i < weight_size + 2; i++)
                    weight_list.add((float)i);
                return weight_list;
        }
    }
    //    -------------------------------------------------------------------------------------
//    Positioning_Algorithm
    private List<siganl_data_type> Positioning_Algorithm
    (List<siganl_data_type> get_weight_data, List<Float> weight_list, int T) {
        switch (T) {
            case 1:
                return ana_signal_1(get_weight_data, weight_list);
            case 2:
                return ana_signal_2(get_weight_data, weight_list);
            case 3:
                return ana_signal_3(get_weight_data, weight_list);
            default:
                return ana_signal_1(get_weight_data, weight_list);
        }
    }

    private List<siganl_data_type> ana_signal_1
            (List<siganl_data_type> get_weight_data, List<Float> weight_list) {
        Log.i("def_algo", "algo1");
        List<String> tmplistUUID = new ArrayList<>();
        List<siganl_data_type> count_data_weight = new ArrayList<>();
        for (int i = 0; i < get_weight_data.size(); i++) {
            if (tmplistUUID.indexOf(get_weight_data.get(i).getUuid()) == -1) {
                tmplistUUID.add(get_weight_data.get(i).getUuid());
                count_data_weight.add(new siganl_data_type(get_weight_data.get(i).getUuid()
                        , Math.round((get_weight_data.get(i).getrssi()) * weight_list.get(i))));
            } else {
                count_data_weight.get(
                        tmplistUUID.indexOf(get_weight_data.get(i).getUuid())).
                        setvalue(Math.round(get_weight_data.get(i).getrssi() * weight_list.get(i)));
            }
        }
        tmplistUUID.clear();
        Collections.sort(count_data_weight);
        return count_data_weight;
    }
    private List<siganl_data_type> ana_signal_2
            (List<siganl_data_type> get_weight_data, List<Float> weight_list) {
        Log.i("def_algo", "algo2");
        List<String> tmplistUUID = new ArrayList<>();
        List<siganl_data_type> count_data_weight = new ArrayList<>();
        for (int i = 0; i < get_weight_data.size(); i++) {
            if (tmplistUUID.indexOf(get_weight_data.get(i).getUuid()) == -1) {
                tmplistUUID.add(get_weight_data.get(i).getUuid());
                count_data_weight.add(new siganl_data_type(get_weight_data.get(i).getUuid()
                        , Math.round(weight_list.get(i))));
            } else {
                count_data_weight.get(
                        tmplistUUID.indexOf(get_weight_data.get(i).getUuid())).
                        setvalue(Math.round(weight_list.get(i)));
            }
        }
        tmplistUUID.clear();
        Collections.sort(count_data_weight);
        return count_data_weight;
    }
    private List<siganl_data_type> ana_signal_3
            (List<siganl_data_type> get_weight_data,
             List<Float> weight_list) {
        Log.i("def_algo", "algo3");
        List<String> tmplistUUID = new ArrayList<>();
        List<siganl_data_type> count_data_weight = new ArrayList<>();

        for (int i = 0; i < get_weight_data.size(); i++) {
            if (tmplistUUID.indexOf(get_weight_data.get(i).getUuid()) == -1) {
                tmplistUUID.add(get_weight_data.get(i).getUuid());
                count_data_weight.add(new siganl_data_type(get_weight_data.get(i).getUuid()
                        , Math.round(get_weight_data.get(i).getrssi()*weight_list.get(i))));
            } else {
                count_data_weight.get(
                        tmplistUUID.indexOf(get_weight_data.get(i).getUuid())).
                        setvalue(Math.round(get_weight_data.get(i).getrssi()*weight_list.get(i)));
            }
        }
        tmplistUUID.clear();

        Collections.sort(count_data_weight);
        return count_data_weight;
    }

    List<Float> tmp_returen = new ArrayList<>();
    private List<Float> ana_signal_6
            (List<siganl_data_type> data_list, float remind_range,double offset) {
//       計算距離
        Log.i("algo6", "in algo6" + String.valueOf(data_list.size()));
        Node[] tmp_dis_Node = new Node[2];
        tmp_dis_Node[0] = allWaypointData.get(data_list.get(0).getUuid());
        Log.i("algo6 tmp_dis_Node[0]", tmp_dis_Node[0].getID());
        List<String> Neighbornodes = tmp_dis_Node[0].getNeighborIDs();
        Log.i("algo6 neig", Neighbornodes.toString());
        boolean tmp_br = false;
        int tmp_compare = 99999;
        for (int i= 1; i< data_list.size(); i++) {
            for (String tmp_Neighbornodes : Neighbornodes) {
                Log.i("algo6 neigpa", tmp_Neighbornodes + "***\t" + data_list.get(i).getUuid());
                if (tmp_Neighbornodes.equals(data_list.get(i).getUuid())) {
                    tmp_dis_Node[1] = allWaypointData.get(tmp_Neighbornodes);
                    tmp_compare = i;
                    tmp_br = true;
                    break;
                }else
                    tmp_dis_Node[1] = null;
            }
            if (tmp_br) break;
        }
        try {
            if (data_list.size() > 1) {
                Log.i("algo6","in try");
                if (tmp_dis_Node[1] != null && tmp_dis_Node[0]!=null)
                    distance = GeoCalulation.getDistance(tmp_dis_Node[0], tmp_dis_Node[1]);
                else {
//                    distance = 0;
                    Log.i("algo6TDN0","null error");
                    return null;
                }
                tmp_returen.clear();
                Log.i("algo6", String.valueOf(distance));
                double[] tmp_difference = new double[2];
                tmp_difference[0] = count_Quadratic(data_list.get(0).getUuid(), remind_range, offset);
                tmp_difference[1] = count_Quadratic(data_list.get(1).getUuid(), distance - remind_range, offset);
                Log.i("algo6", String.valueOf(tmp_difference[0]) + "\t" + String.valueOf(tmp_difference[1]));
                tmp_returen.add((float) Math.abs(tmp_difference[0] - tmp_difference[1]));
                tmp_returen.add((float) (tmp_difference[0]));
                tmp_returen.add((float) tmp_compare);
                Log.i("algo6 return", tmp_returen.toString());
                return tmp_returen;
            } else {
                Log.i("algo6TDN1","null error1");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("algo6TDN2","null error2");
            return null;
        }
    }


    private double count_Quadratic(String s,double range, double offset){
        double a_value = dp.get_a(s);
        double b_value = dp.get_b(s);
        double c_vaule = dp.get_c(s);
        if(getType(s) == 1)
            return -0.3469 * pow(range,3) + 4.576 * pow(range,2) - 20.671 * range -21.034 + offset;
        else if(getType(s) == 2)
            return -0.1944 * pow(range,3) + 3.0498 * pow(range,2) - 16.226 * range - 28.063 + offset;
        else if(getType(s) == 3)
            return 0.0898 * pow(range,3) - 0.803 * pow(range,2) - 2.1311 * range -36.833 + offset;
        else
            return (a_value*pow(range + dp.get_Paramater(s),2) + b_value* (range + dp.get_Paramater(s)) + c_vaule) + offset;
    }

    private int getType(String uuid)
    {
        switch (uuid)
        {
            case "00000014-0000-0010-1103-000000101103"://X光報到處
            case "00030018-0000-0003-1500-000000033000"://五樓電梯
            case "00000015-0000-0010-1019-000000101019"://A19骨科路口
                return 1;
            case "00000014-0000-0010-1102-000000101102"://B2電梯
            case "00000015-0000-0010-1024-000000101024"://A24領藥處
            case "00030018-0000-0002-8000-000000021000"://5B中庭
            case "00000015-0000-0010-1018-000000101018"://A18樓梯
                return 2;
            case "00010014-0000-0000-0000-000000000001"://B1樓梯
            case "00000015-0000-0001-8500-000000011500"://C10電梯
            case "00000015-0000-0010-1021-000000101021"://A21骨科
            case "00030018-0000-0002-9500-000000027000"://5B配膳室
                return 3;
            default:
                return 0;
        }
    }

}