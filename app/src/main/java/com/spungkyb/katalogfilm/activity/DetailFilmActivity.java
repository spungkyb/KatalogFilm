package com.spungkyb.katalogfilm.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spungkyb.katalogfilm.R;

public class DetailFilmActivity extends AppCompatActivity {
    ImageView imgViewPoster;
    TextView txtJudul;
    TextView txtDeskripsi;
    TextView txtTanggalRilis;
    RatingBar ratingBar;
    TextView txtTayangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        Bundle extraAdapter = getIntent().getExtras();
        imgViewPoster = (ImageView)findViewById(R.id.img_view_detail_film);
        txtJudul = (TextView)findViewById(R.id.txt_judul_detail_film);
        txtDeskripsi = (TextView)findViewById(R.id.txt_deskripsi_detail_film);
        txtTanggalRilis = (TextView)findViewById(R.id.txt_tanggal_rilis_detail_film);
        ratingBar=(RatingBar)findViewById(R.id.rating_detail_film);
        txtTayangan = (TextView)findViewById(R.id.txt_tayangan_detail_film);

        String title = extraAdapter.getString("EXTRA_TITLE");
        String overview = extraAdapter.getString("EXTRA_OVERVIEW");
        String releaseDate = extraAdapter.getString("EXTRA_RELEASE_DATE");
        String posterUrl = extraAdapter.getString("EXTRA_POSTER_URL");
        double vote = extraAdapter.getDouble("EXTRA_VOTE",0.0);
        boolean tayangan = extraAdapter.getBoolean("EXTRA_TAYANGAN",false);
        String tayang;
        if (tayangan==true){
            tayang = "Dewasa";
        }
        else{
            tayang ="Bimbingan Orangtua";
        }

        String ratingString = String.valueOf(vote);
        Float ratingFloat = Float.parseFloat(ratingString);
        ratingBar.setRating(ratingFloat);
        ratingBar.setStepSize(0.1f);
        txtJudul.setText(title);
        txtDeskripsi.setText(overview);
        txtTanggalRilis.setText("Tanggal Rilis "+releaseDate);
        txtTayangan.setText("Tayangan "+tayang);
        Glide.with(this).load(posterUrl).into(imgViewPoster);

    }
}
