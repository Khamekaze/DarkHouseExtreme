package com.bam.darkhouseextreme.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bam.darkhouseextreme.app.R;
import com.bam.darkhouseextreme.app.activities.GameActivity;
import com.bam.darkhouseextreme.app.adapter.CharacterListAdapter;
import com.bam.darkhouseextreme.app.helper.DatabaseHelper;
import com.bam.darkhouseextreme.app.model.Player;
import com.bam.darkhouseextreme.app.utilities.Utilities;

import java.util.ArrayList;

/**
 * Created by Chobii on 28/04/15.
 */
public class SelectCharacterFragment extends Fragment {

    private DatabaseHelper helper;
    private Context context;
    private Button deleteBtn;
    private Button selectCharacterBtn;
    private Cursor cursor;
    private ArrayList<Player> players = new ArrayList<>();
    private CharacterListAdapter characterListAdapter;
    private ListView characterListView;
    private TextView characterNameView;
    private Player player;

    public final String LOG_DATA = StartScreenFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        final View root = inflater.inflate(R.layout.selectcharacterfragment, container, false);
        final Typeface fonts = Typeface.createFromAsset(context.getAssets(), "fonts/MISFITS_.TTF");

        helper = new DatabaseHelper(context);
        cursor = helper.getAllCharacters();

        while (cursor.moveToNext()) {
            Player player = new Player();
            player.setId(cursor.getLong(0));
            player.setName(cursor.getString(1));
            player.setMapXCoordinate(cursor.getInt(2));
            player.setMapYCoordinate(cursor.getInt(3));
            players.add(cursor.getPosition(), player);
        }

        characterListView = (ListView) root.findViewById(R.id.characterList);
        deleteBtn = (Button) root.findViewById(R.id.deleteCharacterButton);
        selectCharacterBtn = (Button) root.findViewById(R.id.loadGameButton);

        characterListAdapter = new CharacterListAdapter(context, R.layout.characterselectionrow, players);

        if (characterListView != null) {
            characterListView.setAdapter(characterListAdapter);
        }

        Utilities.setFontForView(root, fonts);
        selectCharacter();
        deleteCharacter();

        return root;
    }

    public void selectCharacter() {
        characterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                characterNameView = (TextView) view.findViewById(R.id.characterNameText);
                long playerId = (long) characterNameView.getTag();
                for (Player p : players) {
                    if (p.getId() == playerId) {
                        player = p;
                    }
                }
                Log.d(LOG_DATA, player.getName());
                chooseSelectedCharacter();
            }
        });
    }

    public void deleteCharacter() {
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null) {
                    helper.deleteCharacter(player.getName());
                } else {
                    Toast.makeText(context, "No character selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public void selectCharacter() {
//        characterListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                characterNameView = (TextView) view.findViewById(R.id.characterNameText);
//                long playerId = (long) characterNameView.getTag();
//                for (Player p : players) {
//                    if (p.getId() == playerId) {
//                        player = p;
//                    }
//                }
//                Log.d(LOG_DATA, player.getName());
//                chooseSelectedCharacter();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                selectCharacterBtn.setClickable(false);
//
//            }
//        });
//    }

    public void chooseSelectedCharacter() {
        selectCharacterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null) {
                    Intent intent = new Intent(context, GameActivity.class);
                    intent.putExtra("player", player);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "No character selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
