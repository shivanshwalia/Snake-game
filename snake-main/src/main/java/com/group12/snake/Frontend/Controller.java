package com.group12.snake.Frontend;

import com.group12.snake.Backend.*;
import com.group12.snake.Backend.Utils.SongUtils;
import com.group12.snake.Backend.Utils.Utils;
import com.group12.snake.Frontend.Utils.UISongUtils;
import com.group12.snake.Frontend.Utils.UIUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Controller{

    public static Scene scene;
    public static boolean userIsInStartMenu = true;

    private Stage stage;
    private Parent root;
    @FXML
    private RadioButton ChillList;
    @FXML
    private RadioButton TrapList;
    @FXML
    private RadioButton HipHopList;
     @FXML
     private RadioButton DiscoList;

     private RadioButton[] listButtons;

    @FXML
    private CheckBox pauseCheckBox;
    private RadioButton mostRecentSelectedRadioButton = null;

    private int selectedListIndex;

    public void StartMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("StartMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        userIsInStartMenu = true;
    }

    public void exit() {
        SongUtils.stop();
        Platform.exit();
    }

    public void FoodFrenzyButton(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getClassLoader().getResource("GameScene.fxml"));
        setScene(event);

        Canvas grid = (Canvas) scene.lookup("#Grid");
        Game game = new Game(grid, true);
        setOnKeyPressed(game, scene);

        checkIfToggleSong(scene);
        resumeButton(game);
        game.start();

        userIsInStartMenu = false;
    }

    public void GameOverScreen(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("GameOverScreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void NormalMode(ActionEvent event) throws Exception {

        root = FXMLLoader.load(getClass().getClassLoader().getResource("GameScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Canvas grid = (Canvas) scene.lookup("#Grid");

        Game game = new Game(grid, false);
        setOnKeyPressed(game, scene);

        checkIfToggleSong(scene);
        resumeButton(game);
        game.start();

        userIsInStartMenu = false;
    }

    public void playAgain(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getClassLoader().getResource("GameScene.fxml"));
        setScene(event);
        Canvas grid = (Canvas) scene.lookup("#Grid");

        Game game = new Game(grid, Game.isFrenzy);
        setOnKeyPressed(game, scene);

        checkIfToggleSong(scene);
        resumeButton(game);
        game.start();
    }

    // Checks if user wants to toggle song in current list and performs the desired action when requested
    public void checkIfToggleSong(Scene scene)
    {
        scene.setOnKeyReleased(e ->
        {
            // User presses either 'E' or 'Q'
            if (e.getCode().equals(KeyCode.E) || e.getCode().equals(KeyCode.Q))
            {
                int previousSongIndex = SongList.songIndex;
                CheckBox pauseBox = (CheckBox)scene.lookup("#pauseCheckBox"); // Avoid NullPointerException when referring to 'pauseCheckBox'

                boolean increaseIndexOfSong = e.getCode().equals(KeyCode.E);
                SongUtils.toggleSong(increaseIndexOfSong, pauseBox.isSelected());

                SongList.synchronizeThumbnailWithSong();
                previousSongIndex = Utils.limitValue(previousSongIndex, SongList.songListIndicesBoundaries[SongList.listIndex]);
                SongList.updateSelectedSongText(previousSongIndex, false);
            }
        });
    }

    // Pause-panel is inactivated and game continues as the user clicks 'Resume'
    public void resumeButton(Game game)
    {
        Group pauseGroup = (Group) scene.lookup("#pause");
        Button button = (Button)scene.lookup("#resume");

        // User clicks 'resume game' and the panel disappears as the game starts again
        button.setOnAction(e ->
        {
            pauseGroup.setOpacity(0);
            pauseGroup.setDisable(true);

            game.start();
        });
    }

    // When the user pauses the game the pause-panel is activated
    public void pauseGame(Game game, Scene scene)
    {
        Group pauseGroup = (Group) scene.lookup("#pause");
        game.stop();

        pauseGroup.setDisable(false);
        pauseGroup.setOpacity(1);
    }


    // The 3 methods contained in this generalized method can be used in all the methods above except 'exit'
    // The reason this isn't refactored before pushing remotely is because I don't want to take credit
    // for the code itself, but for the refactoring design-aspect of it.
    public void generalizedMethod(ActionEvent event) throws IOException {
        root = getRoot("directory here");
        setScene(event);

        boolean bool = false; // This value varies depending on what method
        // we are inspecting of the ones above ('Leaderboard', 'playAgain', 'normalMode', 'foodFrenzyButton')
        startGame(bool);
    }

    // Gets the root with respect to its directory
    public Parent getRoot(String directory) throws IOException {
        return FXMLLoader.load(getClass().getClassLoader().getResource(directory));
    }

    // Sets a reference root to the scene and displays the stage
    public void setScene(ActionEvent event)
    {
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    // Instantiates grid and activates key-listener when game starts
    public void startGame(boolean frenzyModeIsSelected)
    {
        Canvas grid = (Canvas) scene.lookup("#Grid");
        Game game = new Game(grid, frenzyModeIsSelected);

        setOnKeyPressed(game, scene);
        game.start();
    }

    // This methods works as a listener that identifies if keys are pressed
    public void setOnKeyPressed(Game game, Scene scene)
    {
        scene.setOnKeyPressed(e ->
        {
            // Pause game if user clicks 'Esc'
            if (e.getCode().equals(KeyCode.ESCAPE))
            {
                pauseGame(game, scene);
            }

            changeDirection(scene, game, e);
        });
    }

    public void changeDirection(Scene scene, Game game, KeyEvent keyEvent)
    {
        if(!game.canTurn) return;

        switch (keyEvent.getCode()) {
            case W:
                if (game.getDirection() == 1 || game.getDirection() == 3) {
                    game.setDirection(0);
                }
                break;
            case S:
                if (game.getDirection() == 1 || game.getDirection() == 3) {
                    game.setDirection(2);
                }
                break;
            case D:
                if (game.getDirection() == 0 || game.getDirection() == 2) {
                    game.setDirection(3);
                }
                break;
            case A:
                if (game.getDirection() == 0 || game.getDirection() == 2) {
                    game.setDirection(1);
                }
                break;
            default:
                break;
        }

        game.canTurn = false;
    }

    public void changeSongList(ActionEvent event)
    {
        selectedListIndex = 0;
        int previousIndex = SongList.songIndex;
        listButtons = new RadioButton[]{ChillList, TrapList, HipHopList, DiscoList};

        // Only select or unselect list if a new list was picked
        if (!checkIfAnyListIsSelectedTwice(listButtons))
        {
            selectNewList();

            SongList.toggleSongList(selectedListIndex, pauseCheckBox.isSelected());
            SongList.synchronizeThumbnailWithSong();

            UISongUtils.updateSongListTexts();
            SongList.updateSelectedSongText(previousIndex, true);
        }
    }

    // Select the new list to play based on what the user selected
    private void selectNewList()
    {
        for (int i = 0; i < listButtons.length; i++)
        {
            if (listButtons[i].isSelected())
            {
                selectedListIndex = i;
                deselectRadioButton(mostRecentSelectedRadioButton, listButtons[i]);
                mostRecentSelectedRadioButton = listButtons[i];
            }
        }
    }

    // Checks if user clicked on ANY radio button that already is selected
    private boolean checkIfAnyListIsSelectedTwice(RadioButton[] selectedLists)
    {
        // Go through every possible list-index
        for (int i = 0; i < SongList.numberOfSongsInList.length; i++)
        {
            if (checkIfCurrentListWasSelectedTwice(selectedLists[i], i))
            {
                selectedLists[i].setSelected(true);
                return true;
            }
        }

        return false;
    }

    // Checks if the specific radio button was selected twice in a row
    private boolean checkIfCurrentListWasSelectedTwice(RadioButton selectedList, int listIndex)
    {
        return !selectedList.isSelected() && SongList.listIndex == listIndex;
    }

    // Deselect the UI button of the previously selected song-list
    private void deselectRadioButton(RadioButton mostRecentSelected, RadioButton selected) // Put this is 'UI script'
    {
        // Handles the case where user hasn't selected any list in advance
        if (mostRecentSelected == null)
            return;

        // Inactivates the latest song-list and updates its index accordingly
        if (mostRecentSelected != selected)
        {
            mostRecentSelected.setSelected(false);
            SongList.previousListIndex = SongList.listIndex;
        }
    }

    // When the user clicks the 'Pause' checkbox the current song being played pauses/continues depending on the button's previous state
    public void pauseSong()
    {
        SongUtils.togglePause(pauseCheckBox.isSelected());
    }
}