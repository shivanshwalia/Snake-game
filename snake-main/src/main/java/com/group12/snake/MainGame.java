package com.group12.snake;

import com.group12.snake.Backend.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.group12.snake.Backend.Utils.SongUtils;

public class MainGame extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("StartMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Snake");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        SongList.songListIndicesBoundaries = SongList.getSongListIndicesBoundaries();

        SongList.addSongs(new String[] {
                "Lazy Love - KEM.wav", "Music Is - Pryces.wav", "Bees In The Garden - Moire.wav", // ChillList
                "Sky High - Trinity.wav", "Energy I Need - Pecan Pie.wav",                        // TrapList
                "The Flower - RA.wav", "Jazz And Hop - kidcut.wav",                               // HipHopList
                "Disco Street - Andrey Rossi.wav", "Soulful Sparks - Soundroll.wav"               // DiscoList
        });

        SongUtils.startAudioClip(false);
    }

    public static void main(String[] args) {
        launch();
    }
}