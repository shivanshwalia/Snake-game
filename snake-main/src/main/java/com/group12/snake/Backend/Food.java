package com.group12.snake.Backend;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Food extends GridPos {

    private int score;
    private Image image;

    public Food(int x, int y, int score) {

        super(x, y, Color.RED);
        this.score = score;
        this.image = null;

    }

    public int getScore(){
        return this.score;
    }

    public void setImage(String url) {

        this.image = new Image(url);

    }

    public Image getImage() {
        return this.image;
    }



}
