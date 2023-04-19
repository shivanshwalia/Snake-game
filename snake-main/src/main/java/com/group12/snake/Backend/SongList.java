package com.group12.snake.Backend;

import com.group12.snake.Backend.Utils.SongUtils;
import com.group12.snake.Frontend.Utils.UISongUtils;
import com.group12.snake.Frontend.Utils.UIUtils;
import com.group12.snake.Backend.Utils.Utils;
import com.group12.snake.Frontend.Controller;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

// Note: This is a back-end class with general behaviour and logic that heavily cooperates with the front-end-related class 'UIUtils'

// Justification of design choice:
/*
    The variables should be static and belong to the class rather than to objects.
    Allowing multiple objects means enabling multiple song lists at once in the gameplay.
    Having a single player snake-game with more than one song list or radio would create
    confusion and hurt the user experience.

    However, if it would be a multiplayer game, where each player would have their own
    radio, it would be appropriate to capitalize upon the programming feature of creating
    several objects of same type that stores unique values (in this case selected list and
    selected song)
 */

public class SongList
{
    public static int listIndex = 0;
    public static int songIndex = 0;
    public static int previousListIndex = 0;
    public static ArrayList<String> songs = new ArrayList<>();
    public static int[][] songListIndicesBoundaries;

    // Note for developers:
    // The two variables below needs to be updates manually when adding a new songlist:
    public static final int[] numberOfSongsInList = {3, 2, 2, 2};
    public static final String[] listNames = {"ChillList", "TrapList", "HipHopList", "DiscoList"};

    // Adds all songs to the list 'songs'
    // Note for developer: The strings passed in as a parameter must be in correct order
    public static void addSongs(String[] newSongs)
    {
        songs.addAll(Arrays.asList(newSongs));
    }

    // When the user toggles song-list, the first song in the list is being played as the UI-texts are updated
    public static void toggleSongList(int newListIndex, boolean songIsPaused)
    {
        listIndex = newListIndex;
        songIndex = songListIndicesBoundaries[listIndex][0];

        SongUtils.startAudioClip(songIsPaused);
        UIUtils.updateText("#songNameText", songs.get(songIndex), true);
    }

    // Updates the picture of the current song
    public static void synchronizeThumbnailWithSong()
    {
        String list = getCurrentList();
        String newSong = getCurrentSong();

        String commonPath = "assets/";
        Image newSongImage = new Image(commonPath + list + "/" + Utils.removeNLastCharactersInString(newSong, 4) + ".png");

        ImageView songPictureDisplayer = UIUtils.getImageView("#songPictureDisplayer");
        songPictureDisplayer.setImage(newSongImage);
    }

    // Updates a song-text associated with the current song-list in terms of type of font and color gradients
    public static void updateSelectedSongText(int previousSongIndex, boolean userSwitchedList)
    {
        int songIdIndex = getSongInListIndex(songIndex, false);
        previousSongIndex = getSongInListIndex(previousSongIndex, userSwitchedList);

        Text selectedText = UIUtils.getText("#song" + songIdIndex);
        Text previouslySelectedText = UIUtils.getText("#song" + previousSongIndex);

        Text[] textsThatChangesColors = new Text[]{previouslySelectedText, selectedText};

        previouslySelectedText.setFont(UIUtils.getNormalFont("Verdana", UISongUtils.songTextFontSize));
        selectedText.setFont(UIUtils.getSelectedFont("Verdana", false, UISongUtils.songTextFontSize));

        UIUtils.changeTextColor(textsThatChangesColors, true, 2);
    }

    // Returns the index for the song within the list itself
    // without regards to its index in the entirety of all the stored songs in the ArrayList<String> songs
    private static int getSongInListIndex(int index, boolean userSwitchedList)
    {
        // If the user switched list, we need the previous text-index for the previous list
        if (userSwitchedList)
        {
            return index - songListIndicesBoundaries[previousListIndex][0];
        }

        return index - songListIndicesBoundaries[listIndex][0];
    }

    // Returns a 2D-array where each array-row is the index-interval for each song-list
    // Numerical examples of what the method does:
    // LHS = 'numberOfSongsInList'          RHS = Arrays storing i:th index-interval in 'songs' for i:th element in 'numberOfSongsInList'
    // Example 1:   [3, 2]             --->        [0, 2], [3, 4]
    // Example 2:   [3, 2, 3, 4, 2]    --->        [0, 2], [3, 4], [5, 7], [8, 11], [12, 13]
    public static int[][] getSongListIndicesBoundaries()
    {
        int[][] listBoundaries = new int[numberOfSongsInList.length][];

        int min = 0;
        int max = SongList.numberOfSongsInList[0] - 1;

        for (int i = 0; i < numberOfSongsInList.length - 1; i++)
        {
            listBoundaries[i] = new int[]{min, max};

            min += SongList.numberOfSongsInList[i];
            max += SongList.numberOfSongsInList[i + 1];
        }

        listBoundaries[listBoundaries.length - 1] = new int[]{min, max};
        return listBoundaries;
    }

    public static int getNumberOfSongLists()
    {
        return numberOfSongsInList.length;
    }

    public static String getCurrentSong()
    {
        return songs.get(songIndex);
    }

    public static String getCurrentList()
    {
        return listNames[listIndex];
    }
}
