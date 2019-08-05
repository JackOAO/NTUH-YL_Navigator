package eos.waypointbasedindoornavigation;
public class outpatient_list_class{
    private String room_num, call_num;
    private String div_name, clin_name, dr_name;
    public outpatient_list_class(String room_num, String div_name, String clin_name, String dr_name, String call_num) {
        this.room_num = room_num;
        this.div_name = div_name;
        this.clin_name = clin_name;
        this.dr_name = dr_name;
        this.call_num = call_num;
    }
    public String getroom_num(){
        return room_num;
    }
    public void setroom_num(String room_num){
        this.room_num = room_num;
    }
    public String getdiv_name(){
        return div_name;
    }
    public void setdiv_name(String div_name){
        this.div_name = div_name;
    }
    public String getclin_name(){
        return clin_name;
    }
    public void setclin_name(String clin_name){
        this.clin_name = clin_name;
    }
    public String getdr_name(){
        return dr_name;
    }
    public void setdr_name(String dr_name){
        this.dr_name = dr_name;
    }
    public String getcall_num(){
        return call_num;
    }
    public void setcall_num(String call_num){
        this.call_num = call_num;
    }
}