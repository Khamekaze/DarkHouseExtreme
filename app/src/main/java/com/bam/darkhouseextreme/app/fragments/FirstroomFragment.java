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

/**
 * Created by Chobii on 29/04/15.
 */
public class FirstroomFragment extends Fragment {

    private View root;
    private Button buttonGo;
    private Context context;
    private ImageView ltest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        root = inflater.inflate(R.layout.firstroom, container, false);
        ltest = (ImageView)root.findViewById(R.id.ltest);

        buttonGo = (Button)root.findViewById(R.id.buttongo);
        setButtonGo();

        return root;
    }


    private void setButtonGo() {
        buttonGo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation fadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                        ltest.startAnimation(fadein);

                        fadein.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                ltest.setImageResource(R.drawable.room2);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                ltest.setImageResource(R.drawable.room2);
//                                Animation fadeout = AnimationUtils.loadAnimation(context, R.anim.fade_out);
//                                ltest.startAnimation(fadeout);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }
        );
    }
}
