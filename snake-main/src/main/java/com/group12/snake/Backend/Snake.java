package com.group12.snake.Backend;

import com.group12.snake.Backend.FoodTypes.SnakePart;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Snake {
    private GridPos headPos;
    private ArrayList<SnakePart> bodyPos;
    private int direction;// 0 = north, 1 = west, 2 = south, 3 = east

    public Snake() {
        this.headPos = new GridPos(Grid.POS_LENGTH, Grid.POS_LENGTH * 2, Color.DEEPSKYBLUE);
        this.bodyPos = new ArrayList<>();
        this.grow(3);
        this.direction = 3;
    }

    public int updatePos(double WIDTH, double HEIGHT, GridPos foodPos) { //First updates the bodies position and then moves the head

        for (int i = 0; i < bodyPos.size(); i++) {

            if (i < (bodyPos.size() - 1)) {
                this.bodyPos.set(i, this.bodyPos.get(i + 1));
            } else {
                this.bodyPos.set(i, new SnakePart(this.headPos.getxPos(), this.headPos.getyPos(), Color.ORANGE));
            }
        }
        switch (this.direction) {
            case 0:
                headPos.setyPos(headPos.getyPos()-Grid.POS_LENGTH);
                break;

            case 1:
                headPos.setxPos(headPos.getxPos()-Grid.POS_LENGTH);
                break;

            case 2:
                headPos.setyPos(headPos.getyPos()+Grid.POS_LENGTH);
                break;

            case 3:
                headPos.setxPos(headPos.getxPos()+Grid.POS_LENGTH);
                break;

            default:
                //Error
                break;
        }

        return checkCollision(WIDTH, HEIGHT, foodPos);
    }
    public GridPos getHeadPos(){
        return this.headPos;
    }
    public int checkCollision(double WIDTH, double HEIGHT, GridPos foodPos){ //Call this after updatePos method to see if snake has collided
        for(GridPos currentPos : bodyPos){
            if(headPos.equals(currentPos)){
                return 2;
            }
        }

        if(headPos.getxPos() >= WIDTH  || headPos.getyPos()>= HEIGHT || headPos.getxPos() < 0 ||headPos.getyPos() < 0){return 2;}

        if(this.headPos.equals(foodPos)){
            Food food = (Food) foodPos;
            this.grow(food.getScore());
            return 1;
        }

        return 0;
    }
    public void setDirection(int newDirection){
        this.direction = newDirection;
    }
    public int getDirection(){
        return this.direction;
    }

    public void grow(){
        this.bodyPos.add(0, new SnakePart(-50,250,Color.ORANGE));
    }
    public void grow(int amount){
        for(int i = 0; i < amount; i++){
            this.grow();
        }
    }
    public ArrayList<SnakePart> getBodyPos(){
        return this.bodyPos;
    }

}
