package com.spungkyb.katalogfilm.activity;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.spungkyb.katalogfilm.FilmAdapter;
import com.spungkyb.katalogfilm.FilmItems;
import com.spungkyb.katalogfilm.MyAsynctackLoader;
import com.spungkyb.katalogfilm.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<FilmItems>> {
    ListView listView;
    FilmAdapter adapter;
    static final String EXTRAS_FILM = "EXTRAS_FILM";
    TextView txtPencarian;
    TextView txtTidakAdaHasil;
    ProgressDialog mProgress;

    private ArrayList<FilmItems> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        adapter = new FilmAdapter(this);
        adapter.notifyDataSetChanged();
        listView = (ListView) findViewById(R.id.list_view);
        txtPencarian = (TextView) findViewById(R.id.txt_pencarian);
        txtTidakAdaHasil = (TextView)findViewById(R.id.txt_tidak_ada_hasil);
        mProgress = new ProgressDialog(this);
        listView.setAdapter(adapter);
        //jika list view null
        listView.setEmptyView(findViewById(R.id.txt_tidak_ada_hasil));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setQueryHint("Cari Film dengan Judul");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("change", "" + newText);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                mProgress.setTitle("Tunggu");
                mProgress.setMessage("Sedang mencari "+query+" . . .");
                mProgress.setCancelable(false);
                mProgress.show();
                //mProgress.setCancelable(false);
                Log.e("submit", "" + query);
                String judul = query;
                query = query.replaceAll(" ", "%20");
                Bundle bundle = new Bundle();
                bundle.putString(EXTRAS_FILM, query);
                getLoaderManager().restartLoader(0, bundle, SearchActivity.this);
                txtPencarian.setText("Pencarian ''" + judul + "''");

                return false;
            }

        });

        return true;
    }


    @Override
    public Loader<ArrayList<FilmItems>> onCreateLoader(int i, Bundle bundle) {
        String kumpulanFilm = "";
        if (bundle != null) {
            kumpulanFilm = bundle.getString(EXTRAS_FILM);
        }
        return new MyAsynctackLoader(this, kumpulanFilm);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<FilmItems>> loader, ArrayList<FilmItems> filmItems) {
        adapter.setData(filmItems);
        mProgress.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<FilmItems>> loader) {
        adapter.setData(null);
    }
}
