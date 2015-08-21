package com.liferay.mobile.screens.messageboardscreenlet.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by darshan on 14/8/15.
 */
public class MyModel implements Parcelable {

    protected MyModel(Parcel in) {
    }

    public MyModel(Map<String, Object> values) {
    }

    public static final Creator<MyModel> CREATOR = new Creator<MyModel>() {
        @Override
        public MyModel createFromParcel(Parcel in) {
            return new MyModel(in);
        }

        @Override
        public MyModel[] newArray(int size) {
            return new MyModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
