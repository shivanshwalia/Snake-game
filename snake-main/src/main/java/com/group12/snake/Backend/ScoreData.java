package com.group12.snake.Backend;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class ScoreData implements Serializable, Comparable {

    private long timestamp;
    private int value;

    public ScoreData(int value) {

        this.timestamp = Instant.now().getEpochSecond();
        this.value = value;

    }

    public ScoreData(int value, String timestamp) throws ParseException {

        this.value = value;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m");
        this.timestamp = formatter.parse(timestamp).toInstant().getEpochSecond();

    }


    /*Parses the score date into a readable format*/
    public String getDate() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:m");
        return formatter.format(new Date(this.timestamp * 1000));

    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.format("Score: %d at %s", this.getValue(), this.getDate());
    }

    @Override
    public int compareTo(Object o) {

        if(o == null) return 0;
        ScoreData otherScore = (ScoreData) o;
        return otherScore.getValue() - this.getValue();

    }
}
