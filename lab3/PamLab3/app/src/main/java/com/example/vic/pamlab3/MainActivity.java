package com.example.vic.pamlab3;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    List<Feed> feedList = new ArrayList<Feed>();
    List<Novelty> noveltyList = new ArrayList<Novelty>();
    String selectedCategory;
    private MyDBHelper myDB;
    private Spinner spinner;
    private final String myUrl = "http://abcnews.go.com/abcnews/";
    private String completeUrl;
    public static final String[] categories = { "topstories", "internationalheadlines", "usheadlines", "politicsheadlines", "blotterheadlines" };
    public static final String[] fancyCategories = { "Top Stories", "International Headlines", "US Headlines", "Politics Headlines", "Blotter Headlines" };
    private HandleXML obj;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new MyDBHelper(this);
        feedList = myDB.getAllFeed();
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, fancyCategories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button btnFetchRss = (Button) findViewById(R.id.buttonFetchRss);
        btnFetchRss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Feed f = new Feed();
                f.setCategory(selectedCategory);
                long feedId = myDB.createFeed(f);
//                System.out.println("\n" + feedId + "\n");
                obj = new HandleXML(selectedCategory);
                obj.fetchXML();

                Toast.makeText(MainActivity.this, "Please wait.", Toast.LENGTH_SHORT).show();
                try
                {
                    Thread.sleep(3000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                long feed_id = Arrays.asList(categories).indexOf(selectedCategory) + 1;
                noveltyList = obj.getList();
                noveltyList.remove(0);

                for(Novelty item : noveltyList)
                {
                    myDB.createNovelty(item, feed_id);
                }
            }
        });

        Button btnDeleteFeed = (Button) findViewById(R.id.button3);
        btnDeleteFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                feedList = myDB.getAllFeedByCategory(selectedCategory);
                Log.e(myDB.LOG, String.valueOf(feedList.size()));
                for(Feed item : feedList)
                {
                    myDB.deleteFeed(item, true);
                }
                feedList.clear();
            }
        });

        Button btnViewRss = (Button) findViewById(R.id.buttonViewRss);
        btnViewRss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                feedList = myDB.getAllFeedByCategory(selectedCategory);



                if(feedList.size() == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Wait");
                    builder.setMessage("There is no feed for this category.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    });

                    final AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), ViewRssActivity.class);
                    Bundle bundle = new Bundle();
                    int i = 0;
                    for(Feed item : feedList)
                    {
                        bundle.putParcelable(String.valueOf(i), item);
                        i += 1;
                    }
                    intent.putExtras(bundle);
                    int index = 0;
                    for(int j = 0; j < categories.length; j++)
                    {
                        if(categories[j] == selectedCategory)
                        {
                            index = j;
                        }
                    }
                    intent.putExtra("category", fancyCategories[index]);
                    intent.putExtra("count", i);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        completeUrl = myUrl + categories[position];
        selectedCategory = categories[position];
//        Toast.makeText(this, completeUrl, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
