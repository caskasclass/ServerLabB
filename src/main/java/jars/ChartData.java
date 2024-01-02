package jars;

import java.io.Serializable;

public class ChartData implements Serializable {
    private String emotionName;
    private double averageRating;
    private int totalRatings;

    public ChartData(String emotionName, double averageRating, int totalRatings) {
        this.emotionName = emotionName;
        this.averageRating = averageRating;
        this.totalRatings = totalRatings;
    }

    public String getEmotionName() {
        return emotionName;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }
}
