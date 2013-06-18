package com.prudencio.graphnodeview;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Node {
    private int id;
    private ArrayList<Node> adjacentNodes;
    private double dispX;
    private double dispY;
    private double posX;
    private double posY;
    private boolean isDragged;
    private String name;
    private String location;
    private Bitmap photo;

    public Node(int id) {
        super();
        this.id = id;
        adjacentNodes = new ArrayList<Node>();
        this.name = "John";
        this.location = "Porto";
    }

    public Node() {
        adjacentNodes = new ArrayList<Node>();
        this.name = "John";
        this.location = "Porto";
    }

    public Node(ArrayList<Node> adjacentNodes, int id) {
        super();
        this.adjacentNodes = adjacentNodes;
        this.id = id;
        this.name = "John";
        this.location = "Porto";
    }

    /**
     * @return the adjacentNodes
     */
    public ArrayList<Node> getAdjacentNodes() {
        return adjacentNodes;
    }

    /**
     * @param adjacentNodes the adjacentNodes to set
     */
    public void setAdjacentNodes(ArrayList<Node> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }
    /**
     * @return the edges
     */

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the isDragged
     */
    public boolean isDragged() {
        return isDragged;
    }

    /**
     * @param isDragged the isDragged to set
     */
    public void setDragged(boolean isDragged) {
        this.isDragged = isDragged;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public double getDispX() {
        return dispX;
    }

    public void setDispX(double dispX) {
        this.dispX = dispX;
    }

    public double getDispY() {
        return dispY;
    }

    public void setDispY(double dispY) {
        this.dispY = dispY;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

}
