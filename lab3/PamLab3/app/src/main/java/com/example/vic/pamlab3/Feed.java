package com.example.vic.pamlab3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vic on 1/4/18.
 */

public class Feed implements Parcelable
{
    private int id;
    private String category;


    private String created_at;

    public Feed(Parcel in)
    {
        this.id = in.readInt();
        this.category = in.readString();
    }

    public Feed(int id, String category)
    {
        this.id = id;
        this.category = category;
    }

    public Feed()
    {

    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getCreated_at()
    {
        return created_at;
    }

    public void setCreated_at(String created_at)
    {
        this.created_at = created_at;
    }

    @Override
    public String toString()
    {
        return "Feed{" +
                "id=" + id +
                ", category='" + category + '\'' +
                '}';
    }

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(category);
    }


    public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {
        public Feed createFromParcel(Parcel in)
        {
            return new Feed(in);
        }
        public Feed[] newArray(int size)
        {
            return new Feed[size];
        }
    };

}
