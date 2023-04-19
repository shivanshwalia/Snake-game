package com.group12.snake.Backend.Utils;

public class Utils
{
    // Used to remove ".wav" for every song in this project
    public static String removeNLastCharactersInString(String stringText, int n)
    {
        return stringText.substring(0, stringText.length() - n);
    }

    // Source used: https://stackoverflow.com/questions/29012224/java-how-to-remove-last-numbers-of-a-long (retrieved 2022-12-15)
    // The purpose with this function is that Java completely ignores the comma located 3 digits from the last digit
    // when retrieving a clip's length using 'clip.getMicrosecondLength'
    public static long setCommaNDigitsFromEnd(long number, int n)
    {
        return (long)(number / Math.pow(10, n));
    }

    public static int limitValue(int value, int[] limits)
    {
        if (value > limits[1])
            return limits[0];
        else if (value < limits[0])
            return limits[1];

        return value;
    }

    public static boolean checkIfIndexIsWithinRangeOfList(int lengthOfList, int index)
    {
        return index >= 0 && index < lengthOfList;
    }
}
