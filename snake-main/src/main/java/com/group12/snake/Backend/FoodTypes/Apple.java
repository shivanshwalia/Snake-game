package com.group12.snake.Backend.FoodTypes;

import com.group12.snake.Backend.Food;
import javafx.scene.paint.Color;

public class Apple extends Food {

    public Apple(int xPos, int yPos){
        super(xPos, yPos, 1);
        super.setColor(Color.RED);
        this.setImage("assets/apple.png");
    }
}
