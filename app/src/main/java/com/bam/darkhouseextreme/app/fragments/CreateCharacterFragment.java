package com.bam.darkhouseextreme.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.bam.darkhouseextreme.app.R;

/**
 * Created by Chobii on 28/04/15.
 */
public class CreateCharacterFragment extends Fragment {

    public Button ok;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.createcharacterfragment, container, false);
        ok = (Button)root.findViewById(R.id.okGo);
        return root;
    }

    public void setGo() {
        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                    }
                }
        );
    }
}
