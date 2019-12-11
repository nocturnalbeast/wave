package com.kitebe.wave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText cityName,currentLocation;
    TextView weatherTextView,temp,humidty,wind;
    TextView coordTextView;
    AudioManager audioManager;
    MediaPlayer mediaPlayerRain;
    ImageView backgroungImage ;
    String jsonMain;
    Button getWeatherButton,equilizer;

    //song controller
    ImageButton playSong;
    boolean isPlaying = false;

    boolean loop;
    //current location
    LocationManager locationManager;

    LocationListener locationListener;
    String addresss;
    ImageButton songList;
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

//
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            addresss = "Could not find address";

            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddresses != null && listAddresses.size() > 0 ) {

                Log.i("PlaceInfo", listAddresses.get(0).toString());

                addresss = "Address: \n";



                if (listAddresses.get(0).getSubLocality() != null) {

                    addresss = listAddresses.get(0).getSubLocality() + "\n";
                    Log.i("current location:",addresss);
                    try {
//                        mediaPlayerRain.reset();
                        DownloadTask task = new DownloadTask();

//                        String encodedCityName = URLEncoder.encode(addresss.toString(), "UTF-8");

                        task.execute("https://openweathermap.org/data/2.5/weather?q=" + addresss + "&appid=b6907d289e10d714a6e88b30761fae22");

//                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    } catch(Exception e){

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                Toast t = Toast.makeText(MainActivity.this,"Input error :",Toast.LENGTH_SHORT);
                                t.show();
                            }
                        });
                        e.printStackTrace();
                    }
                }

            }

            //TextView addressTextView = (TextView) findViewById(R.id.address);

           // addressTextView.setText(addresss);

            currentLocation.setText(addresss, TextView.BufferType.EDITABLE);
            Log.i("current location:",addresss);




        } catch (IOException e) {

            e.printStackTrace();

        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }





    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in= urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data =reader.read();


                while ( data !=-1){

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
            super.onPostExecute(s);

            try {


                JSONObject jsonWeatherObject = new JSONObject(s);
                //{"coord":{"lon":76.25,"lat":9.96},"weather":[{"id":721,"main":"Haze","description":"haze","icon":"50d"}],"base":"stations","main":{"temp":31,"pressure":1011,"humidity":66,"temp_min":31,"temp_max":31},"visibility":4000,"wind":{"speed":1.5,"deg":210},"clouds":{"all":96},"dt":1573623100,"sys":{"type":1,"id":9211,"country":"IN","sunrise":1573606121,"sunset":1573648193},"timezone":19800,"id":1273874,"name":"Kochi","cod":200}

                String weatherInfo = jsonWeatherObject.getString("weather");
                Log.i("weather content", weatherInfo);

                JSONArray weatherInfoArray = new JSONArray(weatherInfo);
                String weatherInformation = "";

                //weatherInformation

                for (int i=0; i <weatherInfoArray.length(); i++){

                    JSONObject jsonWeatherPart = weatherInfoArray.getJSONObject(i);

                    jsonMain = jsonWeatherPart.getString("main");

                    String description =jsonWeatherPart.getString("description");

                    if (!jsonMain.equals("") && !description.equals("")){

                        weatherInformation += jsonMain + ":" + description +"\r\n";
                    }

//                    if(main.equals("Haze")){
//                   }
                }
                if(!weatherInformation.equals("")) {
                    weatherTextView.setText(weatherInformation);
                }else {
                    Toast.makeText(getApplicationContext(),"could not find weather Information :",Toast.LENGTH_LONG).show();
                }

                String string = loadJSONFromAsset();
                Context context = getApplicationContext();

                Resources resources = context.getResources();
                JSONObject weatherBackgroundContent = new JSONObject(string);

                String weatherBackgroundInfo = weatherBackgroundContent.getString("weather");
                Log.i("weather content", weatherBackgroundInfo);

                JSONArray weatherBackgroundContentArray = new JSONArray(weatherBackgroundInfo);
                String weatherBackgroundInformation = "";

                //weatherInformation

                for (int i=0; i <weatherBackgroundContentArray.length(); i++){

                    JSONObject jsonWeatherBackgroundContentPart = weatherBackgroundContentArray.getJSONObject(i);

                    String name = jsonWeatherBackgroundContentPart.getString("name");

                    if(jsonMain.equals(name)  ){

                        String  song =jsonWeatherBackgroundContentPart.getString("song");

                        // get image name from JSON
                        String image =jsonWeatherBackgroundContentPart.getString("image");

                        // get resource id by image name
                        final int resourceImageId = resources.getIdentifier(image, "drawable", context.getPackageName());

                        // get resource id by song name
                        final int resourceSongId = resources.getIdentifier(song, "raw", context.getPackageName());

                        // get drawable by resourceImageid
                        Drawable drawable = resources.getDrawable(resourceImageId);
                        Toast.makeText(getApplicationContext(),song,Toast.LENGTH_SHORT).show();

                        // backgroungImage.setImageResource(R.drawable.rain);
                        backgroungImage.setImageDrawable(drawable);
                        mediaPlayerRain = MediaPlayer.create(getApplicationContext(),resourceSongId);
                        mediaPlayerRain.start();

                    }

                    if (!name.equals("") ){

                        weatherBackgroundInformation += name+"\r\n";
                        Log.i("weather information",weatherBackgroundInformation);
                    }


                }

            }catch (Exception e){
                Log.i("withObject","hi");
                e.printStackTrace();
            }

            //coord information

            try {
                JSONObject jsonCoordObject = new JSONObject(s);
                //{"coord":{"lon":76.25,"lat":9.96},"weather":[{"id":721,"main":"Haze","description":"haze","icon":"50d"}],"base":"stations","main":{"temp":31,"pressure":1011,"humidity":66,"temp_min":31,"temp_max":31},"visibility":4000,"wind":{"speed":1.5,"deg":210},"clouds":{"all":96},"dt":1573623100,"sys":{"type":1,"id":9211,"country":"IN","sunrise":1573606121,"sunset":1573648193},"timezone":19800,"id":1273874,"name":"Kochi","cod":200}

                String coordInfo = jsonCoordObject.getString("main");
                Log.i("coord content", coordInfo);

//                JSONArray coordInfoArray = new JSONArray(coordInfo);

                JSONObject coordObject= new JSONObject(coordInfo);
                String coordTempInformation = coordObject.getString("temp");
                String coordPressureInformation = coordObject.getString("pressure");
                String coordHumidityInformation = coordObject.getString("humidity");


                if(!coordTempInformation.equals("")) {
                    coordTextView.setText("temp:"+coordTempInformation+"\u2103"+"\rpressure:"+coordPressureInformation+""+"\nHumidity:"+coordHumidityInformation);
                    temp.setText(coordTempInformation+"\u2103");
                    humidty.setText(coordHumidityInformation+"%");

                }else {
                    Toast.makeText(getApplicationContext(),"could not find coord Information :",Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"could not load the coord information :",Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }

            try {
                JSONObject jsonWindObject = new JSONObject(s);
                //{"coord":{"lon":76.25,"lat":9.96},"weather":[{"id":721,"main":"Haze","description":"haze","icon":"50d"}],"base":"stations","main":{"temp":31,"pressure":1011,"humidity":66,"temp_min":31,"temp_max":31},"visibility":4000,"wind":{"speed":1.5,"deg":210},"clouds":{"all":96},"dt":1573623100,"sys":{"type":1,"id":9211,"country":"IN","sunrise":1573606121,"sunset":1573648193},"timezone":19800,"id":1273874,"name":"Kochi","cod":200}

                String coordInfo = jsonWindObject.getString("wind");
                Log.i("coord content", coordInfo);

//                JSONArray coordInfoArray = new JSONArray(coordInfo);

                JSONObject coordObject= new JSONObject(coordInfo);
                String coordWindInformation = coordObject.getString("speed");


                if(!coordWindInformation.equals("")) {

                    double kms = Double.parseDouble(coordWindInformation)* 3.6;

                    wind.setText(kms+"Km/h");


                }else {
                    Toast.makeText(getApplicationContext(),"could not find coord Information :",Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"could not load the coord information :",Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        }



    }


    //reading json from assets
    public String loadJSONFromAsset() {
        Log.i("infunction","heloo");
        String json = null;

        try {
            Log.i("try","hi");
            InputStream is = getAssets().open("songs.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
            Log.d("intry","heloo");
//            Toast.makeText(this, "Json"+json, Toast.LENGTH_SHORT).show();


        } catch (IOException ex) {
            ex.printStackTrace();
            Log.i("incatch","heloo");
            return null;
        }
        Log.i("jsonfile",json);
        Log.i("beforeReturn","heloo");

        return json;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songList = findViewById(R.id.songList);

        songList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getSongList = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(getSongList);
            }
        });


        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        playSong = findViewById(R.id.playPause);


        getWeatherButton=findViewById(R.id.getWeatherButton);
        backgroungImage = findViewById(R.id.backgroundImage);
       // equilizer=findViewById(R.id.equilizer);
        // hazeImage = findViewById(R.id.hazeImage);

        cityName = findViewById(R.id.cityName);
        weatherTextView = findViewById(R.id.weatherTextView);
        coordTextView = findViewById(R.id. coordTextView);

        //current location
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        temp = findViewById(R.id.temp);
        currentLocation = findViewById(R.id.currentLocation);
        humidty = findViewById(R.id.humidity);
        wind = findViewById(R.id.wind);






        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

//                updateLocationInfo(location);

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

        if (Build.VERSION.SDK_INT < 23) {

            startListening();

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {

                    updateLocationInfo(location);

                }

            }

        }

        cityName.setText(addresss, TextView.BufferType.EDITABLE);

        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    DownloadTask task = new DownloadTask();


//                    String encodedCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
                    //http://openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22

                    task.execute("https://openweathermap.org/data/2.5/weather?q=" + cityName.getText().toString() + "&appid=b6907d289e10d714a6e88b30761fae22");

                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);
                } catch(Exception e){

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            Toast t = Toast.makeText(MainActivity.this,"Input error :",Toast.LENGTH_SHORT);
                            t.show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });

        playSong.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mediaPlayerRain.isPlaying()) {
                    mediaPlayerRain.pause();
                    playSong.setImageResource(R.drawable.playbutton);


                }else{
                    mediaPlayerRain.start();
                    playSong.setImageResource(R.drawable.playbutton);
                }
                isPlaying = !isPlaying;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mediaPlayerRain.stop();


        locationManager.removeUpdates(locationListener);

    }

    protected void onStop() {
        super.onStop();

        mediaPlayerRain.stop();
        locationManager.removeUpdates(locationListener);

    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        mediaPlayerRain.stop();
        locationManager.removeUpdates(locationListener);

    }
}

