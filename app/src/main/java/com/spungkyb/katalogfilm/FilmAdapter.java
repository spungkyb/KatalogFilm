package com.spungkyb.katalogfilm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spungkyb.katalogfilm.activity.DetailFilmActivity;
import com.spungkyb.katalogfilm.activity.MainActivity;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 19/06/2018.
 */

public class FilmAdapter extends BaseAdapter {
    private ArrayList<FilmItems> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    public FilmAdapter(Context context) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<FilmItems> items) {
        mData = items;
        notifyDataSetChanged();
    }

    public void addItem(final FilmItems item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    @Override
    public FilmItems getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.film_item, null);
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.txt_judul_film_item);
            holder.textViewOverview = (TextView) convertView.findViewById(R.id.txt_deskripsi_film_item);
            holder.textViewReleaseDate = (TextView) convertView.findViewById(R.id.txt_release_film_item);
            holder.imgViewPoster = (ImageView)convertView.findViewById(R.id.img_view_film_item);
            holder.listViewFilmItem = (LinearLayout)convertView.findViewById(R.id.linear_layout_film_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String posterUrl = "http://image.tmdb.org/t/p/w185";
        holder.textViewTitle.setText(mData.get(position).getTitle());
        holder.textViewOverview.setText(mData.get(position).getOverview());

        String dateStr = null;
        if (mData.get(position).getReleaseDate().toString().equals("")){
            dateStr="-";
        }
        else{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
            try {
                date = dateFormat.parse(mData.get(position).getReleaseDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); //If you need time just put specific format for time like 'HH:mm:ss'
            dateStr = formatter.format(date);
        }

        //holder.textViewReleaseDate.setText(mData.get(position).getReleaseDate());
        holder.textViewReleaseDate.setText(dateStr);

        Glide.with(context).load(posterUrl+mData.get(position).getPosterPath()).into(holder.imgViewPoster);
        holder.listViewFilmItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mData.get(position).getTitle().toString();
                String overview = mData.get(position).getOverview().toString();
                String posterPath = mData.get(position).getPosterPath().toString();
                String posterUrl =  "http://image.tmdb.org/t/p/w185"+posterPath;

                String dateStr = null;
                if (mData.get(position).getReleaseDate().toString().equals("")){
                    dateStr="-";
                }
                else{
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
                    try {
                        date = dateFormat.parse(mData.get(position).getReleaseDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); //If you need time just put specific format for time like 'HH:mm:ss'
                    dateStr = formatter.format(date);
                }

                double vote = mData.get(position).getVote();
                boolean tayangan= mData.get(position).isAdult();
                Intent intent = new Intent(view.getContext(), DetailFilmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("EXTRA_TITLE",title);
                bundle.putString("EXTRA_OVERVIEW",overview);
                bundle.putString("EXTRA_RELEASE_DATE",dateStr);
                bundle.putString("EXTRA_POSTER_URL",posterUrl);
                bundle.putDouble("EXTRA_VOTE",vote);
                bundle.putBoolean("EXTRA_TAYANGAN",tayangan);
                //Log.e("title : "+title,"poster : "+posterUrl+"vote : "+vote+"tayangan : "+tayangan);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView textViewTitle;
        TextView textViewOverview;
        TextView textViewReleaseDate;
        ImageView imgViewPoster;
        LinearLayout listViewFilmItem;
    }
}
