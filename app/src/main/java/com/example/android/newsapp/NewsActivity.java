package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>
{

    private static final String TAG = NewsActivity.class.getName();
    private static final int NEWS_LOADER_ID = 1;
    private static final String REQUEST_URL = "https://content.guardianapis.com/search?q=";
    private static final String END_TEST_KEY_URL = "&api-key=test";

    private TextView emptyTextView;
    private SearchView searchView;
    private View loadingIndicator;
    private NewsAdapter adapter;
    private String searchUrl;
    private String userFilterUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        emptyTextView = (TextView) findViewById(R.id.empty_view);
        emptyTextView.setText(getString(R.string.look_for_news));
        searchView = (SearchView) findViewById(R.id.search_view);
        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        ListView newsListView = (ListView) findViewById(R.id.list);
        newsListView.setEmptyView(emptyTextView);
        adapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(adapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News currentNews = adapter.getItem(i);

                Uri newsUri = Uri.parse(currentNews.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                if (websiteIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(websiteIntent);
                }

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.clear();
                searchUrl = "";
                userFilterUrl = searchView.getQuery().toString();
                searchUrl = REQUEST_URL + userFilterUrl + END_TEST_KEY_URL;

                searchUrl = searchUrl.replaceAll(" ","+");

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected())
                {
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.restartLoader(NEWS_LOADER_ID,null,NewsActivity.this);
                }
                else
                {
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                    emptyTextView.setText(getString(R.string.no_internet));
                }
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public android.content.Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        loadingIndicator.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
        return new NewsLoader(this,searchUrl);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<News>> loader, List<News> newses) {

        loadingIndicator.setVisibility(View.GONE);
        emptyTextView.setText(getString(R.string.no_news_found));
        adapter.clear();
        if(newses != null && !newses.isEmpty())
        {
            adapter.addAll(newses);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<News>> loader) {
        adapter.clear();
    }
}
