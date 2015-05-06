package com.bam.darkhouseextreme.app.fragments;

import android.content.Context;
import android.graphics.Point;
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
import com.bam.darkhouseextreme.app.helper.DatabaseHelper;
import com.bam.darkhouseextreme.app.model.Item;
import com.bam.darkhouseextreme.app.utilities.SaveUtility;
import com.bam.darkhouseextreme.app.utilities.Utilities;

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

        // Width and height of window
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

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
        for (int i = 1; i < 4; i++) {
            int itemID;
            try {
                itemID = context.getResources().getIdentifier(
                        "item" + i + "" + String.valueOf(x_cord) + "" + String.valueOf(y_cord), "id", context.getPackageName());
            } catch (Exception e) {
                itemID = 0;
            }
            if (itemID != 0) {
                switch (i) {
                    case 1:
                        itemButton1 = (Button) root.findViewById(itemID);
                        Log.d(LOG_DATA,"Button 1 tag: " + itemButton1.getTag().toString());
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

    private void setPickUpItem() {
        if (itemButton1 != null) {
            itemButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemButton1.setClickable(false);
                    itemPickedUpTag = itemButton1.getTag().toString();
                    final int itemID = Utilities.isViableItem(itemPickedUpTag, context, x_cord, y_cord);
                    if (itemID != 0) {
                        itemButton1.setBackgroundResource(itemID);
                        Item item = helper.getOneItem(itemPickedUpTag);
                        SaveUtility.saveItemToCharacter(item);
                        Animation fadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);

                        itemButton1.startAnimation(fadein);

                        fadein.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation fadeout = AnimationUtils.loadAnimation(context, R.anim.fade_out);
//                                fadeout.setFillAfter(true);
                                itemButton1.startAnimation(fadeout);
                                fadeout.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        itemButton1.setBackgroundResource(R.color.transparent);
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
        if (itemButton2 != null) {
            itemButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemButton2.setClickable(false);
                    itemPickedUpTag = itemButton2.getTag().toString();
                    final int itemID = Utilities.isViableItem(itemPickedUpTag, context, x_cord, y_cord);
                    if (itemID != 0) {
                        itemButton2.setBackgroundResource(itemID);
                        Item item = helper.getOneItem(itemPickedUpTag);
                        SaveUtility.saveItemToCharacter(item);
                        Animation fadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);

                        itemButton2.startAnimation(fadein);

                        fadein.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation fadeout = AnimationUtils.loadAnimation(context, R.anim.fade_out);
//                                fadeout.setFillAfter(true);
                                itemButton2.startAnimation(fadeout);
                                fadeout.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        itemButton2.setBackgroundResource(R.color.transparent);
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
        if (itemButton3 != null) {
            itemButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemButton3.setClickable(false);
                    itemPickedUpTag = itemButton3.getTag().toString();
                    final int itemID = Utilities.isViableItem(itemPickedUpTag, context, x_cord, y_cord);
                    if (itemID != 0) {
                        itemButton3.setBackgroundResource(itemID);
                        Item item = helper.getOneItem(itemPickedUpTag);
                        SaveUtility.saveItemToCharacter(item);
                        Animation fadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);

                        itemButton3.startAnimation(fadein);

                        fadein.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation fadeout = AnimationUtils.loadAnimation(context, R.anim.fade_out);
//                                fadeout.setFillAfter(true);
                                itemButton3.startAnimation(fadeout);
                                fadeout.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        itemButton3.setBackgroundResource(R.color.transparent);
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
        setPickUpItem();

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
