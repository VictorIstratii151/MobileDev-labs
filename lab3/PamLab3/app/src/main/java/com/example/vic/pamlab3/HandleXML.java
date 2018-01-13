package com.example.vic.pamlab3;

import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.Console;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by vic on 1/3/18.
 */

public class HandleXML
{
    private String chosenCategory = null;
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    private ArrayList<Novelty> list = new ArrayList<>();
    private final String myUrl = "http://abcnews.go.com/abcnews/";
    private String category = "topstories";


    public volatile boolean parsingComplete = true;

    public HandleXML(String category)
    {
        this.urlString = myUrl + category;
        this.chosenCategory = category;
    }

    public ArrayList<Novelty> getList()
    {
        return this.list;
    }

    public void parseXMLAndStoreIt(XmlPullParser parser)
    {
        int event;
        String text=null;

        try
        {
            event = parser.getEventType();
            Novelty novelty = new Novelty();
            while (event != XmlPullParser.END_DOCUMENT)
            {
                String name = parser.getName();

                switch(event)
                {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        switch(name)
                        {
                            case "media:thumbnail":
                                if(parser.getAttributeValue(null, "width").equals("144"))
                                {
                                    text = parser.getAttributeValue(null, "url");
                                    novelty.setImageUri(text);
                                }

                                break;

                            case "title":
                                Log.e(MyDBHelper.LOG, text);
                                novelty.setTitle(text);

                                break;

                            case "link":
                                novelty.setLink(text);

                                break;

                            case "description":
                                novelty.setDescription(text);
                                Novelty temp = new Novelty(novelty.getImageUri(), novelty.getTitle(), novelty.getLink(), novelty.getDescription());
                                list.add(temp);
                                break;
                        }
                }
                event = parser.next();
            }
            parsingComplete = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

//        list.remove(0);

//        long ids[] = { Arrays.asList(MainActivity.categories).indexOf(chosenCategory) };
////        int categoryIndex = Arrays.asList(MainActivity.categories).indexOf(chosenCategory);
//
//        for(Novelty item : list)
//        {
//            long id =
//        }
//        for(Novelty item : list)
//        {
//            System.out.println(item.toString());
//        }
//        sas();
    }

    public void fetchXML()
    {
        Thread networkThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parseXMLAndStoreIt(myparser);
                    System.out.println("\naaaaaa\n");
                    stream.close();
                }

                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        networkThread.start();
    }
}
