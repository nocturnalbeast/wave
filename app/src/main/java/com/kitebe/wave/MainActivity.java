package com.kitebe.wave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.view.View.OnKeyListener;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView autoSuggestion;
    TextView weatherTextView, temp, humidity, wind, clouds;
    TextView songName;
    TextView coordTextView;
    ConstraintLayout backgroundImage;
    static ImageButton songTheme;
    static Button playButton;
    LocationManager locationManager;
    LocationListener locationListener;
    String address;
    ImageButton songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songList = findViewById(R.id.songList);
        songName = findViewById(R.id.songName);
        songTheme = findViewById(R.id.songTheme);

        songList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getSongList = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(getSongList);
            }
        });

        playButton = findViewById(R.id.playbutton);
        backgroundImage = findViewById(R.id.backgroundImage);
        weatherTextView = findViewById(R.id.weatherTextView);
        coordTextView = findViewById(R.id.coordTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        temp = findViewById(R.id.temp);
        // currentLocation = findViewById(R.id.currentLocation);
        humidity = findViewById(R.id.humidity);
        wind = findViewById(R.id.wind);
        clouds = findViewById(R.id.clouds);

        // suggestion stuff for places drop down
        List<String> responseList = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, responseList);
        autoSuggestion = findViewById(R.id.autocomplete_country);
        autoSuggestion.setAdapter(adapter);

        // load cities list from json, or atleast try to
        try {
            String stringLocation = loadJSONFromAsset("cities.json");
            Context context = getApplicationContext();
            Resources resources = context.getResources();
            JSONObject locationJson = new JSONObject(stringLocation);
            String locationName = locationJson.getString("cities");
            JSONArray locationNameArray = new JSONArray(locationName);
            for (int i = 0; i < locationNameArray.length(); i++) {
                JSONObject locationtNameArrayPart = locationNameArray.getJSONObject(i);
                String name = locationtNameArrayPart.getString("name");
                responseList.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        // handle location perms
        if (Build.VERSION.SDK_INT < 23) {
            startListening();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    updateLocationInfo(location);
                }
            }
        }

        autoSuggestion.setText(address, TextView.BufferType.EDITABLE);
        autoSuggestion.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                String query_result;
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        DownloadTask task = new DownloadTask();
                        task.execute("https://openweathermap.org/data/2.5/weather?q=" + autoSuggestion.getText().toString() + "&appid=b6907d289e10d714a6e88b30761fae22");
                        query_result = task.get();


                        //
                        // TODO: ADD HANDLER CODE HERE!
                        //

                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(autoSuggestion.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });


        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // TODO : add play call here
            }
        });
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void updateLocationInfo(Location location) {
        Log.i("LocationInfo", location.toString());
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String query_result;
        try {
            address = "Could not find address";
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAddresses != null && listAddresses.size() > 0) {
                Log.i("PlaceInfo", listAddresses.get(0).toString());
                address = "Address: \n";
                if (listAddresses.get(0).getSubLocality() != null) {
                    address = listAddresses.get(0).getSubLocality() + "\n";
                    Log.i("current location:", address);
                    try {
                        // TODO: add call to reset mpservice here!
                        DownloadTask task = new DownloadTask();
                        task.execute("https://openweathermap.org/data/2.5/weather?q=" + autoSuggestion.getText().toString() + "&appid=b6907d289e10d714a6e88b30761fae22");
                        query_result = task.get();


                        //
                        // TODO: ADD HANDLER CODE HERE!
                        //

                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(autoSuggestion.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.i("current location:", address);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // reading json from assets
    public String loadJSONFromAsset(String s) {
        String json = null;
        try {
            InputStream is = getAssets().open(s);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // TODO : add mediaplayer.stop
        locationManager.removeUpdates(locationListener);

    }

    protected void onStop() {
        super.onStop();
        // TODO : add mediaplayer.stop
        locationManager.removeUpdates(locationListener);

    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        super.onStop();
        // TODO : add mediaplayer.stop
        locationManager.removeUpdates(locationListener);

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Retrieving data for specified city");
            dialog.setMessage("Please wait.");
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
        }

    }

}


