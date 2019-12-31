package com.kitebe.wave;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, String> {


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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

            for (int i = 0; i < weatherInfoArray.length(); i++) {

                JSONObject jsonWeatherPart = weatherInfoArray.getJSONObject(i);

                jsonMain = jsonWeatherPart.getString("main");

                String description = jsonWeatherPart.getString("description");

                if (!jsonMain.equals("") && !description.equals("")) {

                    weatherInformation += jsonMain + ":" + description + "\r\n";
                }

//                    if(main.equals("Haze")){
//                   }
            }
            if (!weatherInformation.equals("")) {
                //    weatherTextView.setText(weatherInformation);
            } else {
                Toast.makeText(getApplicationContext(), "could not find weather Information :", Toast.LENGTH_LONG).show();
            }

            String string = loadJSONFromAsset("songs.json");
            String stringPL = loadJSONFromAsset("progress.json");
            Context context = getApplicationContext();

            Resources resources = context.getResources();
            JSONObject weatherBackgroundContent = new JSONObject(string);

            //progress equalizer json
            JSONObject progressJson = new JSONObject(stringPL);

            String weatherBackgroundInfo = weatherBackgroundContent.getString("weather");
            Log.i("weather content", weatherBackgroundInfo);

            //progress equalizer json
            String playlistName = progressJson.getString("playlistjsonfile");
            Log.i("progress content", playlistName);

            JSONArray weatherBackgroundContentArray = new JSONArray(weatherBackgroundInfo);
            String weatherBackgroundInformation = "";

            //progress equalizer json
            JSONArray playlistNameArray = new JSONArray(playlistName);


            //weatherInformation

            for (int i = 0; i < weatherBackgroundContentArray.length(); i++) {

                JSONObject jsonWeatherBackgroundContentPart = weatherBackgroundContentArray.getJSONObject(i);

                weatherName = jsonWeatherBackgroundContentPart.getString("name");

                if (jsonMain.equals(weatherName)) {

                    song1 = jsonWeatherBackgroundContentPart.getString("song1");
                    song2 = jsonWeatherBackgroundContentPart.getString("song2");
                    song3 = jsonWeatherBackgroundContentPart.getString("song3");
                    song4 = jsonWeatherBackgroundContentPart.getString("song4");
                    song5 = jsonWeatherBackgroundContentPart.getString("song5");
                    plname = jsonWeatherBackgroundContentPart.getString("playlistname");

                    // get image name from JSON
                    String image = jsonWeatherBackgroundContentPart.getString("image");

                    // get resource id by image name
                    final int resourceImageId = resources.getIdentifier(image, "drawable", context.getPackageName());

                    // get resource id by song song


                    // get drawable by resourceImageid
                    Drawable drawable = resources.getDrawable(resourceImageId);
                    Toast.makeText(getApplicationContext(), song1, Toast.LENGTH_SHORT).show();

                    // backgroungImage.setImageResource(R.drawable.rain);
                    backgroungImage.setBackgroundResource(resourceImageId);

                }

                if (!weatherName.equals("")) {

                    weatherBackgroundInformation += weatherName + "\r\n";
                    Log.i("weather information", weatherBackgroundInformation);
                }

            }

            ////progress equalizer json

            for (int i = 0; i < playlistNameArray.length(); i++) {

                JSONObject playlistNameArrayPart = playlistNameArray.getJSONObject(i);

                String name = playlistNameArrayPart.getString("name");

                if (plname.equals(name)) {


                    songName.setText(plname);
                    allprogress1 = playlistNameArrayPart.getInt("progress1");
                    allprogress2 = playlistNameArrayPart.getInt("progress2");
                    allprogress3 = playlistNameArrayPart.getInt("progress3");
                    allprogress4 = playlistNameArrayPart.getInt("progress4");
                    allprogress5 = playlistNameArrayPart.getInt("progress5");
                    Log.i("progressjson", "info");

                    float log1 = (float) (Math.log(100 - (allprogress1 - 1)) / Math.log(100));
                    float log2 = (float) (Math.log(100 - (allprogress2 - 1)) / Math.log(100));
                    float log3 = (float) (Math.log(100 - (allprogress3 - 1)) / Math.log(100));
                    float log4 = (float) (Math.log(100 - (allprogress4 - 1)) / Math.log(100));
                    float log5 = (float) (Math.log(100 - (allprogress5 - 1)) / Math.log(100));

                    playbutton.setBackgroundResource(R.drawable.pausebutton);


                    // get resource id by song song
                    final int resourceSongId1 = resources.getIdentifier(song1, "raw", context.getPackageName());
                    final int resourceSongId2 = resources.getIdentifier(song2, "raw", context.getPackageName());
                    final int resourceSongId3 = resources.getIdentifier(song3, "raw", context.getPackageName());
                    final int resourceSongId4 = resources.getIdentifier(song4, "raw", context.getPackageName());
                    final int resourceSongId5 = resources.getIdentifier(song5, "raw", context.getPackageName());

                    Toast.makeText(getApplicationContext(), allprogress1 + "hello", Toast.LENGTH_SHORT).show();


                    mediaPlayerRain = MediaPlayer.create(getApplicationContext(), resourceSongId1);
                    mediaPlayerRain.start();
                    mediaPlayerRain.setVolume(1 - log1, 1 - log1);

                    mediaPlayerRain2 = MediaPlayer.create(getApplicationContext(), resourceSongId2);
                    mediaPlayerRain2.start();
                    mediaPlayerRain2.setVolume(1 - log2, 1 - log2);


                    mediaPlayerRain3 = MediaPlayer.create(getApplicationContext(), resourceSongId3);
                    mediaPlayerRain3.start();
                    mediaPlayerRain3.setVolume(1 - log3, 1 - log3);

                    mediaPlayerRain4 = MediaPlayer.create(getApplicationContext(), resourceSongId4);
                    mediaPlayerRain4.start();
                    mediaPlayerRain4.setVolume(1 - log4, 1 - log4);

                    mediaPlayerRain5 = MediaPlayer.create(getApplicationContext(), resourceSongId5);
                    mediaPlayerRain5.start();
                    mediaPlayerRain5.setVolume(1 - log5, 1 - log5);


                }

            }

        } catch (Exception e) {
            Log.i("withObject", "hi");
            e.printStackTrace();
        }

        //coord information

        try {
            JSONObject jsonCoordObject = new JSONObject(s);
            //{"coord":{"lon":76.25,"lat":9.96},"weather":[{"id":721,"main":"Haze","description":"haze","icon":"50d"}],"base":"stations","main":{"temp":31,"pressure":1011,"humidity":66,"temp_min":31,"temp_max":31},"visibility":4000,"wind":{"speed":1.5,"deg":210},"clouds":{"all":96},"dt":1573623100,"sys":{"type":1,"id":9211,"country":"IN","sunrise":1573606121,"sunset":1573648193},"timezone":19800,"id":1273874,"name":"Kochi","cod":200}

            String coordInfo = jsonCoordObject.getString("main");
            Log.i("coord content", coordInfo);

//                JSONArray coordInfoArray = new JSONArray(coordInfo);

            JSONObject coordObject = new JSONObject(coordInfo);
            coordTempInformation = coordObject.getString("temp");
            String coordPressureInformation = coordObject.getString("pressure");
            String coordHumidityInformation = coordObject.getString("humidity");
            int tempInt = Integer.parseInt(String.valueOf(coordTempInformation));
            String tempString = String.valueOf(tempInt);
            Log.i("tempInt", String.valueOf(tempInt));


            if (!coordTempInformation.equals("")) {
                // coordTextView.setText("temp:"+coordTempInformation+"\u2103"+"\rpressure:"+coordPressureInformation+""+"\nHumidity:"+coordHumidityInformation);
                temp.setText(coordTempInformation);
//                    Typeface typeface = getResources().getFont(R.font.robotoslabthin);
//                    temp.setTypeface(typeface);

                humidty.setText(coordHumidityInformation + "%");

            } else {
                Toast.makeText(getApplicationContext(), "could not find coord Information :", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "could not load the coord information :", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }

        try {
            JSONObject jsonWindObject = new JSONObject(s);
            //{"coord":{"lon":76.25,"lat":9.96},"weather":[{"id":721,"main":"Haze","description":"haze","icon":"50d"}],"base":"stations","main":{"temp":31,"pressure":1011,"humidity":66,"temp_min":31,"temp_max":31},"visibility":4000,"wind":{"speed":1.5,"deg":210},"clouds":{"all":96},"dt":1573623100,"sys":{"type":1,"id":9211,"country":"IN","sunrise":1573606121,"sunset":1573648193},"timezone":19800,"id":1273874,"name":"Kochi","cod":200}

            String coordInfo = jsonWindObject.getString("wind");
            Log.i("coord content", coordInfo);

//                JSONArray coordInfoArray = new JSONArray(coordInfo);

            JSONObject coordObject = new JSONObject(coordInfo);
            String coordWindInformation = coordObject.getString("speed");


            if (!coordWindInformation.equals("")) {

                double kms = Double.parseDouble(coordWindInformation) * 3.6;

                String test = String.format("%.02f", kms);


                wind.setText(test + "Km/h");


            } else {
                Toast.makeText(getApplicationContext(), "could not find coord Information :", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "could not load the coord information :", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }

        try {
            JSONObject jsonCloudObject = new JSONObject(s);
            //{"coord":{"lon":76.25,"lat":9.96},"weather":[{"id":721,"main":"Haze","description":"haze","icon":"50d"}],"base":"stations","main":{"temp":31,"pressure":1011,"humidity":66,"temp_min":31,"temp_max":31},"visibility":4000,"wind":{"speed":1.5,"deg":210},"clouds":{"all":96},"dt":1573623100,"sys":{"type":1,"id":9211,"country":"IN","sunrise":1573606121,"sunset":1573648193},"timezone":19800,"id":1273874,"name":"Kochi","cod":200}

            String coordInfo = jsonCloudObject.getString("clouds");
            Log.i("coord content", coordInfo);

//                JSONArray coordInfoArray = new JSONArray(coordInfo);

            JSONObject coordObject = new JSONObject(coordInfo);
            String coordCloudInformation = coordObject.getString("all");


            if (!coordCloudInformation.equals("")) {

                clouds.setText(coordCloudInformation + "%");


            } else {
                Toast.makeText(getApplicationContext(), "could not find coord Information :", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "could not load the coord information :", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }

}