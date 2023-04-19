package com.group12.snake.Backend.Utils;

import com.group12.snake.Backend.SongList;
import com.group12.snake.Frontend.Controller;
import com.group12.snake.Frontend.Utils.UIUtils;
import javafx.scene.control.ProgressBar;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

// Sources retrieved:
// https://stackoverflow.com/questions/12908412/print-hello-world-every-x-seconds (2022-12-15): The one with 210+ upvotes
// https://www.youtube.com/watch?v=wJO_cq5XeSA (Retrieved 2022-12-15)

public class SongUtils
{
    public static Clip currentClip;
    public static Clip mostRecentClip = null;

    // Starts playing a new song
    public static void startAudioClip(boolean songIsPaused)
    {
        try
        {


            String commonPath = "target/classes/assets/Songs/";
            String specificSongPath = SongList.getCurrentList() + "/" + SongList.getCurrentSong();

            File musicPath = new File(commonPath + specificSongPath);

            // The song was found and is playable
            if (musicPath.exists())
            {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();

                stopLatestClip();
                clip.open(audioInput);

                // If the user selects a new song while the 'pause' option is selected, the new song shouldn't be played, only selected
                if (!songIsPaused)
                {
                    clip.start();
                }

                setStatesOfCurrentClip(clip);
            }

            // The song wasn't found
            else
            {
                System.out.println("File was not found!");
            }
        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    // Registers new states of the two most recent audio-clips when a new song starts
    private static void setStatesOfCurrentClip(Clip currentClip)
    {
        SongUtils.currentClip = currentClip;
        mostRecentClip = currentClip;
    }

    // Stops playing the most recent clip once the next song starts
    private static void stopLatestClip()
    {
        if (mostRecentClip != null)
        {
            mostRecentClip.close();
            mostRecentClip.stop();
        }
    }

    // Toggle song in current song list: Is executed when user presses 'Q', 'E' or when song is finished
    public static void toggleSong(boolean increase, boolean songIsPaused) // Note for JoelM: Put in SongList.java in next commit
    {
        // User clicks 'E' and moves on to the next song in the current song list
        if (increase)
        {
            SongList.songIndex++;
        }

        // User clicks 'Q' and goes back to the previous song
        else
        {
            SongList.songIndex--;
        }

        SongList.songIndex = Utils.limitValue(SongList.songIndex, SongList.songListIndicesBoundaries[SongList.listIndex]);
        startAudioClip(songIsPaused);
        UIUtils.updateText("#songNameText", SongList.songs.get(SongList.songIndex), true);
    }

    // Pauses or unpauses the current song depending on previous pause-state
    public static void togglePause(boolean pause)
    {
        if (pause)
        {
            currentClip.stop();
        }
        else
            currentClip.start();
    }

    // Update the visual progression of the bar and display it to the user
    public static void updateSongBarProgression()
    {
        double barProgression = (Utils.setCommaNDigitsFromEnd(currentClip.getMicrosecondPosition(), 3) / (double) Utils.setCommaNDigitsFromEnd(currentClip.getMicrosecondLength(), 3));

        // Toggle/Play next song if the current song has completed
        if (barProgression == 1)
        {
            toggleSong(true, false);
            SongList.synchronizeThumbnailWithSong();
        }

        ProgressBar bar = (ProgressBar) (Controller.scene.lookup("#songProgressBar"));
        bar.setProgress(barProgression);
    }

    // Stops and closes the current song as the user exits the game
    public static void stop()
    {
        currentClip.close();
        currentClip.stop();
    }
}
