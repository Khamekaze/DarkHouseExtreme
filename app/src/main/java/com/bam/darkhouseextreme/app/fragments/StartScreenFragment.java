package com.bam.darkhouseextreme.app.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.bam.darkhouseextreme.app.R;

/**
 * Created by Chobii on 28/04/15.
 */
public class StartScreenFragment extends Fragment {

    public final String LOG_DATA = StartScreenFragment.class.getSimpleName();

    public Button newGame, quit, selectCharacter;
    public FragmentManager manager;
    public ViewGroup container;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final Typeface font = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/MISFITS_.TTF");

        this.container = container;

        manager = getActivity().getSupportFragmentManager();

        final View root = inflater.inflate(R.layout.startscreenfragment, container, false);
        newGame = (Button)root.findViewById(R.id.newGame);
        selectCharacter = (Button)root.findViewById(R.id.selectCharacter);
        quit = (Button)root.findViewById(R.id.quit);
        newGame.setTypeface(font);
        newGame();

        return root;

    }


    public void newGame() {
        newGame.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setCustomAnimations(R.anim.enter, R.anim.exit);

                        CreateCharacterFragment charFragment = new CreateCharacterFragment();
                        transaction.replace(R.id.startscreenlayout, charFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
        );
    }
}
