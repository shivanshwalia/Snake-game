package com.group12.snake.Backend.FoodTypes;

import com.group12.snake.Backend.Food;
import javafx.scene.paint.Color;

public class HotDog extends Food {

    public HotDog(int xPos, int yPos){
        super(xPos,yPos,2);
        super.setColor(Color.BLUE);
        this.setImage("assets/hotdog.png");

    }
}
