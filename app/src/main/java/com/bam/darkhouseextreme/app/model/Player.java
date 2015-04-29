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
    private Integer mapXCoordinate;
    private Integer mapYCoordinate;
    private List<Integer> objectIds = new ArrayList<>();
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

    public List<Integer> getObjectIds() {
        return objectIds;
    }

    public void setObjectIds(List<Integer> objectIds) {
        this.objectIds = objectIds;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
