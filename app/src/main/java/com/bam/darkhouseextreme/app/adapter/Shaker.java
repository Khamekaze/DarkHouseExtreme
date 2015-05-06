package com.bam.darkhouseextreme.app.adapter;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Chobii on 04/05/15.
 */
public class Shaker implements SensorEventListener {

    private final static float SHAKE_THRESHOLD = 2.5f;
    private final static int SHAKE_SLOP_TIME_IN_MS = 2000;
    private final static int SHAKE_RESET_TIME_IN_MS = 10000;

    private long shakeTimeStamp;
    private int shakeCount;

    private OnShakeListener shakeListener;

    public void setShakeListener(OnShakeListener listener) {
        shakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        if (shakeListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD) {
                final long now = System.currentTimeMillis();

                if (shakeTimeStamp + SHAKE_SLOP_TIME_IN_MS > now) {
                    return;
                }

                if (shakeTimeStamp + SHAKE_RESET_TIME_IN_MS < now) {
                    shakeCount = 0;
                }

                shakeTimeStamp = now;
                shakeCount++;

                shakeListener.shake(shakeCount);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnShakeListener {
        void shake(int count);
    }
}
