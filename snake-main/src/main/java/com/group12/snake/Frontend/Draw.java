package com.group12.snake.Frontend;

import com.group12.snake.Backend.Food;
import com.group12.snake.Backend.FoodTypes.SnakePart;
import com.group12.snake.Backend.Grid;
import com.group12.snake.Backend.GridPos;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Draw {
    private GraphicsContext gc;

    public Draw (GraphicsContext gc){
        this.gc = gc;
    }

    public void drawBackground(ArrayList<GridPos> positions){
        for(GridPos position : positions){
            this.gc.setFill(position.getColor());
            this.gc.fillRect(position.getxPos(), position.getyPos(), Grid.POS_LENGTH, Grid.POS_LENGTH);
        }
    }
    public void draw(GridPos pos){
        this.gc.setFill(pos.getColor());
        this.gc.fillRect(pos.getxPos(), pos.getyPos(), Grid.POS_LENGTH, Grid.POS_LENGTH);
    }

    public void draw(Food food) {

//        this.draw((GridPos) food);
        this.gc.drawImage(food.getImage(), food.getxPos(), food.getyPos(), Grid.POS_LENGTH, Grid.POS_LENGTH);


    }
    public void draw(SnakePart skin) {

//        this.draw((GridPos) food);
        this.gc.drawImage(skin.getImage(), skin.getxPos(), skin.getyPos(), Grid.POS_LENGTH, Grid.POS_LENGTH);
    }
}
