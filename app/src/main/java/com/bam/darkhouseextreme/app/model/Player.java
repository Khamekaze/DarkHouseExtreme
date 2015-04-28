package com.bam.darkhouseextreme.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 2015-04-28.
 */
public class Player {
    private int id;
    private String name;
    private int mapXCoordinate;
    private int mapYCoordinate;
    private List<Integer> objectIds = new ArrayList<>();
    private int score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
