package com.example.srecko.agrohelper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Srečko on 17. 05. 2016.
 */
public class DataParcel implements Parcelable {
    private  int mData;
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<DataParcel> CREATOR = new Parcelable.Creator<DataParcel>() {
        public DataParcel createFromParcel(Parcel in) {
            return new DataParcel(in);
        }

        public DataParcel[] newArray(int size) {
            return new DataParcel[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private DataParcel(Parcel in) {
        mData = in.readInt();
    }
}
