package com.bam.darkhouseextreme.app.fragments;

import android.content.Context;
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
import com.bam.darkhouseextreme.app.helper.DatabaseHelper;
import com.bam.darkhouseextreme.app.model.Item;
import com.bam.darkhouseextreme.app.utilities.SaveUtility;
import com.bam.darkhouseextreme.app.utilities.Utilities;

/**
 * Created by Chobii on 29/04/15.
 */
public class RoomFragment extends Fragment {

    private final String LOG_DATA = RoomFragment.class.getSimpleName();

    private View root;
    private Button buttonUp, buttonDown, buttonLeft, buttonRight;
    private Context context;
    private ImageView ltest, itemButton;
    private int x_cord, y_cord, score;
    private String itemPickedUpTag;
    private DatabaseHelper helper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        root = inflater.inflate(R.layout.room, container, false);
        ltest = (ImageView)root.findViewById(R.id.ltest);
        helper = new DatabaseHelper(context);

        buttonUp = (Button)root.findViewById(R.id.buttonUp);
        buttonDown = (Button)root.findViewById(R.id.buttonDown);
        buttonLeft = (Button)root.findViewById(R.id.buttonLeft);
        buttonRight = (Button)root.findViewById(R.id.buttonRight);
        itemButton = (ImageView) root.findViewById(R.id.key1);

        int[] stats = SaveUtility.loadStats();
        x_cord = stats[0];
        y_cord = stats[1];
        score  = stats[2];

        continueIfApplicable(x_cord, y_cord);

        setButtonUp();
        setButtonDown();
        setButtonLeft();
        setButtonRight();
        setPickUpItem();

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
                        if (!isRoom(x_cord-=1, y_cord)) {
                            x_cord+=1;
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
                        if (!isRoom(x_cord+=1, y_cord)) {
                            x_cord-=1;
                            informOfError();
                        }
                    }
                }
        );
    }

    private void setPickUpItem() {
        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                itemButton.setClickable(false);
                itemPickedUpTag = itemButton.getTag().toString();
                Log.d(LOG_DATA, "In room frag " + itemPickedUpTag);
                Item item = helper.getOneItem(itemPickedUpTag);
                SaveUtility.saveItemToCharacter(item);
                Animation fadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                itemButton.setAnimation(fadein);

                fadein.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation fadeout = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                        itemButton.startAnimation(fadeout);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    private void changeRoom(final int roomId) {

        Log.d(LOG_DATA, String.valueOf(x_cord));
        Log.d(LOG_DATA, String.valueOf(y_cord));

        Animation fadeout = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        ltest.startAnimation(fadeout);

        fadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ltest.setImageResource(roomId);
                Animation fadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                ltest.startAnimation(fadein);
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
        ltest.setImageResource(roomId);
    }

//    private void isRoomsNext(int x, int y) {
//        String left = String.valueOf(x-1) + String.valueOf(y);
//        String right = String.valueOf(x+1) + String.valueOf(y);
//        String up = String.valueOf(x) + String.valueOf(y+1);
//        String down = String.valueOf(x) + String.valueOf(y-1);
//
//        int roomId;
//
//        if ((roomId = Utilities.isViableRoom(left, context)) == 0) {
//            buttonLeft.setEnabled(false);
//        } else {
//            buttonLeft.setEnabled(true);
//        }
//
//        if ((roomId = Utilities.isViableRoom(right, context)) == 0) {
//            buttonRight.setEnabled(false);
//        } else {
//            buttonRight.setEnabled(true);
//        }
//
//        if ((roomId = Utilities.isViableRoom(up, context)) == 0) {
//            buttonUp.setEnabled(false);
//        } else {
//            buttonUp.setEnabled(true);
//        }
//
//        if ((roomId = Utilities.isViableRoom(down, context)) == 0) {
//            buttonDown.setEnabled(false);
//        } else {
//            buttonDown.setEnabled(true);
//        }
//    }
}
