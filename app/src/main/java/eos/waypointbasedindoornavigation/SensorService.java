package eos.waypointbasedindoornavigation;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorService extends Service {
    private SensorManager sm;
    private Sensor aSensor;
    private Sensor mSensor;
    private float[] accelerometerValue;
    private float[] magneticValue;
    private String direction;
    private  SensorEventListener myListener;

    public class LocalBiner extends Binder{
        SensorService getService()
        {
            return SensorService.this;
        }
    }
    private LocalBiner mLocalBinder = new LocalBiner();

    public SensorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        direction = "";
        accelerometerValue = new float[3];
        magneticValue = new float[3];
        myListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                    magneticValue = event.values;
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                    accelerometerValue = event.values;
                getDirecion();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sm.registerListener(myListener, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(myListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        getDirecion();
    }

    public void getDirecion()
    {
        //tempValue[0]: azimuth, rotation around the Z axis.
        //tempValues[1]: pitch, rotation around the X axis.
        //tempValues[2]: roll, rotation around the Y axis.
        float[] tempValues = new float[3];
        float[] tempRotation = new float[9];
        SensorManager.getRotationMatrix(tempRotation,null,accelerometerValue,magneticValue);
        SensorManager.getOrientation(tempRotation,tempValues);

        //toDegrees
        tempValues[0] = (float)Math.toDegrees(tempValues[0]);
        tempValues[1] = (float)Math.toDegrees(tempValues[1]);
        tempValues[2] = (float)Math.toDegrees(tempValues[2]);
        Log.i("tempValues[0]","tempValues[0] : " + tempValues[0]);

        if(tempValues[0] >= -22 && tempValues[0] <22) {
            direction = "北";
            Log.i("facingDirection", "北");

        }
        else if(tempValues[0] >= 22 && tempValues[0] <67)
        {
            direction = "東北";
            Log.i("facingDirection", "東北");
        }
        else if(tempValues[0] >= 67 && tempValues[0] < 112){
            direction = "東";
            Log.i("facingDirection", "東");
        }
        else if(tempValues[0] >= 112 && tempValues[0] <157)
        {
            direction = "東南";
            Log.i("facingDirection", "東南");
        }
        else if((tempValues[0] >= 157 && tempValues[0] <= 180) || (tempValues[0]) >= -180 && tempValues[0] < -157){
            direction = "南";
            Log.i("facingDirection", "南");
        }
        else if(tempValues[0] >= -157 && tempValues[0] < -112)
        {
            direction = "西南";
            Log.i("facingDirection", "西南");
        }
        else if(tempValues[0] >= -112 && tempValues[0] < -67){
            direction = "西";
            Log.i("facingDirection", "西");
        }
        else if(tempValues[0] >= -67 && tempValues[0] < -22)
        {
            direction = "西北";
            Log.i("facingDirection", "西北");
        }
        Log.i("direction","currentDirection : " + direction);
    }
    public String getDirection()
    {
        return direction;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return  mLocalBinder;
    }
}
