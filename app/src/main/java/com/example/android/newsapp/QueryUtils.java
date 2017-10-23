package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 25.06.2017.
 */

public final class QueryUtils {

    private static final String TAG = QueryUtils.class.getName();
    private static final String titleKey = "webTitle";
    private static final String webUrlKey = "webUrl";
    private static final String dateKey = "webPublicationDate";
    private static final String categoryKey = "sectionName";
    private static final String responseKey = "response";
    private static final String resultsKey = "results";

    public static URL createUrl(String stringUrl)
    {
        URL url = null;

        try
        {
            url = new URL(stringUrl);
        }
        catch (MalformedURLException e)
        {
            Log.e(TAG,"Cannot create url",e);
        }

        return url;
    }

    public static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();

        if(inputStream!=null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line!=null)
            {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }

    public static String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse = "";

        if(url==null)
        {
            return jsonResponse;
        }

        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(1500);
            urlConnection.setReadTimeout(1000);
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse=readFromStream(inputStream);
            }
            else
            {
                Log.e(TAG,"Error response code: "+urlConnection.getResponseCode());
            }

        }
        catch (IOException e)
        {
           Log.e(TAG,"Connection problem",e);
        }

        finally
        {
            if(urlConnection!=null)
            {
                urlConnection.disconnect();
            }
            if(inputStream!=null)
            {
                inputStream.close();
            }
        }

        return jsonResponse;
    }


    private static List<News>  extractFeatureFromJson (String newsJson)
    {
        if (TextUtils.isEmpty(newsJson))
        {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try
        {
            JSONObject jsonObject = new JSONObject(newsJson);
            JSONObject jsonObject1 = jsonObject.getJSONObject(responseKey);
            JSONArray newsArray = jsonObject1.getJSONArray(resultsKey);

            for(int i = 0; i<newsArray.length(); i++)
            {
                JSONObject currentNews = newsArray.getJSONObject(i);
                String title = currentNews.getString(titleKey);
                String webUrl = currentNews.getString(webUrlKey);
                String date = currentNews.getString(dateKey);
                String category = currentNews.getString(categoryKey);
                String author = "";

                if(title.contains("|"))
                {
                    String[] titleParts = title.split("[|]");
                    title = titleParts[0];
                    author = titleParts[1];
                    title = title.trim();
                    author = author.trim();
                }

                String[] dateSplit = date.split("T");
                date = dateSplit[0];

                News news = new News(title,author,date,category,webUrl);

                newsList.add(news);

            }
        }
        catch (JSONException e)
        {
           Log.e(TAG,"Problem parsing json",e);
        }


        return newsList;
    }

    public static List<News> fetchNewsData(String requestURL)
    {
        URL url = createUrl(requestURL);

        String jsonREsponse = null;

        try
        {
            jsonREsponse = makeHttpRequest(url);
        }
        catch (IOException e)
        {
           Log.e(TAG,"Connection problems",e);
        }

        List<News> newsList = extractFeatureFromJson(jsonREsponse);

        return newsList;

    }
}
