package com.group12.snake.Backend.FoodTypes;

import com.group12.snake.Backend.GridPos;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SnakePart extends GridPos {
    private static final Image image = new Image("assets/Red_circle.svg.png");;
    public SnakePart(int xPos, int yPos, Color color) {
        super(xPos, yPos, color);
        }

    public Image getImage() {
        return image;
    }
}

