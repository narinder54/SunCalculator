package swindroid.suntime.ui;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sandhu on 22/09/2016.
 */

public class Location implements Parcelable{
    private String city_name;
    private String latitude;
    private String longitude;
    private String time_zone;

    public Location(String city_name, String latitude,String longitude, String time_zone) {
        this.city_name = city_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time_zone = time_zone;
    }

    private Location(Parcel in)
    {
        this.city_name = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.time_zone = in.readString();
    }
    public void Update(String city_name, String latitude, String longitude, String time_zone) {
        this.city_name = city_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time_zone = time_zone;
    }
    /** Code for Parcel and unparcel of data among different activities**/


    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(city_name);
        out.writeString(latitude);
        out.writeString(longitude);
        out.writeString(time_zone);
    }

    public static final Parcelable.Creator<Location> CREATOR =
            new Parcelable.Creator<Location>()
            {
                public Location createFromParcel(Parcel in)
                {
                    return new Location(in);
                }

                public Location[] newArray(int size)
                {
                    return new Location[size];
                }
            };


    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return city_name +","+ latitude + ","+longitude+","+ time_zone;
    }
}
