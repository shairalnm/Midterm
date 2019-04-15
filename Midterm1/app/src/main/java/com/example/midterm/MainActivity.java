package com.example.midterm;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetTrackAsync.TrackInfoData{
    public static String TRACK_KEY = "TRACK";
    ProgressBar progressBar;
    EditText searchBar;
    TrackAdapter adapter = null;
    ArrayList<Track> data = new ArrayList<>();
    SeekBar limitBar;
    TextView limitDisplayTextView;
    int numberOfResults;
    String urlStr;
    RadioButton radioButtonTrackRating,radioButtonArtistRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("MusixMatch Track Search");

        searchBar = findViewById(R.id.search_bar_editText);
        limitBar = findViewById(R.id.limit_seekBar);
        limitDisplayTextView = findViewById(R.id.limit_display_textView);
        radioButtonArtistRating =findViewById(R.id.radioButtonArtistRating);
        radioButtonTrackRating = findViewById(R.id.radioButtonTrackRating);
        numberOfResults = 10;

        limitBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numberOfResults = progress;
                limitDisplayTextView.setText("Limit: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove whitespace at beginning and end of searchbar entry
                searchBar.setText(searchBar.getText().toString().trim());

                if(isConnected() && searchBar.getText().length() > 0) {
                    RequestParams url;
                    String[] temp = searchBar.getText().toString().split(" ");
                    String searchTerm = temp[0];
                    for(int i = 1; i < temp.length; i++) {
                        searchTerm += "+" + temp[i];
                    }


                    Log.d("demo", "Value being sent to RequestParams as term: " + searchTerm);
                    if(radioButtonTrackRating.isChecked()) {
                        url = new RequestParams()
                                .addParameter("q_artist", searchTerm)
                                .addParameter("page_size", numberOfResults + "")
                                .addParameter("s_track_rating", "desc")
                                .addParameter("apikey","b596ecbc13b89492aac9c78b6a58af2f");
                        urlStr = url.getEncodedUrl("http://api.musixmatch.com/ws/1.1/track.search");
                    }
                    else if(radioButtonArtistRating.isChecked()){
                        url = new RequestParams()
                                .addParameter("q_artist", searchTerm)
                                .addParameter("page_size", numberOfResults + "")
                                .addParameter("s_artist_rating", "desc")
                                .addParameter("apikey","b596ecbc13b89492aac9c78b6a58af2f");
                         urlStr = url.getEncodedUrl("http://api.musixmatch.com/ws/1.1/track.search");
                    }

                   Log.d("demo","url string........"+urlStr);
                    //progressBar.setVisibility(View.VISIBLE);
                    new GetTrackAsync(MainActivity.this).execute(urlStr);


                } else if (searchBar.getText().length() <= 0) {
                    searchBar.setError("You must enter a keyword to search");
                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public void handleTrackData(ArrayList<Track> tracks) {
        ListView listView = findViewById(R.id.results_listView);
        data.clear();
        data.addAll(tracks);

        if(adapter == null) {
            //progressBar.setVisibility(View.INVISIBLE);
            adapter = new TrackAdapter(MainActivity.this, R.layout.track_item, data);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Track track = adapter.getItem(position);



                String url = track.track_share_url;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    @Override
    public void updateProgress(int progress) {

    }






    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
}
