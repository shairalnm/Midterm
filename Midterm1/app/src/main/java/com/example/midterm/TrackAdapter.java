package com.example.midterm;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TrackAdapter extends ArrayAdapter<Track> {

    public TrackAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Track> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Track track = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.track_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewTrackTitle = convertView.findViewById(R.id.track_title_textView);
            viewHolder.textViewTrackAlbum = convertView.findViewById(R.id.album_textView);
            viewHolder.textViewTrackArtist = convertView.findViewById(R.id.track_artist_textView);
            viewHolder.textViewTrackDate = convertView.findViewById(R.id.track_date_textView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textViewTrackTitle.setText("Track: " + track.track_name);
        viewHolder.textViewTrackAlbum.setText("Album: " + track.album_name);
        viewHolder.textViewTrackArtist.setText("Artist: " + track.artist_name);
        viewHolder.textViewTrackDate.setText("Date: " + track.updated_time.substring(5, 10) + "-" + track.updated_time.substring(0, 4));

        return convertView;
    }

    private static class ViewHolder{
        TextView textViewTrackTitle;
        TextView textViewTrackAlbum;
        TextView textViewTrackArtist;
        TextView textViewTrackDate;
    }
}
