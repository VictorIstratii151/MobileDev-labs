package com.example.vic.lab2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.NoiseSuppressor;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.attr.type;

/**
 * Created by vic on 11/8/17.
 */



public class NotificationService extends Service
{
    Gson gson = new Gson();
    Type type = new TypeToken<List<NotificationEvent>>() {}.getType();
    private final IBinder mBinder = new LocalBinder();
    public String readFile(Context context, String filename) {
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }

    public File writeToFile(Context context, String filename, String data)
    {
        File file = new File(context.getFilesDir(), filename);
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public void clearFile(Context context, String filename)
    {
        PrintWriter writer;
        File file = new File(context.getFilesDir(), filename);
        try
        {
            writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("\nFILE NOT FOUND\n");
        }
    }


    public class LocalBinder extends Binder
    {
        NotificationService getService()
        {
            return NotificationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    public void sayHello()
    {
        System.out.println("\nAYYYYYYYYYYYY WADDDUPP\n");
    }

    public void clearNotifications(int numba, Intent alarmIntent, Context context)
    {
        AlarmManager manager = (AlarmManager) this.getSystemService(ALARM_SERVICE);

        for(int i = 0; i < numba; i++)
        {
            PendingIntent pi = PendingIntent.getBroadcast(context, i, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            manager.cancel(pi);
        }
    }

    public List<NotificationEvent> serialize(List<NotificationEvent> dataList, String filename, Context context, Intent intent)
    {
        dataList = normalizeNotifications(dataList, intent, context);
        clearFile(context, filename);
        String json = gson.toJson(dataList, type);
        File file = writeToFile(context, filename, json);

        return dataList;
    }

    public void registerAlarm(int id, Intent intent, long timeInMillis, Context context)
    {
        AlarmManager manager = (AlarmManager) this.getSystemService(ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        if (android.os.Build.VERSION.SDK_INT >= 19)
        {
            manager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
        else
        {
            manager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }

    public List<NotificationEvent> normalizeNotifications(List<NotificationEvent> list, Intent intent, Context context)
    {
        System.out.println("\nyoyoyo normalize list ejje\n");
        System.out.println("\n" + list + "\n");
        int i = 0;
        for(NotificationEvent e : list)
        {
            i++;
            if(e.getId() != i)
            {
                e.setId(i);

                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, e.getYear());
                myCalendar.set(Calendar.MONTH, e.getMonth() - 1);
                myCalendar.set(Calendar.DAY_OF_MONTH, e.getDay());
                myCalendar.set(Calendar.HOUR_OF_DAY, e.getHour());
                myCalendar.set(Calendar.MINUTE, e.getMinute());

                registerAlarm(e.getId(), intent, myCalendar.getTimeInMillis(), context);
            }
        }
        return list;
    }
    public <T> List<T> deserialize(Class<T> klassz, String filename, Context context)
    {
        String readData =  readFile(context, filename);
        if(readData.length() == 0)
        {
            return new ArrayList<T>();
        }
        else
        {
            List<T> fromJson = gson.fromJson(readData, type);
            return fromJson;
        }
    }

}
