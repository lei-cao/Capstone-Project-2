package com.cao.lei.fit.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Represent the movie fetched from the API and passed between intents
 */
public class TrainingSet implements Parcelable {
//    public Long id;

    public String title;
    public String description;

    @SerializedName("video_url")
    public String videourl;
    public String thumbnail;
    public String image;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
//        out.writeLong(id);
        out.writeString(title);
        out.writeString(description);
        out.writeString(videourl);
        out.writeString(thumbnail);
        out.writeString(image);
    }

    public static final Parcelable.Creator<TrainingSet> CREATOR = new Parcelable.Creator<TrainingSet>() {
        public TrainingSet createFromParcel(Parcel in) {
            return new TrainingSet(in);
        }

        public TrainingSet[] newArray(int size) {
            return new TrainingSet[size];
        }
    };

    public TrainingSet(Parcel in) {
//        id = in.readLong();
        title = in.readString();
        description = in.readString();
        videourl = in.readString();
        thumbnail = in.readString();
        image = in.readString();
    }

    public TrainingSet() {
    }

}

