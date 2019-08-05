package eos.waypointbasedindoornavigation;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter{
    private LayoutInflater myInflater;
    private List<outpatient_list_class> outpatient_list;
    public MyAdapter(Context context, List<outpatient_list_class> outpatient_list){
        myInflater = LayoutInflater.from(context);
        this.outpatient_list = outpatient_list;
    }

    @Override
    public int getCount() {
        return outpatient_list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return outpatient_list.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return outpatient_list.indexOf(getItem(position));
    }

    private class ViewHolder {
        TextView room_num;
        TextView div_name;
        TextView clin_name;
        TextView dr_name;
        TextView call_num;

        public ViewHolder(TextView room_num, TextView div_name, TextView clin_name, TextView dr_name, TextView call_num){
            this.room_num = room_num;
            this.div_name = div_name;
            this.clin_name = clin_name;
            this.dr_name = dr_name;
            this.call_num = call_num;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = myInflater.inflate(R.layout.list_outpatient, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.room_num),
                    (TextView) convertView.findViewById(R.id.div_name),
                    (TextView) convertView.findViewById(R.id.clin_name),
                    (TextView) convertView.findViewById(R.id.dr_name),
                    (TextView) convertView.findViewById(R.id.call_num)
            );
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        outpatient_list_class outpatient = (outpatient_list_class) getItem(position);
        holder.room_num.setText(outpatient.getroom_num());
        holder.div_name.setText(outpatient.getdiv_name());
        holder.clin_name.setText(outpatient.getclin_name());
        holder.dr_name.setText(outpatient.getdr_name());
        holder.call_num.setText(outpatient.getcall_num());

        return convertView;
    }
}
