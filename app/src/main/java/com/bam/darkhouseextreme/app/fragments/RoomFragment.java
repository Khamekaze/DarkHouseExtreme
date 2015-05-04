package com.bam.darkhouseextreme.app.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.bam.darkhouseextreme.app.R;
import com.bam.darkhouseextreme.app.adapter.Shaker;
import com.bam.darkhouseextreme.app.utilities.SaveUtility;
import com.bam.darkhouseextreme.app.utilities.Utilities;

import java.util.List;

/**
 * Created by Chobii on 29/04/15.
 */
public class RoomFragment extends Fragment {

    private final String LOG_DATA = RoomFragment.class.getSimpleName();

    private View root;
    private Button buttonUp, buttonDown, buttonLeft, buttonRight;
    private Context context;
    private ImageView roomImage;
    private int x_cord, y_cord, score;

    private List<Button> eventsInRoom;

    private SensorManager sManager;
    private Sensor sensor;
    private Shaker shaker;

    private Animation animation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();

        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shaker = new Shaker();

        shaker.setShakeListener(new Shaker.OnShakeListener() {
            @Override
            public void shake(int count) {
                handleShake(count);
            }
        });

        root = inflater.inflate(R.layout.room, container, false);
        roomImage = (ImageView)root.findViewById(R.id.roomImage);

        buttonUp = (Button)root.findViewById(R.id.buttonUp);
        buttonDown = (Button)root.findViewById(R.id.buttonDown);
        buttonLeft = (Button)root.findViewById(R.id.buttonLeft);
        buttonRight = (Button)root.findViewById(R.id.buttonRight);

        animation = AnimationUtils.loadAnimation(context, R.anim.alpha_button);

        int[] stats = SaveUtility.loadStats();
        x_cord = stats[0];
        y_cord = stats[1];
        score  = stats[2];

        continueIfApplicable(x_cord, y_cord);

        setButtonUp();
        setButtonDown();
        setButtonLeft();
        setButtonRight();

        return root;
    }


    private void setButtonUp() {
        buttonUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isRoom(x_cord, y_cord+=1)) {
                            y_cord-=1;
                            informOfError();
                        }
                    }
                }
        );
    }

    private void setButtonDown() {
        buttonDown.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isRoom(x_cord, y_cord-=1)) {
                            y_cord+=1;
                            informOfError();
                        }
                    }
                }
        );
    }

    private void setButtonLeft() {
        buttonLeft.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isRoom(x_cord -= 1, y_cord)) {
                            x_cord += 1;
                            informOfError();
                        }
                    }
                }
        );
    }

    private void setButtonRight() {
        buttonRight.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isRoom(x_cord += 1, y_cord)) {
                            x_cord -= 1;
                            informOfError();
                        }
                    }
                }
        );
    }

    private void changeRoom(final int roomId) {

        Log.d(LOG_DATA, String.valueOf(x_cord));
        Log.d(LOG_DATA, String.valueOf(y_cord));

        Animation fadeout = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        roomImage.startAnimation(fadeout);

        fadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                roomImage.setImageResource(roomId);
                Animation fadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                roomImage.startAnimation(fadein);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void informOfError() {
        Toast.makeText(context, "Can't go this way", Toast.LENGTH_LONG)
                .show();
    }

    private boolean isRoom(int x, int y) {
        String room = String.valueOf(x) + String.valueOf(y);
        final int roomId;
        if ((roomId = Utilities.isViableRoom(room, context)) != 0) {
            changeRoom(roomId);
            Log.d(LOG_DATA, String.valueOf(x) + ", " + String.valueOf(y) + ", " + String.valueOf(score));
            SaveUtility.saveProgress(x, y, score +=10);
            return true;
        }
        else return false;

    }

    private void continueIfApplicable(int x, int y) {
        String room = String.valueOf(x) + String.valueOf(y);
        final int roomId;
        roomId = Utilities.isViableRoom(room, context);
        roomImage.setImageResource(roomId);
    }

    private void handleShake(int count) {
        Log.d(LOG_DATA, "shake that ass");
        for (Button event : eventsInRoom) {
            if (event != null) {
                event.setVisibility(View.VISIBLE);
                event.startAnimation(animation);
                event.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sManager.unregisterListener(shaker);
    }

    @Override
    public void onResume() {
        super.onResume();
        sManager.registerListener(shaker, sensor, SensorManager.SENSOR_DELAY_UI);
    }
}
