package com.example.vic.pamlab3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by vic on 1/4/18.
 */

public class MyDBHelper extends SQLiteOpenHelper
{
    public static final String LOG = "DBhelper";
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Pam3Lab.db";

    public static final String TABLE_FEED= "feed";
    public static final String TABLE_NOVELTIES = "novelties";
    public static final String TABLE_NOVELTIES_FEED = "novelties_feed";

    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "created_at";

    public static final String KEY_CATEGORY = "category";

    public static final String KEY_IMAGE_URI = "image_uri";
    public static final String KEY_TITLE = "title";
    public static final String KEY_LINK = "link";
    public static final String KEY_DESCRIPTION = "description";

    public static final String KEY_FEED_ID = "feed_id";
    public static final String KEY_NOVELTY_ID = "novelty_id";

    public static final String CREATE_TABLE_FEED = "CREATE TABLE "
            + TABLE_FEED + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CATEGORY
            + " TEXT, " + KEY_CREATED_AT + " DATETIME " + ")";

    public static final String CREATE_TABLE_NOVELTIES = "CREATE TABLE "
            + TABLE_NOVELTIES + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_IMAGE_URI + " TEXT, "
            + KEY_TITLE + " TEXT, " + KEY_LINK + " TEXT, " + KEY_DESCRIPTION + " TEXT, "
            + KEY_CREATED_AT + " DATETIME " + ")";

    public static final String CREATE_TABLE_NOVELTIES_FEED = "CREATE TABLE "
            + TABLE_NOVELTIES_FEED + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_FEED_ID + " INTEGER, " + KEY_NOVELTY_ID + " INTEGER, "
            + KEY_CREATED_AT + " DATETIME " + ")";

    public MyDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_FEED);
        db.execSQL(CREATE_TABLE_NOVELTIES);
        db.execSQL(CREATE_TABLE_NOVELTIES_FEED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int OldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOVELTIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOVELTIES_FEED);

        onCreate(db);
    }


    public long createNovelty(Novelty novelty, long feed_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE_URI, novelty.getImageUri());
        values.put(KEY_TITLE, novelty.getTitle());
        values.put(KEY_LINK, novelty.getLink());
        values.put(KEY_DESCRIPTION, novelty.getDescription());
        values.put(KEY_CREATED_AT, getDateTime());

        long novelty_id = db.insert(TABLE_NOVELTIES, null, values);

        createNoveltyFeed(novelty_id, feed_id);


        return novelty_id;
    }

    public Novelty getNovelty(long novelty_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NOVELTIES + " WHERE "
                + KEY_ID + " = " + novelty_id;
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null)
        {
            c.moveToFirst();
        }

        Novelty nv = new Novelty();
        nv.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        nv.setImageUri(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
        nv.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
        nv.setLink(c.getString(c.getColumnIndex(KEY_LINK)));
        nv.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));

        return nv;
    }

    public List<Novelty> getAllNovelties()
    {
        List<Novelty> novelties = new ArrayList<Novelty>();
        String selectQuery = "SELECT * FROM " + TABLE_NOVELTIES;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst())
        {
            do
            {
                Novelty nv = new Novelty();
                nv.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                nv.setImageUri(c.getString(c.getColumnIndex(KEY_IMAGE_URI)));
                nv.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                nv.setLink(c.getString(c.getColumnIndex(KEY_LINK)));
                nv.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));

                novelties.add(nv);
            } while(c.moveToNext());
        }

        return novelties;
    }

    public List<Novelty> getAllNoveltiesByFeedId(long feedId)
    {
        List<Novelty> novelties = new ArrayList<Novelty>();

        String selectQuery = "SELECT * FROM " + TABLE_NOVELTIES
                + " WHERE " + KEY_ID + " IN(SELECT " + KEY_NOVELTY_ID + " FROM "
                + TABLE_NOVELTIES_FEED + " WHERE " + KEY_FEED_ID + " = " + feedId + ")";
//        String selectQuery = "SELECT * FROM " + TABLE_NOVELTIES + " nv, "
//                + TABLE_FEED + " fd, " + TABLE_NOVELTIES_FEED + " nf WHERE fd."
//                + KEY_CATEGORY + " = '" + feed_category + "'" + " AND fd." + KEY_ID
//                + " = " + "nf." + KEY_FEED_ID + " AND nv." + KEY_ID + " = "
//                + "nf." + KEY_NOVELTY_ID;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst())
        {
            do
            {
                Novelty nv = new Novelty();
                nv.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                nv.setImageUri(c.getString(c.getColumnIndex(KEY_IMAGE_URI)));
                nv.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                nv.setLink(c.getString(c.getColumnIndex(KEY_LINK)));
                nv.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));

                novelties.add(nv);
            } while(c.moveToNext());
        }

        return novelties;
    }

    public List<Novelty> getAllNoveltiesByFeedCategory(String feed_category)
    {
        List<Novelty> novelties = new ArrayList<Novelty>();

        String selectQuery = "SELECT * FROM " + TABLE_NOVELTIES + " nv, "
                + TABLE_FEED + " fd, " + TABLE_NOVELTIES_FEED + " nf WHERE fd."
                + KEY_CATEGORY + " = '" + feed_category + "'" + " AND fd." + KEY_ID
                + " = " + "nf." + KEY_FEED_ID + " AND nv." + KEY_ID + " = "
                + "nf." + KEY_NOVELTY_ID;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst())
        {
            do
            {
                Novelty nv = new Novelty();
                nv.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                nv.setImageUri(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                nv.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                nv.setLink(c.getString(c.getColumnIndex(KEY_LINK)));
                nv.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));

                novelties.add(nv);
            } while(c.moveToNext());
        }

        return novelties;
    }

    public int updateNovelty(Novelty novelty)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE_URI, novelty.getImageUri());
        values.put(KEY_TITLE, novelty.getTitle());
        values.put(KEY_LINK, novelty.getLink());
        values.put(KEY_DESCRIPTION, novelty.getDescription());

        return db.update(TABLE_NOVELTIES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(novelty.getId()) });
    }

    public void deleteNovelty(long novelty_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOVELTIES, KEY_ID + " = ?",
                new String[] { String.valueOf(novelty_id) });
    }

    public long createFeed(Feed feed)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY, feed.getCategory());
        values.put(KEY_CREATED_AT, getDateTime());

        long feed_id = db.insert(TABLE_FEED, null, values);

        return feed_id;
    }

    public Feed getFeed(long feed_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_FEED + " WHERE "
                + KEY_ID + " = " + feed_id;
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null)
        {
            c.moveToFirst();
        }

        Feed fd = new Feed();
        fd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        fd.setCategory(c.getString(c.getColumnIndex(KEY_CATEGORY)));

        return fd;
    }

    public List<Feed> getAllFeed()
    {
        List<Feed> feed  = new ArrayList<Feed>();
        String selectQuery = "SELECT * FROM " + TABLE_FEED;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst())
        {
            do
            {
                Feed f = new Feed();
                f.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                f.setCategory(c.getString(c.getColumnIndex(KEY_CATEGORY)));

                feed.add(f);
            } while(c.moveToNext());
        }

        return feed;
    }

    public List<Feed> getAllFeedByCategory(String category)
    {
        List<Feed> feed  = new ArrayList<Feed>();
        String selectQuery = "SELECT * FROM " + TABLE_FEED + " WHERE " + KEY_CATEGORY
                + " = '" + category + "' ";
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst())
        {
            do
            {
                Feed f = new Feed();
                f.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                f.setCategory(c.getString(c.getColumnIndex(KEY_CATEGORY)));

                feed.add(f);
            } while(c.moveToNext());
        }

        return feed;
    }

    public int updateFeed(Feed feed)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY, feed.getCategory());

        return db.update(TABLE_FEED, values, KEY_ID + " = ?",
                new String[] { String.valueOf(feed.getId()) });
    }

    public void deleteFeed(Feed feed, boolean deletion_flag)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        if(deletion_flag)
        {
            List<Novelty> allFeedNovelties = getAllNoveltiesByFeedCategory(feed.getCategory());

            for(Novelty novelty : allFeedNovelties)
            {
                deleteNovelty(novelty.getId());
            }
        }

        db.delete(TABLE_FEED, KEY_ID + " = ?",
                new String[] { String.valueOf(feed.getId()) });
    }

    public long createNoveltyFeed(long novelty_id, long feed_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOVELTY_ID, novelty_id);
        values.put(KEY_FEED_ID, feed_id);
        values.put(KEY_CREATED_AT, getDateTime());

        long id = db.insert(TABLE_NOVELTIES_FEED, null, values);

        return id;
    }

    public int updateNoveltyFeed(long id, long feed_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FEED_ID, feed_id);

        return db.update(TABLE_NOVELTIES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public void deleteNoveltyFeed(long id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOVELTIES, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });

    }

    public void closeDB()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null && db.isOpen())
        {
            db.close();
        }
    }

    private String getDateTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        return dateFormat.format(date);
    }
}
