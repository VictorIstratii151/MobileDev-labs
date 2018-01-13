package com.example.vic.pamlab3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ViewRssActivity extends AppCompatActivity
{
    private MyDBHelper myDB;
    private List<Novelty> novelties = new ArrayList<Novelty>();

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rss);

        myDB = new MyDBHelper(this);
        Bundle bundle = getIntent().getExtras();
        List<Feed> receivedList = new ArrayList<Feed>();
        String cat = bundle.getString("category");
        int count = bundle.getInt("count");

        TextView tvName = (TextView) findViewById(R.id.textViewRssCategory);
        tvName.setText(cat);
        for(int i = 0; i < count; i++)
        {
            Feed addItem = bundle.getParcelable(String.valueOf(i));
            receivedList.add(addItem);
        }

        for(Feed item : receivedList)
        {
            long id = item.getId();
            List<Novelty> items  = myDB.getAllNoveltiesByFeedId(item.getId());
            novelties.addAll(items);
        }

        Log.e(myDB.LOG, String.valueOf(novelties.size()));


        RecyclerView rvNovelties = (RecyclerView) findViewById(R.id.recyclerViewNews);
        RecyclerAdapter adapter = new RecyclerAdapter(this, novelties);
        rvNovelties.setAdapter(adapter);
        rvNovelties.setLayoutManager(new LinearLayoutManager(this));
    }
}
