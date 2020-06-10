package eos.waypointbasedindoornavigation;

/*--

Module Name:

    RecyclerViewAdapter.java

Abstract:

    This module works as an adapter between waypoint information
    and location UI display

Author:

        Phil Wu 01-Feb-2018

        --*/

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import eos.waypointbasedindoornavigation.RecyclerViewAdapter.MyViewHolder;

import java.util.Collections;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private LayoutInflater inflater;

    List<Node> data = Collections.emptyList();
    Boolean clicked = false;
    Context context;
    String[] listItem;
    String AR_MOD;
    String NORMAL_MOD;
    String TITLE;

    public RecyclerViewAdapter(Context context, List<Node> data){
        Log.i("xxx_List","RecyclerViewAdapter");
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        Context appContext = GetApplicationContext.getAppContext();
        SharedPreferences languagePref = PreferenceManager.getDefaultSharedPreferences(appContext);
        String language_option = languagePref.getString("language","繁體中文");
        if(language_option.equals("繁體中文"))
        {
            AR_MOD = "AR模式";
            NORMAL_MOD = "一般模式";
            TITLE = "模式選擇";
        }
        else  if(language_option.equals("English")) {
            AR_MOD = "AR Mode";
            NORMAL_MOD = "Normal Mode";
            TITLE = "Mode Select";
        }
        listItem = new String[]{AR_MOD,NORMAL_MOD};
    }
    @Override

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("xxx_List","onCreateViewHolder");
        View view = inflater.inflate(R.layout.rowitem, null);
        MyViewHolder holder = new MyViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Log.i("xxx_List", "onBindViewHolder Postion = " + position);
        final Node current = data.get(position);
        // determine which of location information to be displayed on UI
        holder.title.setText(current.getName());
        //holder.region.setText(current.get_regionID());
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        holder.title.setWidth(width);
        // an onclick listener for location names in ListViewActivity

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicked == false) {
                    clicked = true;
                    Log.i("xxx_List", "title");
                    // send Name, ID and Region of the selected location
                    //to MainActivity
                    //Intent i = new Intent(context, NavigationActivity.class);
                    //i.putExtra("destinationName", current.getName());
                    //i.putExtra("destinationID", current.getID());
                    //i.putExtra("destinationRegion", current.get_regionID());
                    //context.startActivity(i);
                    //((Activity) context).finish();
                    new AlertDialog.Builder(RecyclerViewAdapter.this.context).setTitle(TITLE)
                            .setItems(listItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(i == 0)//AR Mode
                                    {
                                        Intent intent = new Intent(context,ARModeTeachingActivity.class);
                                        intent.putExtra("destinationName", current.getName());
                                        intent.putExtra("destinationID", current.getID());
                                        intent.putExtra("destinationRegion", current.get_regionID());
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                    }
                                    else if(i == 1)//Normal Mode
                                    {
                                        Intent intent = new Intent(context,NavigationActivity.class);
                                        intent.putExtra("destinationName", current.getName());
                                        intent.putExtra("destinationID", current.getID());
                                        intent.putExtra("destinationRegion", current.get_regionID());
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                    }
                                }
                            }).show();

                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private Button title;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (Button) itemView.findViewById(R.id.listText);

        }
    }
}
