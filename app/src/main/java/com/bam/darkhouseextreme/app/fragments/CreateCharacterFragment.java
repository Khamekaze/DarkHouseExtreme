package com.bam.darkhouseextreme.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.bam.darkhouseextreme.app.R;
import com.bam.darkhouseextreme.app.activities.GameActivity;
import com.bam.darkhouseextreme.app.helper.DatabaseHelper;
import com.bam.darkhouseextreme.app.model.Player;
import com.bam.darkhouseextreme.app.utilities.Utilities;

/**
 * Created by Chobii on 28/04/15.
 */
public class CreateCharacterFragment extends Fragment {

    public Button ok;
    private EditText editText;
    private TextView txtV;
    private Context context;
    private DatabaseHelper helper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();

        final Typeface fonts = Typeface.createFromAsset(context.getAssets(), "fonts/MISFITS_.TTF");

        final View root = inflater.inflate(R.layout.createcharacterfragment, container, false);
        ok = (Button)root.findViewById(R.id.okGo);
        editText = (EditText)root.findViewById(R.id.createEdit);
        txtV = (TextView)root.findViewById(R.id.createText);

        Utilities.setFontForView(root, fonts);
        setGo();
        return root;
    }

    public void setGo() {
        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helper = new DatabaseHelper(context);
                        Player player = helper.createCharacter(editText.getText().toString());
                        Intent intent = new Intent(context, GameActivity.class);
                        intent.putExtra("player", player);
                        startActivity(intent);
                    }
                }
        );
    }





}
