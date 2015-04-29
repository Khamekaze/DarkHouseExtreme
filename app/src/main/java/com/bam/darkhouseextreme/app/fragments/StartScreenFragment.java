package com.bam.darkhouseextreme.app.fragments;

import android.content.Context;
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
import com.bam.darkhouseextreme.app.utilities.Utilities;

/**
 * Created by Chobii on 28/04/15.
 */
public class StartScreenFragment extends Fragment {

    public final String LOG_DATA = StartScreenFragment.class.getSimpleName();

    public Button newGame, quit, loadGame;
    public FragmentManager manager;
    public ViewGroup container;

    private Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();

        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/MISFITS_.TTF");

        this.container = container;

        manager = getActivity().getSupportFragmentManager();

        final View root = inflater.inflate(R.layout.startscreenfragment, container, false);
        newGame = (Button) root.findViewById(R.id.newGame);
        loadGame = (Button) root.findViewById(R.id.loadGameButton);
        quit = (Button) root.findViewById(R.id.quit);
        newGame.setTypeface(font);
        newGame();
        loadGame();

        Utilities.setFontForView(root, font);

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

    public void loadGame() {
        loadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit);

                SelectCharacterFragment selectCharacterFragment = new SelectCharacterFragment();
                transaction.replace(R.id.startscreenlayout, selectCharacterFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
