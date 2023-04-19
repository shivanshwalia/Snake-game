package com.group12.snake.Backend;

import javafx.scene.paint.Color;

public class GridPos {
    private int xPos;
    private int yPos;
    private Color color;

    public GridPos(int xPos, int yPos, Color color) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.color = color;
    }

    public int getxPos() {
        return this.xPos;
    }

    public int getyPos() {
        return this.yPos;
    }
    public Color getColor(){return this.color;}

    public void setColor(Color color){
        this.color = color;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }  
    
    public void setyPos(int yPos) {
        this.yPos = yPos;
    }  

    @Override
    public String toString() {
        return String.format("[X: %d, Y:%d]", this.xPos, this.yPos);
    }

    @Override
    public boolean equals(Object obj) { //Checks if yPos and xPos are equal and if they are returns true
        if(obj == null){ return false;} //Not sure about the implementation, should work but if you want to change it feel free
        if(obj == this){return true;}
        else if(obj instanceof GridPos){
            GridPos otherGridPos = (GridPos) obj;
            if(this.xPos == otherGridPos.getxPos() && this.yPos == otherGridPos.getyPos()){return true;}
        }
        return false;
    }

}
