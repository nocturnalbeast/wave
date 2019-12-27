package com.kitebe.wave;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

import static com.kitebe.wave.MainActivity.allprogress1;
import static com.kitebe.wave.MainActivity.allprogress2;
import static com.kitebe.wave.MainActivity.allprogress3;
import static com.kitebe.wave.MainActivity.allprogress4;
import static com.kitebe.wave.MainActivity.allprogress5;
import static com.kitebe.wave.MainActivity.isPlayingBoolean;
import static com.kitebe.wave.MainActivity.mediaPlayerRain;
import static com.kitebe.wave.MainActivity.mediaPlayerRain2;
import static com.kitebe.wave.MainActivity.mediaPlayerRain3;
import static com.kitebe.wave.MainActivity.mediaPlayerRain4;
import static com.kitebe.wave.MainActivity.mediaPlayerRain5;


public class SecondActivity extends Activity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter adapter;
    SharedPreferences sharedPreferences;
    int noteId=-1;
    TextView songName;
    static Button playButton;
    static int imageId;
   int newprogress1,newprogress2,newprogress3,newprogress4,newprogress5;
   Button songList;
   static ImageButton songTheme2;
   


    //MediaPlayer mediaPlayer1,mediaPlayer2,mediaPlayer3,mediaPlayer4,mediaPlayer5;
    static SeekBar volumeSeekBar1,volumeSeekBar2,volumeSeekBar3,volumeSeekBar4,volumeSeekBar5;
    float log1,log2,log3,log4,log5, newvolume,newvolume2,newvolume3,newvolume4,newvolume5;
    int progress1,progress2,progress3,progress4,progress5;
    //saveMusic button
    Button saveMusic1,saveMusic2,saveMusic3,saveMusic4,saveMusic5,saveAll;
   static Button playList1,playList2,playList3,playList4,playList5,playList6,playList7,playList8,playList9,playList10,playList11,playList12;
    boolean saveOption = false,saveOption2 = false,saveOption3 = false,saveOption4 = false,saveOption5 = false, sharedPreferenceValueBoolean=false;
    float currentVolume,currentVolume2,currentVolume3,currentVolume4,currentVolume5;
    //SharedPreferences sharedpreferencesexit;
    int getSharedPreferenceValue,getSharedPreferenceValue2,getSharedPreferenceValue3,getSharedPreferenceValue4,getSharedPreferenceValue5;
    ImageView birdImage,thunderImage,rainImage,riverImage,leafImage;
    ImageButton backbutton;

    //    AudioManager audioManager1,audioManager2,audioManager3;
    int min1 = 0, max1 = 10, current1 = 5,
            min2 = 0, max2 = 10, current2 = 5,
            min3 = 0, max3 = 10, current3 = 5;

    public static final String MyPREFERENCES = "MyPrefs" ;





    //reading json from assets
    public String loadJSONFromAsset(String s) {
        Log.i("infunction","heloo");
        String json = null;

        try {
            Log.i("try","hi");
            InputStream is = getAssets().open(s);

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        MainActivity.intentBoolean=true;

//        mediaPlayer1 = MediaPlayer.create(this, R.raw.song1);
//
//        mediaPlayer2 = MediaPlayer.create(this, R.raw.song2);
////        mediaPlayer2.start();
//        mediaPlayer3 = MediaPlayer.create(this, R.raw.song3);
////        mediaPlayer3.start();
//        mediaPlayer4 = MediaPlayer.create(this, R.raw.song4);
//        mediaPlayer5 = MediaPlayer.create(this, R.raw.song5);


        volumeSeekBar1 = findViewById(R.id.volumeSeekBar1);
//        mediaPlayer1.setVolume(3,3);
//        volumeSeekBar1.setProgress(5);

        volumeSeekBar2 = findViewById(R.id.volumeSeekBar2);
//       volumeSeekBar2.setProgress(max2 - min2);
//       volumeSeekBar2.setProgress(current2 - min2);

        volumeSeekBar3 = findViewById(R.id.volumeSeekBar3);
//        volumeSeekBar3.setProgress(max3 - min3);
        volumeSeekBar4 = findViewById(R.id.volumeSeekBar4);
        volumeSeekBar5 = findViewById(R.id.volumeSeekBar5);

        songList = findViewById(R.id.songList);
        songList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


      /*  try {
            if(isPlayingBoolean){
                playButton.setBackgroundResource(R.drawable.pausebutton);
            }else {
                playButton.setBackgroundResource(R.drawable.playbutton);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
*/
        sharedPreferences = getApplicationContext()
                .getSharedPreferences("com.kitebe.wave", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if (set == null) {
            notes.add("add new note");
        }else {
            notes = new ArrayList(set);
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);



        //saveMusic button
//        saveMusic1 = findViewById(R.id.saveMusic1);
//        saveMusic2 = findViewById(R.id.saveMusic2);
//        saveMusic3 = findViewById(R.id.saveMusic3);
//        saveMusic4 = findViewById(R.id.saveMusic4);
//        saveMusic5 = findViewById(R.id.saveMusic5);
//        saveAll = findViewById(R.id.saveAll);

        //playlist
        playList1 = findViewById(R.id.playList1);
        playList2 = findViewById(R.id.playList2);
        playList3 = findViewById(R.id.playList3);
        playList4 = findViewById(R.id.playList4);
        playList5 = findViewById(R.id.playList5);
        playList6 = findViewById(R.id.playList6);
        playList7 = findViewById(R.id.playList7);
        playList8 = findViewById(R.id.playList8);
        playList9 = findViewById(R.id.playList9);
        playList10 = findViewById(R.id.playList10);
        playList11= findViewById(R.id.playList11);
        playList12= findViewById(R.id.playList12);
        songName = findViewById(R.id.songName);
        playButton = findViewById(R.id.playButton);



        //images
        birdImage = findViewById(R.id.birdImage);
        thunderImage = findViewById(R.id.thunderImage);
        rainImage = findViewById(R.id.rainImage);
        riverImage = findViewById(R.id.riverImage);
        leafImage = findViewById(R.id.leafImage);
        songTheme2 = findViewById(R.id.songTheme);
        songTheme2.setBackgroundResource(imageId);

//        try {
//            sharedpreferencesexit = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//
//            getSharedPreferenceValue = sharedpreferencesexit.getInt("currentProgress", 10);
//
//            Log.i("channel", String.valueOf(getSharedPreferenceValue));
//        }catch (Exception e){
//            Log.d("Error",e.toString());
//        }
//        try {
//
//            getSharedPreferenceValue2 = sharedpreferencesexit.getInt("currentProgress2", 10);
//
//            Log.i("channel2", String.valueOf(getSharedPreferenceValue2));
//        }catch (Exception e){
//            Log.d("Error2",e.toString());
//        }
//        try {
//
//            getSharedPreferenceValue3 = sharedpreferencesexit.getInt("currentProgress3", 10);
//
//            Log.i("channel3", String.valueOf(getSharedPreferenceValue3));
//        }catch (Exception e){
//            Log.d("Error3",e.toString());
//        }
//        try {
//
//            getSharedPreferenceValue4 = sharedpreferencesexit.getInt("currentProgress4", 10);
//
//            Log.i("channel4", String.valueOf(getSharedPreferenceValue4));
//        }catch (Exception e){
//            Log.d("Error4",e.toString());
//        }
//
//        try {
//
//            getSharedPreferenceValue5 = sharedpreferencesexit.getInt("currentProgress5", 10);
//
//            Log.i("channel5", String.valueOf(getSharedPreferenceValue5));
//        }catch (Exception e){
//            Log.d("Error5",e.toString());
//        }
//
//
//        if (getSharedPreferenceValue!=0) {
//          //  mediaPlayer1.start();
//
//            volumeSeekBar1.setProgress(getSharedPreferenceValue);
//            newvolume = (float) (Math.log(100 - (getSharedPreferenceValue-1)) / Math.log(100));
//
//            MainActivity.mediaPlayerRain.setVolume(1-newvolume,1-newvolume);
//            Log.i("sharedvalueinstarting",String.valueOf(newvolume));
//
//        }
//
//        if (getSharedPreferenceValue2!=0) {
//
////                mediaPlayer1.setVolume(1 - getSharedPreferenceValue, 1 - getSharedPreferenceValue);
//
////               volumeSeekBar1.setProgress(Integer.parseInt(getSharedPreferenceValue.toString()));
//            volumeSeekBar2.setProgress(getSharedPreferenceValue2);
//            newvolume2 = (float) (Math.log(100 - (getSharedPreferenceValue2-1)) / Math.log(100));
//
//            MainActivity.mediaPlayerRain2.setVolume(1-newvolume,1-newvolume);
//            Log.i("sharedvalueinstarting2",String.valueOf(newvolume2));
//
//        }
//        if (getSharedPreferenceValue3!=0) {
//           // mediaPlayer3.start();
////                mediaPlayer1.setVolume(1 - getSharedPreferenceValue, 1 - getSharedPreferenceValue);
//
////               volumeSeekBar1.setProgress(Integer.parseInt(getSharedPreferenceValue.toString()));
//            volumeSeekBar3.setProgress(getSharedPreferenceValue3);
//            newvolume3 = (float) (Math.log(100 - (getSharedPreferenceValue3-1)) / Math.log(100));
//
//            MainActivity.mediaPlayerRain3.setVolume(1-newvolume,1-newvolume);
//            Log.i("sharedvalueinstarting3",String.valueOf(newvolume3));
//
//        }
//        if (getSharedPreferenceValue4!=0) {
//           // mediaPlayer4.start();
////                mediaPlayer1.setVolume(1 - getSharedPreferenceValue, 1 - getSharedPreferenceValue);
//
////               volumeSeekBar1.setProgress(Integer.parseInt(getSharedPreferenceValue.toString()));
//            volumeSeekBar4.setProgress(getSharedPreferenceValue4);
//            newvolume4 = (float) (Math.log(100 - (getSharedPreferenceValue4-1)) / Math.log(100));
//
//            MainActivity.mediaPlayerRain4.setVolume(1-newvolume,1-newvolume);
//            Log.i("sharedvalueinstarting4",String.valueOf(newvolume4));
//
//        }
//        if (getSharedPreferenceValue5!=0) {
//            //mediaPlayer5.start();
////                mediaPlayer1.setVolume(1 - getSharedPreferenceValue, 1 - getSharedPreferenceValue);
//
////               volumeSeekBar1.setProgress(Integer.parseInt(getSharedPreferenceValue.toString()));
//            volumeSeekBar5.setProgress(getSharedPreferenceValue5);
//            newvolume5 = (float) (Math.log(100 - (getSharedPreferenceValue5-1)) / Math.log(100));
//
//            MainActivity.mediaPlayerRain5.setVolume(1-newvolume,1-newvolume);
//            Log.i("sharedvalueinstarting5",String.valueOf(newvolume5));
//
//        }

//        volumeSeekBar3.setProgress(current3 - min3);
//        audioManager1 = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        audioManager2 = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        audioManager3 = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        volumeSeekBar1.setProgress(allprogress1);
        volumeSeekBar2.setProgress(allprogress2);
        volumeSeekBar3.setProgress(allprogress3);
        volumeSeekBar4.setProgress(allprogress4);
        volumeSeekBar5.setProgress(allprogress5);

        try {
            if(mediaPlayerRain.isPlaying()){
                playButton.setBackgroundResource(R.drawable.pausebutton);
            }else {
                playButton.setBackgroundResource(R.drawable.playbutton);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        songName.setText(MainActivity.plname);


        /*{
            try {
                String stringPL = loadJSONFromAsset("progress.json");
                Context context = getApplicationContext();

                Resources resources = context.getResources();
                JSONObject progressJson = new JSONObject(stringPL);

                String playlistName=progressJson.getString("playlistjsonfile");
                Log.i("progress content", playlistName);

                JSONArray playlistNameArray = new JSONArray(playlistName);


                for (int i=0; i <playlistNameArray.length(); i++){

                    JSONObject playlistNameArrayPart = playlistNameArray.getJSONObject(i);

                    String name = playlistNameArrayPart.getString("name");

                    try {
                        if(MainActivity.plname.equals(name)  ){



                            newprogress1 =playlistNameArrayPart.getInt("progress1");
                            newprogress2 =playlistNameArrayPart.getInt("progress2");
                            newprogress3 =playlistNameArrayPart.getInt("progress3");
                            newprogress4 =playlistNameArrayPart.getInt("progress4");
                            newprogress5 =playlistNameArrayPart.getInt("progress5");
                            Log.i("progressjsonnew",String.valueOf(progress1));




//                        float log1 = (float) (Math.log(100 - (progress1-1)) / Math.log(100));
//                        float log2 = (float) (Math.log(100 - (progress2-1)) / Math.log(100));
//                        float log3 = (float) (Math.log(100 - (progress3-1)) / Math.log(100));
//                        float log4 = (float) (Math.log(100 - (progress4-1)) / Math.log(100));
//                        float log5 = (float) (Math.log(100 - (progress5-1)) / Math.log(100));


                            // get resource id by song song
//                    final int resourceSongId1 = resources.getIdentifier(song1, "raw", context.getPackageName());
//                    final int resourceSongId2 = resources.getIdentifier(song2, "raw", context.getPackageName());
//                    final int resourceSongId3 = resources.getIdentifier(song3, "raw", context.getPackageName());
//                    final int resourceSongId4 = resources.getIdentifier(song4, "raw", context.getPackageName());
//                    final int resourceSongId5 = resources.getIdentifier(song5, "raw", context.getPackageName());

                            Toast.makeText(getApplicationContext(),progress1+"hello",Toast.LENGTH_SHORT).show();


                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
*/



        try {
            volumeSeekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar volumeSeekBar, final int progress, boolean fromUser) {

           /*     if (saveOption) {
//                   final float log1 = (float) (Math.log(50-progress)/Math.log(50));

                    //mediaPlayer1.start();
                    mediaPlayerRain.setVolume(1-newvolume,1-newvolume);
                    mediaPlayerRain.setLooping(true);
                    Log.i("volume", String.valueOf(currentVolume));
                    volumeSeekBar1.setProgress(progress1);


                }
                else {*/
                    try {

                        progress1=progress;
                    Log.i("progress", String.valueOf(progress1));
                    log1 = (float) (Math.log(100 - (progress1)) / Math.log(100));
                    volumeSeekBar1.setProgress(progress1);
                    // mediaPlayer1.start();
                        mediaPlayerRain.setVolume(1-log1,1-log1);
                        mediaPlayerRain.setLooping(true);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.i("logvalue", String.valueOf(log1));
/*
                    saveMusic1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(saveOption){
                                log1 = (float) (Math.log(100 - (progress1-1)) / Math.log(100));

                                currentVolume = log1;
                                mediaPlayerRain.setLooping(true);
                                sharedPreferenceValueBoolean=true;
                                saveOption=false;

                            }
                            else {
                                log1 = (float) (Math.log(100 - (progress1-1)) / Math.log(100));

                                currentVolume = log1;
                               // mediaPlayer1.start();
                                mediaPlayerRain.setVolume(1-newvolume,1-newvolume);
                                mediaPlayerRain.setLooping(true);
                                saveOption=true;

//                                SharedPreferences.Editor editor = sharedpreferencesexit.edit();
//                                editor.putInt("currentProgress", progress1);
//                                editor.apply();

//                                Log.i("editorValue", String.valueOf(sharedpreferencesexit.getInt("currentProgress",10)));
                                Log.i("currentvolumetesting", String.valueOf(currentVolume));

                            }
                        }
                    });
*/
                    //}
                }

                @Override
                public void onStartTrackingTouch(SeekBar volumeSeekBar1) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar volumeSeekBar1) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        volumeSeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar volumeSeekBar, final int progress, boolean fromUser) {
//                if (saveOption2) {
////                   final float log1 = (float) (Math.log(50-progress)/Math.log(50));
//
//                   // mediaPlayer2.start();
//                    mediaPlayerRain2.setVolume(1-newvolume,1-newvolume);
//                    mediaPlayerRain2.setLooping(true);
//                    Log.i("volume", String.valueOf(currentVolume2));
//                    volumeSeekBar2.setProgress(progress2);
//
//
//                }

                   try {
                       progress2 = progress;

                       Log.i("progress2", String.valueOf(progress2));
                       log2 = (float) (Math.log(100 - (progress2)) / Math.log(100));
                       volumeSeekBar2.setProgress(progress2);
                       //mediaPlayer2.start();
                       mediaPlayerRain2.setVolume(1 - log2, 1 - log2);
                       mediaPlayerRain2.setLooping(true);
                       Log.i("logvalue2", String.valueOf(log2));
                   }catch (Exception e){
                       e.printStackTrace();
                   }
                    //saveButton
/*
                    saveMusic2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(saveOption2){
                                log2 = (float) (Math.log(100 - (progress2-1)) / Math.log(100));
                                currentVolume2 = log2;
                                mediaPlayerRain2.setLooping(true);
                                saveOption2=false;

                            }
                            else {
                                log2 = (float) (Math.log(100 - (progress2-1)) / Math.log(100));

                                currentVolume2 = log2;
                               // mediaPlayer2.start();
                                mediaPlayerRain2.setVolume(1-newvolume,1-newvolume);
                                mediaPlayerRain2.setLooping(true);
                                saveOption2=true;

//                                SharedPreferences.Editor editor = sharedpreferencesexit.edit();
//                                editor.putInt("currentProgress2", progress2);
//                                editor.apply();

//                                Log.i("editorValue2", String.valueOf(sharedpreferencesexit.getInt("currentProgress2",10)));
                                Log.i("currentvolumetesting2", String.valueOf(currentVolume2));

                            }
                        }
                    });
*/



            }

            @Override
            public void onStartTrackingTouch(SeekBar volumeSeekBar2) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar volumeSeekBar2) {


            }
        });

        volumeSeekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar volumeSeekBar, int progress, boolean fromUser) {
                try {
//                    if (saveOption3) {
////                   final float log1 = (float) (Math.log(50-progress)/Math.log(50));
//
//                        //mediaPlayer3.start();
//                        mediaPlayerRain3.setVolume(1-log3,1-log3);
//                        mediaPlayerRain4.setLooping(true);
//                        Log.i("volume", String.valueOf(currentVolume3));
//                        volumeSeekBar3.setProgress(progress3);
//
//                    }

                        progress3=progress;
                        Log.i("progress3", String.valueOf(progress3));
                        log3 = (float) (Math.log(100 - (progress3)) / Math.log(100));
                        volumeSeekBar3.setProgress(progress3);
                        // mediaPlayer3.start();
                        mediaPlayerRain3.setVolume(1-log3,1-log3);
                        mediaPlayerRain3.setLooping(true);
                        Log.i("logvalue3", String.valueOf(log3));
                        //saveButton
/*
                    saveMusic3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(saveOption3){
                                log3 = (float) (Math.log(100 - (progress2-1)) / Math.log(100));
                                currentVolume3 = log3;
                                mediaPlayerRain3.setLooping(true);
                                saveOption3=false;

                            }
                            else {
                                log3 = (float) (Math.log(100 - (progress3-1)) / Math.log(100));

                                currentVolume3 = log3;
                               // mediaPlayer3.start();
                                mediaPlayerRain3.setVolume(1-newvolume,1-newvolume);
                                mediaPlayerRain3.setLooping(true);
                                saveOption3=true;

//                                SharedPreferences.Editor editor = sharedpreferencesexit.edit();
//                                editor.putInt("currentProgress3", progress3);
//                                editor.apply();

                              //  Log.i("editorValue3", String.valueOf(sharedpreferencesexit.getInt("currentProgress3",10)));
                                Log.i("currentvolumetesting3", String.valueOf(currentVolume3));

                            }
                        }
                    });
*/

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar volumeSeekBar3) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar volumeSeekBar3) {

            }
        });

        volumeSeekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar volumeSeekBar, final int progress, boolean fromUser) {
//                if (saveOption4) {
////                   final float log1 = (float) (Math.log(50-progress)/Math.log(50));
//
//                    //mediaPlayer4.start();
//                    mediaPlayerRain4.setVolume(1-log4,1-log4);
//                    mediaPlayerRain4.setLooping(true);
//                    Log.i("volume", String.valueOf(currentVolume4));
//                    volumeSeekBar4.setProgress(progress4);
//
//
//                }

                   try {
                       progress4 = progress;

                       Log.i("progress4", String.valueOf(progress4));
                       log4 = (float) (Math.log(100 - (progress4)) / Math.log(100));
                       volumeSeekBar4.setProgress(progress4);

                       // mediaPlayer4.start();
                       mediaPlayerRain4.setVolume(1 - log4, 1 - log4);
                       mediaPlayerRain4.setLooping(true);
                       Log.i("logvalue4", String.valueOf(log4));
                   }catch (Exception e){
                       e.printStackTrace();
                   }
                    //saveButton
/*
                    saveMusic4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(saveOption4){
                                log4 = (float) (Math.log(100 - (progress4-1)) / Math.log(100));
                                currentVolume4 = log4;
                                mediaPlayerRain4.setLooping(true);
                                saveOption4=false;

                            }
                            else {
                                log4 = (float) (Math.log(100 - (progress4-1)) / Math.log(100));

                                currentVolume4 = log4;
                               // mediaPlayer4.start();
                                mediaPlayerRain4.setVolume(1-newvolume,1-newvolume);
                                mediaPlayerRain4.setLooping(true);
                                saveOption4=true;

//                                SharedPreferences.Editor editor = sharedpreferencesexit.edit();
//                                editor.putInt("currentProgress4", progress4);
//                                editor.apply();
//
//                                Log.i("editorValue4", String.valueOf(sharedpreferencesexit.getInt("currentProgress4",10)));
                                Log.i("currentvolumetesting4", String.valueOf(currentVolume4));

                            }
                        }
                    });
*/



            }

            @Override
            public void onStartTrackingTouch(SeekBar volumeSeekBar2) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar volumeSeekBar2) {


            }
        });

        volumeSeekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar volumeSeekBar, final int progress, boolean fromUser) {
//                if (saveOption5) {
////                   final float log1 = (float) (Math.log(50-progress)/Math.log(50));
//
//                    //mediaPlayer5.start();
//                    mediaPlayerRain5.setVolume(1-log5,1-log5);
//                    mediaPlayerRain5.setLooping(true);
//                    Log.i("volume5", String.valueOf(currentVolume5));
//                    volumeSeekBar5.setProgress(progress5);
//
//
//                }
               try {


                   progress5 = progress;
                   Log.i("progress5", String.valueOf(progress5));
                   log5 = (float) (Math.log(100 - (progress5)) / Math.log(100));
                   volumeSeekBar5.setProgress(progress5);
                   // mediaPlayer5.start();
                   mediaPlayerRain5.setVolume(1 - log5, 1 - log5);
                   mediaPlayerRain5.setLooping(true);
                   Log.i("logvalue5", String.valueOf(log5));

               }catch (Exception e){
                   e.printStackTrace();
               }
                    //saveButton
/*
                    saveMusic5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(saveOption5){
                                log5 = (float) (Math.log(100 - (progress5-1)) / Math.log(100));
                                currentVolume5 = log5;
                                mediaPlayerRain5.setLooping(true);
                                saveOption5=false;

                            }
                            else {
                                log5 = (float) (Math.log(100 - (progress5-1)) / Math.log(100));

                                currentVolume5 = log5;
                               // mediaPlayer5.start();
                                mediaPlayerRain5.setVolume(1 - currentVolume5, 1 - currentVolume5);
                                mediaPlayerRain5.setLooping(true);
                                saveOption5=true;

//                                SharedPreferences.Editor editor = sharedpreferencesexit.edit();
//                                editor.putInt("currentProgress5", progress5);
//                                editor.apply();
//
//                                Log.i("editorValue5", String.valueOf(sharedpreferencesexit.getInt("currentProgress5",10)));
                                Log.i("currentvolumetesting5", String.valueOf(currentVolume5));

                            }
                        }
                    });
*/



            }

            @Override
            public void onStartTrackingTouch(SeekBar volumeSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar volumeSeekBar) {


            }
        });

//       saveAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                SharedPreferences.Editor editor = sharedpreferencesexit.edit();
////                editor.putInt("currentProgress", progress1);
////                editor.putInt("currentProgress2", progress2);
////                editor.putInt("currentProgress3", progress3);
////                editor.putInt("currentProgress4", progress4);
////                editor.putInt("currentProgress5", progress5);
////                editor.apply();
//
//            }
//        });
//

//        saveAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (noteId != -1) {
//
//                }else {
//                    notes.add("");
//                    adapter.notifyDataSetChanged();
//                    noteId = notes.size() - 1;
//                }
//
//                notes.set(noteId, String.valueOf(progress1));
//                adapter.notifyDataSetChanged();
//                sharedPreferences = getApplicationContext()
//                        .getSharedPreferences("com.kitebe.wave", Context.MODE_PRIVATE);
//                HashSet<String> set = new HashSet<String>(notes);
//                sharedPreferences.edit().putStringSet("notes", set).apply();
//
//
//            }
//        });

        try {
            playList1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allprogress1=30;
                    allprogress2=47;
                    allprogress3=65;
                    allprogress4=34;
                    allprogress5=20;
                    volumeSeekBar1.setProgress(30);
                    volumeSeekBar2.setProgress(47);
                    volumeSeekBar3.setProgress(65);
                    volumeSeekBar4.setProgress(34);
                    volumeSeekBar5.setProgress(20);
                    imageId=R.drawable.rectangle;
                    songTheme2.setBackgroundResource(R.drawable.rectangle);
                    try {
                        MainActivity.songTheme1.setBackgroundResource(R.drawable.rectangle);

                    if(mediaPlayerRain.isPlaying()){
                        playButton.setBackgroundResource(R.drawable.pausebutton);
                    }else {
                        playButton.setBackgroundResource(R.drawable.playbutton);

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            playList2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allprogress1=2;
                    allprogress2=8;
                    allprogress3=2;
                    allprogress4=90;
                    allprogress5=2;

                    volumeSeekBar1.setProgress(2);
                    volumeSeekBar2.setProgress(8);
                    volumeSeekBar3.setProgress(2);
                    volumeSeekBar4.setProgress(90);
                    volumeSeekBar5.setProgress(2);

                    imageId=R.drawable.rectangle2;

                    songTheme2.setBackgroundResource(R.drawable.rectangle2);
                    try {
                        MainActivity.songTheme1.setBackgroundResource(R.drawable.rectangle2);

                    if(mediaPlayerRain.isPlaying()){
                        playButton.setBackgroundResource(R.drawable.pausebutton);
                    }else {
                        playButton.setBackgroundResource(R.drawable.playbutton);

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            playList3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allprogress1=60;
                    allprogress2=17;
                    allprogress3=1;
                    allprogress4=30;
                    allprogress5=20;
                    volumeSeekBar1.setProgress(60);
                    volumeSeekBar2.setProgress(17);
                    volumeSeekBar3.setProgress(1);
                    volumeSeekBar4.setProgress(30);
                    volumeSeekBar5.setProgress(20);

                    imageId=R.drawable.rectangle3;

                    songTheme2.setBackgroundResource(R.drawable.rectangle3);
                    try {
                        MainActivity.songTheme1.setBackgroundResource(R.drawable.rectangle3);

                    if(mediaPlayerRain.isPlaying()){
                        playButton.setBackgroundResource(R.drawable.pausebutton);
                    }else {
                        playButton.setBackgroundResource(R.drawable.playbutton);

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            playList4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allprogress1=70;
                    allprogress2=20;
                    allprogress3=70;
                    allprogress4=50;
                    allprogress5=20;
                    volumeSeekBar1.setProgress(70);
                    volumeSeekBar2.setProgress(20);
                    volumeSeekBar3.setProgress(70);
                    volumeSeekBar4.setProgress(50);
                    volumeSeekBar5.setProgress(20);

                    songTheme2.setBackgroundResource(R.drawable.rectangle);

                    imageId=R.drawable.rectangle;
                    try {
                        MainActivity.songTheme1.setBackgroundResource(R.drawable.rectangle);

                    if(mediaPlayerRain.isPlaying()){
                        playButton.setBackgroundResource(R.drawable.pausebutton);
                    }else {
                        playButton.setBackgroundResource(R.drawable.playbutton);

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            playList5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allprogress1=20;
                    allprogress2=70;
                    allprogress3=40;
                    allprogress4=40;
                    allprogress5=50;
                    volumeSeekBar1.setProgress(20);
                    volumeSeekBar2.setProgress(70);
                    volumeSeekBar3.setProgress(40);
                    volumeSeekBar4.setProgress(40);
                    volumeSeekBar5.setProgress(50);
                    songTheme2.setBackgroundResource(R.drawable.rectangle);

                    imageId=R.drawable.rectangle;
                    try {
                        MainActivity.songTheme1.setBackgroundResource(R.drawable.rectangle);

                    if(mediaPlayerRain.isPlaying()){
                        playButton.setBackgroundResource(R.drawable.pausebutton);
                    }else {
                        playButton.setBackgroundResource(R.drawable.playbutton);

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            playList6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allprogress1=10;
                    allprogress2=20;
                    allprogress3=40;
                    allprogress4=10;
                    allprogress5=50;
                    volumeSeekBar1.setProgress(10);
                    volumeSeekBar2.setProgress(20);
                    volumeSeekBar3.setProgress(40);
                    volumeSeekBar4.setProgress(10);
                    volumeSeekBar5.setProgress(50);
                    songTheme2.setBackgroundResource(R.drawable.rectangle2);

                    imageId=R.drawable.rectangle2;
                    try {
                        MainActivity.songTheme1.setBackgroundResource(R.drawable.rectangle2);

                    if(mediaPlayerRain.isPlaying()){
                        playButton.setBackgroundResource(R.drawable.pausebutton);
                    }else {
                        playButton.setBackgroundResource(R.drawable.playbutton);

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            playList7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allprogress1=20;
                    allprogress2=40;
                    allprogress3=40;
                    allprogress4=30;
                    allprogress5=20;
                    volumeSeekBar1.setProgress(20);
                    volumeSeekBar2.setProgress(40);
                    volumeSeekBar3.setProgress(40);
                    volumeSeekBar4.setProgress(30);
                    volumeSeekBar5.setProgress(20);
                    songTheme2.setBackgroundResource(R.drawable.rectangle3);

                    imageId=R.drawable.rectangle3;
                    try {
                        MainActivity.songTheme1.setBackgroundResource(R.drawable.rectangle3);

                    if(mediaPlayerRain.isPlaying()){
                        playButton.setBackgroundResource(R.drawable.pausebutton);
                    }else {
                        playButton.setBackgroundResource(R.drawable.playbutton);

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            playList8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allprogress1=70;
                    allprogress2=40;
                    allprogress3=70;
                    allprogress4=30;
                    allprogress5=20;
                    volumeSeekBar1.setProgress(70);
                    volumeSeekBar2.setProgress(40);
                    volumeSeekBar3.setProgress(70);
                    volumeSeekBar4.setProgress(30);
                    volumeSeekBar5.setProgress(20);
                    songTheme2.setBackgroundResource(R.drawable.rectangle);

                    imageId=R.drawable.rectangle;
                    try {
                        MainActivity.songTheme1.setBackgroundResource(R.drawable.rectangle);

                    if(mediaPlayerRain.isPlaying()){
                        playButton.setBackgroundResource(R.drawable.pausebutton);
                    }else {
                        playButton.setBackgroundResource(R.drawable.playbutton);

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            playList9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allprogress1=100;
                    allprogress2=10;
                    allprogress3=10;
                    allprogress4=20;
                    allprogress5=30;
                    volumeSeekBar1.setProgress(100);
                    volumeSeekBar2.setProgress(10);
                    volumeSeekBar3.setProgress(10);
                    volumeSeekBar4.setProgress(20);
                    volumeSeekBar5.setProgress(30);
                    songTheme2.setBackgroundResource(R.drawable.rectangle2);

                    imageId=R.drawable.rectangle2;
                    try {
                        MainActivity.songTheme1.setBackgroundResource(R.drawable.rectangle2);

                    if(mediaPlayerRain.isPlaying()){
                        playButton.setBackgroundResource(R.drawable.pausebutton);
                    }else {
                        playButton.setBackgroundResource(R.drawable.playbutton);

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            playList10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allprogress1=40;
                    allprogress2=50;
                    allprogress3=100;
                    allprogress4=10;
                    allprogress5=20;
                    volumeSeekBar1.setProgress(40);
                    volumeSeekBar2.setProgress(50);
                    volumeSeekBar3.setProgress(100);
                    volumeSeekBar4.setProgress(10);
                    volumeSeekBar5.setProgress(20);
                    songTheme2.setBackgroundResource(R.drawable.rectangle3);

                    imageId=R.drawable.rectangle3;
                    try {
                        MainActivity.songTheme1.setBackgroundResource(R.drawable.rectangle3);

                    if(mediaPlayerRain.isPlaying()){
                        playButton.setBackgroundResource(R.drawable.pausebutton);
                    }else {
                        playButton.setBackgroundResource(R.drawable.playbutton);

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            playList11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allprogress1=50;
                    allprogress2=80;
                    allprogress3=70;
                    allprogress4=40;
                    allprogress5=20;
                    volumeSeekBar1.setProgress(50);
                    volumeSeekBar2.setProgress(80);
                    volumeSeekBar3.setProgress(70);
                    volumeSeekBar4.setProgress(40);
                    volumeSeekBar5.setProgress(20);
                    songTheme2.setBackgroundResource(R.drawable.rectangle);

                    imageId=R.drawable.rectangle;
                    try {
                        MainActivity.songTheme1.setBackgroundResource(R.drawable.rectangle);

                    if(mediaPlayerRain.isPlaying()){
                        playButton.setBackgroundResource(R.drawable.pausebutton);
                    }else {
                        playButton.setBackgroundResource(R.drawable.playbutton);

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            playList12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allprogress1=80;
                    allprogress2=40;
                    allprogress3=80;
                    allprogress4=50;
                    allprogress5=20;
                    volumeSeekBar1.setProgress(80);
                    volumeSeekBar2.setProgress(40);
                    volumeSeekBar3.setProgress(80);
                    volumeSeekBar4.setProgress(50);
                    volumeSeekBar5.setProgress(20);
                    songTheme2.setBackgroundResource(R.drawable.rectangle);

                    imageId=R.drawable.rectangle;
                    try {
                        MainActivity.songTheme1.setBackgroundResource(R.drawable.rectangle);

                    if(mediaPlayerRain.isPlaying()){
                        playButton.setBackgroundResource(R.drawable.pausebutton);
                    }else {
                        playButton.setBackgroundResource(R.drawable.playbutton);

                    }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    if (mediaPlayerRain.isPlaying()) {
                        mediaPlayerRain.pause();
                        mediaPlayerRain2.pause();
                        mediaPlayerRain3.pause();
                        mediaPlayerRain4.pause();
                        mediaPlayerRain5.pause();
                        playButton.setBackgroundResource(R.drawable.playbutton);
                        try {
                            MainActivity.playbutton.setBackgroundResource(R.drawable.playbutton);
                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }else{
                        mediaPlayerRain.start();
                        mediaPlayerRain2.start();
                        mediaPlayerRain3.start();
                        mediaPlayerRain4.start();
                        mediaPlayerRain5.start();
                        playButton.setBackgroundResource(R.drawable.pausebutton);
                        try {
                            MainActivity.playbutton.setBackgroundResource(R.drawable.pausebutton);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                isPlayingBoolean = !isPlayingBoolean;
            }
        });


    }


    public void onBackPressed() {
        super.onBackPressed();
//        mediaPlayer1.stop();
//        mediaPlayer2.stop();
//        mediaPlayer3.stop();
//        mediaPlayer4.stop();
//        mediaPlayer5.stop();
    }
    @Override
    protected void onStop() {
        super.onStop();
//        MainActivity.mediaPlayerRain.stop();
//        MainActivity.mediaPlayerRain2.stop();
//        MainActivity.mediaPlayerRain3.stop();
//        MainActivity.mediaPlayerRain4.stop();
//        MainActivity.mediaPlayerRain5.stop();
    }
}
