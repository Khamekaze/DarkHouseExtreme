package com.bam.darkhouseextreme.app;

import android.test.ActivityInstrumentationTestCase2;

import com.bam.darkhouseextreme.app.activities.StartScreenActivity;
import com.bam.darkhouseextreme.app.adapter.CharacterListAdapter;
import com.bam.darkhouseextreme.app.model.Player;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin on 4/30/2015.
 */
public class SelectCharacterTest extends ActivityInstrumentationTestCase2<StartScreenActivity> {


    public SelectCharacterTest(Class<StartScreenActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        List<Player> players = new ArrayList<>();
        players.add(new Player(0, "Benji"));
        CharacterListAdapter cla = new CharacterListAdapter(getActivity().getApplicationContext(), R.layout.characterselectionrow, players);
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testList() {

    }

}
