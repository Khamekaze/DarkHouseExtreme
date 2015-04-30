package com.bam.darkhouseextreme.app.utilities;

import android.content.Context;
import com.bam.darkhouseextreme.app.helper.DatabaseHelper;
import com.bam.darkhouseextreme.app.model.Item;
import com.bam.darkhouseextreme.app.model.Player;

import java.util.List;

/**
 * Created by Chobii on 30/04/15.
 */
public class SaveUtility {

    private static Player player;
    private static DatabaseHelper helper;

    public static void setHelper(Context context) {
        helper = new DatabaseHelper(context);
    }

    public static void saveProgress(int x, int y, int score) {
        boolean success = helper.updateCharacter(String.valueOf(player.getId()), String.valueOf(x), String.valueOf(y), score);
        player.setMapXCoordinate(x);
        player.setMapYCoordinate(y);
    }

    public static List<Player> getAllCharacters() {
        return helper.getAllCharacters();
    }

    public static void saveItemToCharacter(Item item) {
        helper.addObjectToPlayerInventory(String.valueOf(player.getId()), String.valueOf(item.getId()));
        player.getPlayerItems().add(item);
    }

    public static void deleteCharacter(Player player) {
        helper.deleteCharacter(String.valueOf(player.getId()));
    }

    public static void loadCharacter(Player player) {
        SaveUtility.player = player;
    }

    public static void createCharacter(String name) {
        player = helper.createCharacter(name);
        player.setMapXCoordinate(0);
        player.setMapYCoordinate(1);
        player.setScore(0);
    }

    public static int[] loadStats() {
        return new int[] {player.getMapXCoordinate(), player.getMapYCoordinate(), player.getScore()};
    }
}
