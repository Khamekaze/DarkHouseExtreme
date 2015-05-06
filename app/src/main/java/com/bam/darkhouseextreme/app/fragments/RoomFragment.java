package com.bam.darkhouseextreme.app.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.content.res.TypedArray;
import android.graphics.Point;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bam.darkhouseextreme.app.R;
import com.bam.darkhouseextreme.app.adapter.Shaker;
import com.bam.darkhouseextreme.app.helper.DatabaseHelper;
import com.bam.darkhouseextreme.app.model.Item;
import com.bam.darkhouseextreme.app.utilities.SaveUtility;
import com.bam.darkhouseextreme.app.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chobii on 29/04/15.
 */
public class RoomFragment extends Fragment {

    private final String LOG_DATA = RoomFragment.class.getSimpleName();

    private View root;
    private Button buttonUp, buttonDown, buttonLeft, buttonRight, itemButton1, itemButton2, itemButton3;
    private Context context;
    private ImageView roomImage;
    private int x_cord, y_cord, score;
    private String itemPickedUpTag;
    private DatabaseHelper helper;

    private List<Button> eventsInRoom = new ArrayList<>();

    private SensorManager sManager;
    private Sensor sensor;
    private Shaker shaker;

    private Animation animation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        createButtons();

        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shaker = new Shaker();

        shaker.setShakeListener(new Shaker.OnShakeListener() {
            @Override
            public void shake(int count) {
                handleShake(count);
            }
        });


//        root = inflater.inflate(R.layout.room, container, false);
        root = placeItems(inflater.inflate(R.layout.room, container, false));

        roomImage = (ImageView) root.findViewById(R.id.roomImage);
        helper = new DatabaseHelper(context);


        buttonUp = (Button) root.findViewById(R.id.buttonUp);
        buttonDown = (Button) root.findViewById(R.id.buttonDown);
        buttonLeft = (Button) root.findViewById(R.id.buttonLeft);
        buttonRight = (Button) root.findViewById(R.id.buttonRight);


        animation = AnimationUtils.loadAnimation(context, R.anim.alpha_button);

        int[] stats = SaveUtility.loadStats();
        x_cord = stats[0];
        y_cord = stats[1];
        score = stats[2];

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
                        if (!isRoom(x_cord, y_cord += 1)) {
                            y_cord -= 1;
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
                        if (!isRoom(x_cord, y_cord -= 1)) {
                            y_cord += 1;
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

    private void setItemButtons() {
        Log.d(LOG_DATA, " start of setItemButtons");
        for (int i = 1; i < 4; i++) {
            int itemID;
            try {
                itemID = context.getResources().getIdentifier(
                        "item" + i + "" + String.valueOf(x_cord) + "" + String.valueOf(y_cord), "id", context.getPackageName());
                Log.d(LOG_DATA, "item" + i + String.valueOf(x_cord) + "" + String.valueOf(y_cord));
                Log.d(LOG_DATA, "item id = " + itemID);
            } catch (Exception e) {
                itemID = 0;
            }
            if (itemID != 0) {
                switch (i) {
                    case 1:
                        itemButton1 = (Button) root.findViewById(itemID);
                        Log.d(LOG_DATA, "Button 1 tag: " + itemButton1.getTag().toString());
                        break;
                    case 2:
                        itemButton2 = (Button) root.findViewById(itemID);
                        break;
                    case 3:
                        itemButton3 = (Button) root.findViewById(itemID);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void setPickUpItem(final Button itemButton) {

        if (itemButton != null) {
            itemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemButton.setVisibility(View.VISIBLE);
                    itemButton.setClickable(false);
                    itemPickedUpTag = itemButton.getTag().toString();
                    final int itemID = Utilities.isViableItem(itemPickedUpTag, context, x_cord, y_cord);
                    if (itemID != 0) {
                        itemButton.setBackgroundResource(itemID);
                        Item item = helper.getOneItem(itemPickedUpTag);
                        SaveUtility.saveItemToCharacter(item);
                        Animation fadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);

                        itemButton.startAnimation(fadein);

                        fadein.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation fadeout = AnimationUtils.loadAnimation(context, R.anim.fade_out);
//                                fadeout.setFillAfter(true);
                                itemButton.startAnimation(fadeout);
                                fadeout.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        itemButton.setBackgroundResource(R.drawable.placeholder);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });


                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    } else {
                        noItemMessage();
                    }
                }
            });
        }
    }

    private void changeRoom(final int roomId) {

        Log.d(LOG_DATA, String.valueOf(x_cord));
        Log.d(LOG_DATA, String.valueOf(y_cord));
        setItemButtons();
        for(Button b : eventsInRoom) {
            setPickUpItem(b);

        }

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

    private void noItemMessage() {
        Toast.makeText(context, "Nothing to be found here", Toast.LENGTH_LONG)
                .show();
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
            SaveUtility.saveProgress(x, y, score += 10);
            return true;
        } else return false;

    }

    private void continueIfApplicable(int x, int y) {
        String room = String.valueOf(x) + String.valueOf(y);
        final int roomId;
        roomId = Utilities.isViableRoom(room, context);
        roomImage.setImageResource(roomId);
    }

    private void handleShake(int count) {
        Log.d(LOG_DATA, "shake that ass");
        for (final Button event : eventsInRoom) {
            if (event != null) {
//                event.setVisibility(View.VISIBLE);
                event.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.d(LOG_DATA, String.valueOf(animation.isInitialized()));
                        event.setBackgroundResource(R.drawable.item_button);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        event.setBackgroundResource(R.drawable.placeholder);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

//                event.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void createButtons() {

        itemButton1 = new Button(context);
        itemButton2 = new Button(context);
        itemButton3 = new Button(context);

        String coordinates = String.valueOf(x_cord) + String.valueOf(y_cord);

        itemButton1.setTag(1);
        itemButton2.setTag(2);
        itemButton3.setTag(3);
        itemButton1.setBackgroundResource(R.drawable.placeholder);
        itemButton2.setBackgroundResource(R.drawable.placeholder);
        itemButton3.setBackgroundResource(R.drawable.placeholder);
//        itemButton1.setVisibility(View.INVISIBLE);
//        itemButton2.setVisibility(View.INVISIBLE);
//        itemButton3.setVisibility(View.INVISIBLE);
        eventsInRoom.add(itemButton1);
        eventsInRoom.add(itemButton2);
        eventsInRoom.add(itemButton3);

        switch (coordinates) {
            case "00":
        itemButton1.setId((R.id.item100));
//                itemButton2.setId((R.id.item200));
//                itemButton3.setId((R.id.item300));
                break;
            default:
                break;
        }
    }


    public RelativeLayout placeItems(View root) {
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;

        int screenHeight = size.y - 100;
        RelativeLayout mainRelativeLayout = (RelativeLayout) root.findViewById(R.id.mainRel);

        RelativeLayout.LayoutParams buttonDetails = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        buttonDetails.setMargins((screenWidth / 2) + 200, (screenHeight / 3), 0, 0);
        mainRelativeLayout.addView(itemButton1, buttonDetails);
//        buttonDetails.setMargins(200, (screenHeight/3)*2, 0, 0);
//        mainRelativeLayout.addView(itemButton2, buttonDetails);


        return mainRelativeLayout;
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
