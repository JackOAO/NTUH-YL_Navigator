package eos.waypointbasedindoornavigation;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class DirectionSensorUtils implements SensorEventListener {
    private SensorManager sensorManager;
    private String direction;
    float[] accelerometerValues = new float[3];
    float[] magneticValues = new float[3];

    public DirectionSensorUtils(Context context)
    {
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    }

    public void registerSensor()
    {
        //SensorSetup
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this,accelerometerSensor,Sensor.TYPE_ACCELEROMETER,1000000);
        sensorManager.registerListener(this,magneticSensor,Sensor.TYPE_MAGNETIC_FIELD,1000000);
    }
    public void unregisterSensor()
    {
        if(sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelerometerValues = event.values.clone();
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magneticValues = event.values.clone();

        float[] R = new float[9];
        float[] values = new float[3];
        SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticValues);
        SensorManager.getOrientation(R, values);
        countDirection(values);
    }

    public void countDirection(float[] values)
    {
        float degree = (float) Math.toDegrees(values[0]);
        if(degree >= -22 && degree <22) {
            direction = "北";
            Log.i("countDirection", "北");
        }
        else if(degree >= 22 && degree <67)
        {
            direction = "東北";
            Log.i("countDirection", "東北");
        }
        else if(degree >= 67 && degree < 112){
            direction = "東";
            Log.i("countDirection", "東");
        }
        else if(degree >= 112 && degree <157)
        {
            direction = "東南";
            Log.i("countDirection", "東南");
        }
        else if((degree >= 157 && degree <= 180) || (degree >= -180 && degree < -157)){
            direction = "南";
            Log.i("countDirection", "南");
        }
        else if(degree >= -157 && degree < -112)
        {
            direction = "西南";
            Log.i("countDirection", "西南");
        }
        else if(degree >= -112 && degree < -67){
            direction = "西";
            Log.i("countDirection", "西");
        }
        else if(degree >= -67 && degree < -22)
        {
            direction = "西北";
            Log.i("countDirection", "西北");
        }
    }

    public String getDirection()
    {
        return direction;
    }
    public int getDirectionNum()
    {
        int returnNum = -1;
        switch (direction)
        {
            case "北":
                returnNum = 0;
                break;
            case "東北":
                returnNum = 1;
                break;
            case "東":
                returnNum = 2;
                break;
            case "東南":
                returnNum = 3;
                break;
            case "南":
                returnNum = 4;
                break;
            case "西南":
                returnNum = 5;
                break;
            case "西":
                returnNum = 6;
                break;
            case "西北":
                returnNum = 7;
                break;
        }
        return returnNum;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
