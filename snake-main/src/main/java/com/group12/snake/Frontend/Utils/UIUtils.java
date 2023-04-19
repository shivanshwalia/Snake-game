package com.group12.snake.Frontend.Utils;

import com.group12.snake.Frontend.Controller;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class UIUtils
{
    // Updates the string-value of the text below the song-thumbnail to the name of the current song
    public static void updateText(String textId, String newMessage, boolean isSong)
    {
        if (isSong)
        {
            newMessage = com.group12.snake.Backend.Utils.Utils.removeNLastCharactersInString(newMessage, 4);
        }

        Text text = getText(textId);
        text.setText(newMessage);
    }

    // This method changes the color of texts in an array, with two predetermined constant colors
    // Note that 'selectedSongTextGradient' is assumed to be the "default" color
    public static void changeTextColor(Text[] texts, boolean usePattern, int pattern)
    {
        // Do not use pattern - Change the color of each text to 'selectedSongTextGradient'
        if (!usePattern)
        {
            for (Text currentText : texts)
                currentText.setFill(UISongUtils.selectedSongTextGradient);
        }

        // Use pattern - The order of the texts in the array determines their color
        else
        {
            boolean useDefaultColor;

            for (int i = 0; i < texts.length; i++)
            {
                useDefaultColor = (i % pattern) == 1;

                if (useDefaultColor)
                    texts[i].setFill(UISongUtils.selectedSongTextGradient);
                else
                    texts[i].setFill(UISongUtils.normalSongTextGradient);
            }
        }
    }

    // Retrieve Text based on its assigned id in SceneBuilder
    public static Text getText(String id)
    {
        return (Text)(Controller.scene.lookup(id));
    }

    // Retrieve ImageView based on its assigned id in SceneBuilder
    public static ImageView getImageView(String id)
    {
        return (ImageView)(Controller.scene.lookup(id));
    }

    // Returns the normal font
    public static Font getNormalFont(String fontType, int size)
    {
        return Font.font(fontType, FontWeight.NORMAL, FontPosture.REGULAR, size);
    }

    // Returns the specified selected font
    public static Font getSelectedFont(String fontType, boolean italicFont, int size)
    {
        if (italicFont)
        {
            return Font.font(fontType, FontWeight.BOLD, FontPosture.ITALIC, size);
        }
        else
        {
            return Font.font(fontType, FontWeight.BOLD, size);
        }
    }
}
