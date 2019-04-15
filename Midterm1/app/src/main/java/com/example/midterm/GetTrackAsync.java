package com.example.midterm;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

public class GetTrackAsync extends AsyncTask<String, Integer, ArrayList<Track>> {
    TrackInfoData trackInfoData;
    ArrayList<Track> tracks = new ArrayList<>();
    MainActivity activity;

    public GetTrackAsync(TrackInfoData trackInfoData) {
        this.trackInfoData = trackInfoData;
    }

    @Override
    protected void onPreExecute() {
//        super.onPreExecute();
       activity.progressBar.setVisibility(View.VISIBLE);
    }

    @Override

    protected ArrayList<Track> doInBackground(String... strings) {
        HttpURLConnection connection = null;

        Log.d("demo","url in do in "+strings[0]);

        try {
            URL url = new URL(strings[0]);
            Log.d("demo","url in do in try"+strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                publishProgress(1);
                Log.d("demo","in if condition"+strings[0]);
                tracks = new ArrayList<>();

                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                Log.d("demo","json.............."+json);
                JSONObject root = new JSONObject(json);
                Log.d("demo","root "+root.toString());
                JSONObject message= new JSONObject(json);
                message= root.getJSONObject("message");
                Log.d("demo","message: "+message.toString());
                JSONObject body= new JSONObject(json);
                body = message.getJSONObject("body");
                Log.d("demo","body: "+body.toString());

                JSONArray tracksArray = body.getJSONArray("track_list");
                Log.d("demo","tracksArray: "+tracksArray.toString());
                for(int i = 0; i < tracksArray.length(); i++) {
                    JSONObject musicJson = tracksArray.getJSONObject(i);
                    Log.d("demo","track: "+musicJson.toString());
                    JSONObject trackJson = musicJson.getJSONObject("track");
                    Log.d("demo","track: "+trackJson.toString());
                    Track track = new Track();
                    //track_name,album_name,artist_name,updated_time,track_share_url
                    track.track_name = trackJson.getString("track_name");
                    track.album_name = trackJson.getString("album_name");
                    track.artist_name = trackJson.getString("artist_name");
                    track.updated_time = trackJson.getString("updated_time");
                    track.track_share_url = trackJson.getString("track_share_url");
                    Log.d("demo","track.........: "+track.toString());

                    tracks.add(track);
                }
            }

        } catch (SocketTimeoutException e) {
            publishProgress(-1);
            e.printStackTrace();
        } catch (MalformedURLException e) {
            //Log.d("demo", "ERORR1: " + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            //Log.d("demo", "ERORR2: " + e.toString());
            e.printStackTrace();
        } catch (JSONException e) {
            //Log.d("demo", "ERORR2: " + e.toString());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return tracks;
    }
    @Override
    protected void onPostExecute(ArrayList<Track> tracks) {

        Log.d("demo", "onPostExecute: " + tracks.size());

        trackInfoData.handleTrackData(tracks);
        activity.progressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    protected void onProgressUpdate(Integer... values)
    {
        trackInfoData.updateProgress(values[0]);
    }

    public static interface TrackInfoData {
        public void handleTrackData(ArrayList<Track> tracks);
        public void updateProgress(int progress);
    }
}
