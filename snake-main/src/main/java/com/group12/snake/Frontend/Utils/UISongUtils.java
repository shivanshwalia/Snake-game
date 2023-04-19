package com.group12.snake.Frontend.Utils;

import com.group12.snake.Backend.SongList;
import com.group12.snake.Backend.Utils.Utils;
import com.group12.snake.Frontend.Controller;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;

public class UISongUtils
{
    public static final LinearGradient selectedSongTextGradient = new LinearGradient(
            0.0, 0.0, 0.6667, 1.0, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, new Color(0.95, 0.057, 0.057, 1.0)),
            new Stop(1.0, new Color(0.2526, 0.0229, 0.8842, 1.0)));

    public static final Color normalSongTextGradient = new Color(0.0, 0.0, 0.0, 1.0);
    public static final int songTextFontSize = 8;
    private static final int maximumSongsInList = 5;

    // Returns the list-buttons by searching for their id
    public static RadioButton[] getSongListsButtons()
    {
        RadioButton[] buttons = new RadioButton[SongList.getNumberOfSongLists()];

        for (int i = 0; i < buttons.length; i++)
        {
            String currentListTag = "#" + (Utils.removeNLastCharactersInString(SongList.listNames[i], 4)).toLowerCase(); // Removes "List" from retrieved string to match with assigned id in SceneBuilder
            buttons[i] = (RadioButton)(Controller.scene.lookup(currentListTag));
        }

        return buttons;
    }

    // Updates the string-value of a song to a name of a song in the newly selected list
    public static void setSongListTexts(Text songText, int i)
    {
        if (Utils.checkIfIndexIsWithinRangeOfList(SongList.numberOfSongsInList[SongList.listIndex], i))
        {
            int startIdxOfCurrentList = SongList.songListIndicesBoundaries[SongList.listIndex][0];

            String nameOfWAVSong = SongList.songs.get(startIdxOfCurrentList + i);
            String songNumber = (i + 1) + ". ";

            String nameOfSong = songNumber + Utils.removeNLastCharactersInString(nameOfWAVSong, 4);
            songText.setText(nameOfSong);
        }
    }

    // Sets a text's opacity based on if its index is within the range of the current song-list
    public static void setSongListTextsOpacity(Text songText, int i)
    {
        // Make text visible if its index is within the range of the length of the list
        if (Utils.checkIfIndexIsWithinRangeOfList(SongList.numberOfSongsInList[SongList.listIndex], i))
        {
            songText.setOpacity(1);
        }

        // Make text invisible if the size of the current list of songs doesn't support enough songs
        else
        {
            songText.setOpacity(0);
        }
    }

    // Updates the texts that represents the songs in the current list by setting their opacity and string-values accordingly
    public static void updateSongListTexts()
    {
        for (int i = 0; i < maximumSongsInList; i++)
        {
            Text songText = UIUtils.getText("#song" + i);

            setSongListTextsOpacity(songText, i);
            setSongListTexts(songText, i);
        }
    }
}
