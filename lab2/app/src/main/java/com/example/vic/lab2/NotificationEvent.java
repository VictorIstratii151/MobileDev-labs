package com.example.vic.lab2;

/**
 * Created by vic on 11/6/17.
 */

public class NotificationEvent
{
    private int id;
//    private long pendingIntent_id;
    private String name;
    private String note;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public NotificationEvent(int id, String name, String note, int year, int month, int day, int hour, int minute)
    {
        this.id = id;
//        this.pendingIntent_id = pendingIntent_id;
        setName(name);
        setNote(note);
        setYear(year);
        setMonth(month);
        setDay(day);
        setHour(hour);
        setMinute(minute);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

//    public long getPendingIntent_id() { return pendingIntent_id; }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getNote()
    {
        return this.note;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getYear()
    {
        return this.year;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public int getMonth()
    {
        return this.month;
    }

    public void setDay(int day)
    {
        this.day = day;
    }

    public int getDay()
    {
        return this.day;
    }

    public void setHour(int hour)
    {
        this.hour = hour;
    }

    public int getHour()
    {
        return this.hour;
    }

    public void setMinute(int minute)
    {
        this.minute = minute;
    }

    public int getMinute()
    {
        return this.minute;
    }

    @Override
    public String toString()
    {
        return "Event [id = " + getId() +  ", name = " + getName() + ", note = " + getNote() + ", year = " + getYear() +
                ", month = " + getMonth() + ", day = " + getDay() + ", hour = " + getHour() + ", minute = " + getMinute() + "]";
    }
}
