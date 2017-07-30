package swindroid.suntime.ui;

/**
 * Created by sandhu on 19/10/2016.
 */

public class RangeItem {
    String date,sunrise,sunset;

    public RangeItem(String date, String sunrise, String sunset) {
        this.date = date;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
