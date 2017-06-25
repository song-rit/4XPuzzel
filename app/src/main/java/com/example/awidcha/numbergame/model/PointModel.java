package com.example.awidcha.numbergame.model;

/**
 * Created by Awidcha on 23/6/2560.
 */

public class PointModel {

    private int id;
    private String fastestPoint;

    public PointModel() {
    }


    public PointModel(int id, String fastestPoint) {
        this.id = id;
        this.fastestPoint = fastestPoint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFastestPoint() {
        return fastestPoint;
    }

    public void setFastestPoint(String fastestPoint) {
        this.fastestPoint = fastestPoint;
    }
}
