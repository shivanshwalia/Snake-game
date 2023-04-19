package com.group12.snake.Backend;

import com.group12.snake.Backend.FoodTypes.SnakePart;
import com.group12.snake.Backend.Utils.SongUtils;
import com.group12.snake.Frontend.Controller;
import com.group12.snake.Frontend.Utils.UISongUtils;
import com.group12.snake.Frontend.Controller;
import com.group12.snake.Frontend.Utils.UIUtils;
import com.group12.snake.Frontend.Draw;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Game extends AnimationTimer {

    private Grid gameGrid;
    private Canvas canvas;
    public long lastUpdate;
    public boolean canTurn;
    private ArrayList<ScoreData> scoreList;
    public static boolean isFrenzy;

    public Game(Canvas grid, boolean frenzy){

        this.gameGrid = new Grid();
        this.canvas = grid;
        this.lastUpdate = 0;
        this.canTurn = true;
        this.scoreList = Game.readScores();
        this.isFrenzy = frenzy;
        gameGrid.spawnFood(isFrenzy);

        UIUtils.updateText("#songNameText", SongList.songs.get(SongList.songIndex), true);

        // Note for JoelM: The two lines below are repetitive - Controller.java: 'pauseSong()'
        CheckBox pauseCheckBox = (CheckBox)(Controller.scene.lookup("#pauseCheckBox"));
        pauseCheckBox.setSelected(!SongUtils.currentClip.isRunning());

        selectSongUI();
        UISongUtils.updateSongListTexts();
        SongList.updateSelectedSongText(SongList.songIndex, false);
        SongList.synchronizeThumbnailWithSong();
    }

        private void selectSongUI() // This method doesn't belong to this class, based on its behaviour - JoelM: Fix this
    {
        String selectedSongList = SongList.getCurrentList();
        RadioButton Button = (RadioButton)(Controller.scene.lookup("#" + selectedSongList));
        Button.setSelected(true);

        // Note for JoelM: Either come up with a better general design for all song lists, or make general method for the things below
        /*if (Objects.equals(selectedSongList, SongList.listNames[0]))
        {
            RadioButton chillButton = (RadioButton)(Controller.scene.lookup("#chill"));
            chillButton.setSelected(true);
        }
        else if (Objects.equals(selectedSongList, SongList.listNames[1]))
        {
            RadioButton trapButton = (RadioButton)(Controller.scene.lookup("#trap"));
            trapButton.setSelected(true);
        }
        else if (Objects.equals(selectedSongList, SongList.listNames[2]))
        {
            RadioButton hipHopButton = (RadioButton)(Controller.scene.lookup("#hipHop"));
            hipHopButton.setSelected(true);
        }
        else if (Objects.equals(selectedSongList, SongList.listNames[3]))
        {
            RadioButton hipHopButton = (RadioButton)(Controller.scene.lookup("#disco"));
            hipHopButton.setSelected(true);
        }*/
    }

    /*readScores will load the score.json file and read its values adding them to the leaderboard array*/
    public static ArrayList<ScoreData> readScores() {

        ArrayList<ScoreData> leaderboard = new ArrayList<>();

        try {

            byte[] bytes = Files.readAllBytes(Paths.get("Scores/score.json"));
            String fileContent = new String (bytes);

            JSONObject obj = new JSONObject(fileContent);
            JSONArray scores = obj.getJSONArray("scores");
            for(Object score : scores) {

                JSONObject scoreObject = (JSONObject) score;

                int value = (Integer) scoreObject.get("value");
                String timestamp = (String) scoreObject.get("timestamp");
                leaderboard.add(new ScoreData(value, timestamp));

            }


        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        Collections.sort(leaderboard);
        return leaderboard;

    }

    /*The handle method is a javafx method that is overridden and will be called on every frame update by javafx
    * this allows us to have the game loop aspect which we need */
    @Override
    public void handle(long time) {

        if((time-lastUpdate) >= Math.pow(10,9)/5)
        {
            // Only update song-bar progression when user is in the game scene
            if (!Controller.userIsInStartMenu)
            {
                SongUtils.updateSongBarProgression();
            }

            int code = gameGrid.moveSnake(isFrenzy);

            switch (code) {

                case 1:
                    this.updateScore();
                    this.paintScene(time);
                    break;
                case 2:
                    this.stop();
                    this.writeScores();
                    this.showGameOver();
                    break;

                default:
                    this.paintScene(time);
                    break;

            }


        }



    }

    /*paintScene will repaint/draw the scene on every update*/
    private void paintScene(long time) {

        Draw playground = new Draw(canvas.getGraphicsContext2D());
        playground.drawBackground(getPos());
        playground.draw(getHeadPos());
        for(SnakePart bodyPos : gameGrid.getBodyPos()){
            playground.draw(bodyPos);
        }
        playground.draw(gameGrid.getFood());
        this.lastUpdate = time;
        this.canTurn = true;

    }
    private void updateScore() {
        Label label = (Label) canvas.getScene().lookup("#score");
        String scores = String.valueOf(getScore(gameGrid.getBodyPos()));
        label.setText(scores);
    }
    private void showGameOver() {

        Stage window = (Stage)canvas.getScene().getWindow();
        try {

            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("GameOverScreen.fxml"));
            Scene scene = new Scene(root);
            window.setScene(scene);

            Label label = (Label) scene.lookup("#finalScore");
            label.setText(String.valueOf(getScore(gameGrid.getBodyPos())));

            VBox vbox = (VBox) scene.lookup("#leaderboard");
            ArrayList<ScoreData> leaderboard = Game.readScores();
            Accordion accordion = new Accordion();

            for (ScoreData score : leaderboard) {

                TitledPane titledPane = new TitledPane("Score: " + score.getValue(), new Label("Obtained at " + score.getDate()));
                accordion.getPanes().add(titledPane);

            }

            vbox.getChildren().add(accordion);

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /*writeScores will attempt to write the scores to the score.json file*/
    private void writeScores() {

        try {
            ScoreData currentScore = new ScoreData(this.getScore(gameGrid.getBodyPos()));
            this.scoreList.add(currentScore);

            JSONObject jo = new JSONObject();
            JSONArray scores = new JSONArray();

            for(ScoreData score : this.scoreList) {

                JSONObject data = new JSONObject();
                data.put("value", score.getValue());
                data.put("timestamp", score.getDate());
                scores.put(data);

            }
            jo.put("scores", scores);
            FileWriter writer = new FileWriter("Scores/score.json");
            writer.write(jo.toString());
            writer.flush();
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void setDirection(int newDirection){
        this.gameGrid.setDirection(newDirection);
    }
    public int getDirection(){
        return gameGrid.getDirection();
    }

    /**/
    public int getScore(ArrayList<SnakePart> positions) {
        return positions.size() - 3;
    }
    /*public boolean play(int direction) throws Exception{
        long frameRate = 1000/2; // one second in milisecond / framrate
        long time = System.currentTimeMillis();      
        Thread.sleep((frameRate-time%frameRate));

        return gameGrid.moveSnake(); //If snake hasn't collided


    }*/
    public ArrayList<GridPos> getPos(){
        return gameGrid.getPositions();
    }
    public GridPos getHeadPos(){
        return gameGrid.getHeadPos();
    }
}
