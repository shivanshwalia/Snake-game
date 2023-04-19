package com.group12.snake.Backend;

import com.group12.snake.Backend.FoodTypes.Apple;
import com.group12.snake.Backend.FoodTypes.Pizza;
import com.group12.snake.Backend.FoodTypes.HotDog;

import com.group12.snake.Backend.FoodTypes.SnakePart;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class Grid {
    private final double WIDTH;
    private final double HEIGHT;
    public static final int POS_LENGTH = 50;
    private final static ArrayList<GridPos> positions = new ArrayList<>();;
    private Snake snake;
    public Food food;

    public Grid() {

        this.WIDTH = 600;
        this.HEIGHT = 600;
        this.drawGrid();
        this.snake = new Snake();

    }


    /* The drawGrid method draws the game grid by iterating through the columns and rows and adding them to the positions array */
    private void drawGrid() {

        double horizontalPositions = Math.floor(this.WIDTH / POS_LENGTH);
        double verticalPositions = Math.floor(this.HEIGHT / POS_LENGTH);

        for (int x = 0; x < horizontalPositions; x++) {

            for (int y = 0; y < verticalPositions; y++) {

                Color color = (x+y) % 2 == 0 ? Color.web("17E16E") : Color.web("23BB64");
                GridPos addedPosition = new GridPos(x * POS_LENGTH, y * POS_LENGTH, color);
                Grid.positions.add(addedPosition);

            }

        }

    }
    public ArrayList<GridPos> getPositions(){
        return Grid.positions;
    }

    /*The spawnFood method handles spawning food in the game by randomly spawning in an unused game tile*/
    public Food spawnFood(boolean foodFrenzy) {

        ArrayList<GridPos> unusedPositions = new ArrayList<>(Grid.positions);
        unusedPositions.remove(this.snake.getHeadPos());
        unusedPositions.removeAll(this.snake.getBodyPos());
        int randomIndex = (int) (Math.random() * unusedPositions.size());

        Random random = new Random();
        int randomNum = random.nextInt(100);

        GridPos position = unusedPositions.get(randomIndex); // save this to get the x and y
        Food returnedFood = new Apple(position.getxPos(), position.getyPos());

        if(foodFrenzy) {

            //Uses variable randomNum to give a percentage of different food to spawn
            if(randomNum >= 70 && randomNum < 90){
                returnedFood = new Pizza(position.getxPos(), position.getyPos());
            }else if(randomNum >= 90){
                returnedFood = new HotDog(position.getxPos(), position.getyPos());
            }

        }
        this.food = returnedFood;
        return returnedFood;
    }
    public GridPos getHeadPos(){
        return snake.getHeadPos();
    }

    public int moveSnake(boolean frenzy){
        int code = snake.updatePos(this.WIDTH, this.HEIGHT, this.food);
        if(code == 1) {

            this.food = this.spawnFood(frenzy);

        }
        return code;
    }

    @Override
    public String toString() {
        return Grid.positions.toString();
    }
    public void setDirection(int newDirection){
        snake.setDirection(newDirection);
    }
    public int getDirection(){
        return snake.getDirection();
    }
    public Food getFood() { return this.food; }
    public ArrayList<SnakePart> getBodyPos(){
        return snake.getBodyPos();
    }

}
