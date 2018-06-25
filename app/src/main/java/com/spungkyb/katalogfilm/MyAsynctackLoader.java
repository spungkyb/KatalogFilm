package com.spungkyb.katalogfilm;

import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 19/06/2018.
 */

public class MyAsynctackLoader extends AsyncTaskLoader<ArrayList<FilmItems>> {
    private ArrayList<FilmItems> mData;
    private boolean mHasResult = false;
    private String mKumpulanFilm;
    private static final String API_KEY = "b86fa9de907d44742bb07418ef1072ff";

    public MyAsynctackLoader(final Context context, String kumpulanFilm) {
        super(context);

        onContentChanged();
        this.mKumpulanFilm = kumpulanFilm;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(final ArrayList<FilmItems> data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            onReleaseResources(mData);
            mData = null;
            mHasResult = false;
        }
    }

    @Override
    public ArrayList<FilmItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList<FilmItems> FilmItemses = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + mKumpulanFilm;
        Log.e("url", url + "");

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    String totalResult = responseObject.getString("total_results");
                    JSONArray results = responseObject.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject film = results.getJSONObject(i);
                        FilmItems filmItems = new FilmItems(film);
                        FilmItemses.add(filmItems);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Jika response gagal maka , do nothing
                //Toast.makeText(getContext().getApplicationContext(), "Gagal menampilkan Film", Toast.LENGTH_SHORT).show();
                Log.e("Gagal","gagal");
            }
        });
        return FilmItemses;
    }

    protected void onReleaseResources(ArrayList<FilmItems> data) {
        //nothing to do.
    }
}
