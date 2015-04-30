package com.bam.darkhouseextreme.app;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.bam.darkhouseextreme.app.helper.DatabaseHelper;
import com.bam.darkhouseextreme.app.model.Player;

import java.util.List;

/**
 * Created by Chobii on 30/04/15.
 */


public class TestOfDatabase extends AndroidTestCase {

    private Player player;
    private DatabaseHelper helper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "_test");
        helper = new DatabaseHelper(context);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCRUD() {
        // Test Add.
        String name = "Akana";
        player = helper.createCharacter(name);
        assertEquals(name, player.getName());
        // Test Update.
        boolean updated = helper.updateCharacter(String.valueOf(player.getId()), "0", "1", 100);
        assertTrue(updated);
        // Test Read.
        List<Player> players = helper.getAllCharacters();
        assertTrue(players.size() != 0);
        // Test Delete.
        boolean deleted = helper.deleteCharacter(String.valueOf(player.getId()));
        assertTrue(deleted);

    }
}
