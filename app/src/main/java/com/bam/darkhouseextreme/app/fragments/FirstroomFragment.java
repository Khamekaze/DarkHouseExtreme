package com.bam.darkhouseextreme.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import com.bam.darkhouseextreme.app.R;
import com.bam.darkhouseextreme.app.utilities.Utilities;

/**
 * Created by Chobii on 29/04/15.
 */
public class FirstroomFragment extends Fragment {

    private View root;
    private Button buttonUp, buttonDown, buttonLeft, buttonRight;
    private Context context;
    private ImageView ltest;
    private int x_cord, y_cord;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        root = inflater.inflate(R.layout.firstroom, container, false);
        ltest = (ImageView)root.findViewById(R.id.ltest);

        buttonUp = (Button)root.findViewById(R.id.buttonUp);
        buttonDown = (Button)root.findViewById(R.id.buttonDown);
        buttonLeft = (Button)root.findViewById(R.id.buttonLeft);
        buttonRight = (Button)root.findViewById(R.id.buttonRight);

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
                        x_cord += 1;
                        changeRoom(x_cord, y_cord);
                        isRoomsNext(x_cord, y_cord);
                    }
                }
        );
    }

    private void setButtonDown() {
        buttonDown.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        x_cord -= 1;
                        changeRoom(x_cord, y_cord);
                        isRoomsNext(x_cord, y_cord);
                    }
                }
        );
    }

    private void setButtonLeft() {
        buttonLeft.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        y_cord -= 1;
                        changeRoom(x_cord, y_cord);
                        isRoomsNext(x_cord, y_cord);
                    }
                }
        );
    }

    private void setButtonRight() {
        buttonRight.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        y_cord += 1;
                        changeRoom(x_cord, y_cord);
                        isRoomsNext(x_cord, y_cord);
                    }
                }
        );
    }

    private void changeRoom(int x, int y) {
        String room = String.valueOf(x) + String.valueOf(y);
        final int roomId;
        if ((roomId = Utilities.isViableRoom(room, context)) != 0) {

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
    }

    private void isRoomsNext(int x, int y) {
        String left = String.valueOf(x-1) + String.valueOf(y);
        String right = String.valueOf(x+1) + String.valueOf(y);
        String up = String.valueOf(x) + String.valueOf(y+1);
        String down = String.valueOf(x) + String.valueOf(y-1);

        int roomId;

        if ((roomId = Utilities.isViableRoom(left, context)) == 0) {
            buttonLeft.setEnabled(false);
        } else {
            buttonLeft.setEnabled(true);
        }

        if ((roomId = Utilities.isViableRoom(right, context)) == 0) {
            buttonRight.setEnabled(false);
        } else {
            buttonRight.setEnabled(true);
        }

        if ((roomId = Utilities.isViableRoom(up, context)) == 0) {
            buttonUp.setEnabled(false);
        } else {
            buttonUp.setEnabled(true);
        }

        if ((roomId = Utilities.isViableRoom(down, context)) == 0) {
            buttonDown.setEnabled(false);
        } else {
            buttonDown.setEnabled(true);
        }
    }
}
