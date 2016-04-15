package com.tafayor.selfcamerashot.taflib.helpers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Created by youssef on 26/12/13.
 */
public class SensorsHelper
{
   
 

    
  
    
    
    

    public  static boolean isFusionAvailable(Context ctx)
    {
        return (isAccelerometerSensorAvailable(ctx) && isMagneticSensorAvailable(ctx)
        && isGyroscopeSensorAvailable(ctx));
    }


    public  static float getAccelMaxRange(Context ctx)
    {
        SensorManager manager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        float range = accelSensor.getMaximumRange() * SensorManager.GRAVITY_EARTH;

        return range;
    }


    public  static boolean isAccelerometerSensorAvailable(Context ctx)
    {
        boolean found = false;

        SensorManager sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null)
        {
            found = true;
        }


        return found;
    }


    public  static boolean isMagneticSensorAvailable(Context ctx)
    {
        boolean found = false;

        SensorManager sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (accelerometerSensor != null) {
            found = true;
        }


        return found;
    }


    public  static boolean isGyroscopeSensorAvailable(Context ctx)
    {
        boolean found = false;

        SensorManager sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (sensor != null) {
            found = true;
        }

        return found;
    }
}
