package com.group12.snake.Backend.FoodTypes;

import com.group12.snake.Backend.Food;
import javafx.scene.paint.Color;

public class Pizza extends Food {

    public Pizza(int xPos, int yPos){

        super(xPos, yPos, 3);
        super.setColor(Color.YELLOW);
        super.setImage("assets/pizza.png");

    }
}
