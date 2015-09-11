package com.qrpasswds;

import android.os.Parcel;
import android.os.Parcelable;

public class Credential implements Parcelable {

    public String type;
    public String user;
    public String pass;
    public boolean isSelected = false;

    public Credential(String t, String u, String p) {
        type = (t.length()) > 0 ? t : "";
        user = (u.length()) > 0 ? u : "";
        pass = (p.length()) > 0 ? p : "";
    }

    public Credential(Parcel in){
        type = in.readString();
        user = in.readString();
        pass = in.readString();
        isSelected = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(user);
        dest.writeString(pass);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Credential createFromParcel(Parcel in) {
                    return new Credential(in);
                }

                public Credential[] newArray(int size) {
                    return new Credential[size];
                }
            };
}
