package com.example.vic.pamlab5;

/**
 * Created by vic on 1/14/18.
 */

public class DoctorModel
{
    int id;
    String name;
    String speciality;
    String address;
    String about;
    float rating;
    String photo;

    public DoctorModel()
    {

    }

    public DoctorModel(int id, String name, String speciality, String address, String about,
                       float rating, String photo)
    {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
        this.address = address;
        this.about = about;
        this.rating = rating;
        this.photo = photo;
    }


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSpeciality()
    {
        return speciality;
    }

    public void setSpeciality(String speciality)
    {
        this.speciality = speciality;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAbout()
    {
        return about;
    }

    public void setAbout(String about)
    {
        this.about = about;
    }

    public float getRating()
    {
        return rating;
    }

    public void setRating(float rating)
    {
        this.rating = rating;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }
}
