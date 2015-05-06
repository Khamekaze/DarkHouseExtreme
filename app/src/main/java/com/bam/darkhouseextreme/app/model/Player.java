package com.bam.darkhouseextreme.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 2015-04-28.
 */
public class Player implements Serializable {
    private long id;
    private String name;
    private int mapXCoordinate;
    private int mapYCoordinate;
    private List<Item> playerItems = new ArrayList<>();
    private int score;

    public Player() {

    }

    public Player(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMapXCoordinate() {
        return mapXCoordinate;
    }

    public void setMapXCoordinate(int mapXCoordinate) {
        this.mapXCoordinate = mapXCoordinate;
    }

    public int getMapYCoordinate() {
        return mapYCoordinate;
    }

    public void setMapYCoordinate(int mapYCoordinate) {
        this.mapYCoordinate = mapYCoordinate;
    }

    public List<Item> getPlayerItems() {
        return playerItems;
    }

    public void setPlayerItems(List<Item> playerItems) {
        this.playerItems = playerItems;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (id != player.id) return false;
        if (mapXCoordinate != player.mapXCoordinate) return false;
        if (mapYCoordinate != player.mapYCoordinate) return false;
        if (score != player.score) return false;
        if (name != null ? !name.equals(player.name) : player.name != null) return false;
        return !(playerItems != null ? !playerItems.equals(player.playerItems) : player.playerItems != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + mapXCoordinate;
        result = 31 * result + mapYCoordinate;
        result = 31 * result + (playerItems != null ? playerItems.hashCode() : 0);
        result = 31 * result + score;
        return result;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mapXCoordinate=" + mapXCoordinate +
                ", mapYCoordinate=" + mapYCoordinate +
                ", playerItems=" + playerItems +
                ", score=" + score +
                '}';
    }
}
