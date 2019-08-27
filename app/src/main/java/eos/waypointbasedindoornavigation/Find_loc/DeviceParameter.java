package eos.waypointbasedindoornavigation.Find_loc;

import android.content.Context;
import android.util.Log;

import eos.waypointbasedindoornavigation.Node;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class DeviceParameter {
    private static final String n_value = "n";
    private static final String id = "id";
    private static final String R0 = "R0";
    private static final String name = "name";
    private static final String parameter = "parameter";
    private static final String install_hight = "install_hight";
    private static final String a_value = "a";
    private static final String b_value = "b";
    private static final String c_value = "c";
    private static HashMap<String, Node> allWaypointData = new HashMap<>();
    private static JSONArray jarray = new JSONArray();
    private ReadWrite_File wf= new ReadWrite_File();
    private static Context c;

    public void DeviceParameter(){
        Log.i("init", "DeviceParameter set");
    }
    public void set_allWaypointData(HashMap<String, Node> allWaypointData){
        this.allWaypointData = allWaypointData;
        Log.i("init", "set allWaypointData");
    }
    public void setupDeviceParameter(Context c) {
        Log.i("setupDeviceParameter","setupDeviceParameter");
        this.c = c;
        jarray = wf.ReadJsonFile(c);
        if (jarray == null) initdivice();
        else {
            try {
                List<String> con_dif0 = new ArrayList<>();
                List<String> con_dif1 = new ArrayList<>();
                for (Node tmp_node : allWaypointData.values()) {
                    con_dif0.add(tmp_node.getID());
                }
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject tmp_jobject = jarray.getJSONObject(i);
                    con_dif1.add(tmp_jobject.getString(this.id));
                }
                //所有Node UUID list
                Log.i("JSONtag0",con_dif0.toString());
                //檔案中有的uuid
                Log.i("JSONtag1",con_dif1.toString());
                con_dif0.removeAll(con_dif1);
                //缺少的Node list
                Log.i("JSONtag1",con_dif0.toString());
                if (!con_dif0.isEmpty()){
                    //檔案裡面的資訊
                    Log.i("JSONtag2",jarray.toString());
                    for (String tmp_arraylist: con_dif0){
                        for (Node tmp_node : allWaypointData.values()) {
                            if(tmp_node.getID().equals(tmp_arraylist)) {
                                JSONObject tmp_add_jobject = new JSONObject();
                                tmp_add_jobject.put(this.id, tmp_arraylist);
                                tmp_add_jobject.put(this.name, tmp_node.getName());
                                tmp_add_jobject.put(this.parameter, 0.0);
                                tmp_add_jobject.put(this.R0, -45);
                                tmp_add_jobject.put(this.n_value, -2.14);
                                tmp_add_jobject.put(this.install_hight, 1.5);
                                tmp_add_jobject.put(this.a_value, 0.1175);
                                tmp_add_jobject.put(this.b_value, -3.4473);
                                tmp_add_jobject.put(this.c_value,  -49.029);
                                jarray.put(tmp_add_jobject);
                                break;
                            }
                        }
                    }
                    //將資訊加入預設值，建立最新的JSON File
                    Log.i("JSONtag3",jarray.toString());
                    wf.writejson(jarray.toString());
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public double get_Paramater(String s){
        for (int i=0; i < jarray.length(); i ++){
            try {
                JSONObject tmp_jobject = jarray.getJSONObject(i);
                if(tmp_jobject.getString(this.id).equals(s)){
                    return tmp_jobject.getDouble(this.parameter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    //---------------New Type For ax^2 + bx + c----------------------
    public double get_a(String s){
        for (int i=0; i < jarray.length(); i ++){
            try {
                JSONObject tmp_jobject = jarray.getJSONObject(i);
                if(tmp_jobject.getString(this.id).equals(s)){
                    return tmp_jobject.getDouble(this.a_value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    public double get_b(String s){
        for (int i=0; i < jarray.length(); i ++){
            try {
                JSONObject tmp_jobject = jarray.getJSONObject(i);
                if(tmp_jobject.getString(this.id).equals(s)){
                    return tmp_jobject.getDouble(this.b_value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    public double get_c(String s){
        for (int i=0; i < jarray.length(); i ++){
            try {
                JSONObject tmp_jobject = jarray.getJSONObject(i);
                if(tmp_jobject.getString(this.id).equals(s)){
                    return tmp_jobject.getDouble(this.c_value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    //----------------------------------------------------------------------
    public Boolean our_Beacon(String s){
        for (int i=0; i < jarray.length(); i ++){
            try {
                JSONObject tmp_jobject = jarray.getJSONObject(i);
                if(tmp_jobject.getString(this.id).equals(s)){
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void initdivice(){
        try {
            JSONArray tmp_jarray = new JSONArray();
            for (Node tmp_node : allWaypointData.values()) {
                JSONObject tmp_add_jobject = new JSONObject();
                tmp_add_jobject.put(this.id, tmp_node.getID());
                tmp_add_jobject.put(this.parameter, 0);
                tmp_add_jobject.put(this.R0, -45);
                tmp_add_jobject.put(this.n_value, -2.14);
                tmp_add_jobject.put(this.install_hight, 1.5);
                tmp_jarray.put(tmp_add_jobject);
            }
            Log.i("inijson", tmp_jarray.toString());
            jarray = tmp_jarray;
            wf.writejson(tmp_jarray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
