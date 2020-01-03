package com.kitebe.wave;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;


public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener, AudioManager.OnAudioFocusChangeListener {

    public static final String ACTION_PLAY = "com.kitebe.wave.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.kitebe.wave.ACTION_PAUSE";
    public static final String ACTION_STOP = "com.kitebe.wave.ACTION_STOP";

    // the components that make up environmental noise
    private ArrayList<MediaPlayer> components;
    int[] resumePositions = new int[5];

    // mediasession stuff
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    // focus control and stuff
    private AudioManager audioManager;

    // binder for clients
    private final IBinder iBinder = new LocalBinder();

    // handle calls
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;


    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        callStateListener();
        registerBecomingNoisyReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (requestAudioFocus() == false) {
            stopSelf();
        }
        if (mediaSessionManager == null) {
            try {
                initMediaSession();
                initMediaPlayer();
            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }
        }
        handleIncomingActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaSession.release();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (components.get(0) != null || components.get(1) != null || components.get(2) != null || components.get(3) != null || components.get(4) != null) {
            stopMedia();
            for (MediaPlayer comp : components) {
                comp.release();
            }
        }
        removeAudioFocus();
        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        unregisterReceiver(becomingNoisyReceiver);
    }

    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopMedia();
        stopSelf();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playMedia();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        //Invoked indicating the completion of a seek operation.
    }

    @Override
    public void onAudioFocusChange(int focusState) {

        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if (components.get(0) == null || components.get(1) == null || components.get(2) == null || components.get(3) == null || components.get(4) == null)
                    initMediaPlayer();
                for (MediaPlayer comp : components) {
                    if (!comp.isPlaying())
                        comp.start();
                    comp.setVolume(1.0f, 1.0f);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                for (MediaPlayer comp : components) {
                    if (comp.isPlaying())
                        comp.stop();
                    comp.release();
                    comp = null;
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                for (MediaPlayer comp : components) {
                    if (comp.isPlaying())
                        comp.pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                for (MediaPlayer comp : components) {
                    if (comp.isPlaying())
                        comp.setVolume(0.1f, 0.1f);
                }
                break;
        }
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }

    private void initMediaPlayer() {
        for (MediaPlayer comp : components) {
            if (comp == null)
                comp = new MediaPlayer();
            comp.setOnCompletionListener(this);
            comp.setOnErrorListener(this);
            comp.setOnPreparedListener(this);
            comp.setOnBufferingUpdateListener(this);
            comp.setOnSeekCompleteListener(this);
            comp.setOnInfoListener(this);
            comp.prepareAsync();
        }
    }

    private void setDataSource(ArrayList<String> songList) {
        if (songList.size() == components.size()) {
            for (int i = 0; i < components.size(); i++) {
                MediaPlayer comp = components.get(i);
                if (comp == null) {
                    comp = new MediaPlayer();
                    comp = MediaPlayer.create(getApplicationContext(), Uri.parse("file:///android_asset/" + songList.get(i)));
                }

            }
        }
    }

    private void playMedia() {
        for (MediaPlayer comp : components) {
            if (!comp.isPlaying())
                comp.start();
        }
    }

    private void stopMedia() {
        if (components.get(0) == null || components.get(1) == null || components.get(2) == null || components.get(3) == null || components.get(4) == null)
            return;
        for (MediaPlayer comp : components) {
            if (comp.isPlaying())
                comp.stop();
        }
    }

    private void pauseMedia() {
        if (components.get(0).isPlaying() || components.get(1).isPlaying() || components.get(2).isPlaying() || components.get(3).isPlaying() || components.get(4).isPlaying()) {
            for (int i = 0; i < resumePositions.length; i++) {
                components.get(i).pause();
                resumePositions[i] = components.get(i).getCurrentPosition();
            }
        }
    }

    private void resumeMedia() {
        if (!components.get(0).isPlaying() || !components.get(1).isPlaying() || !components.get(2).isPlaying() || !components.get(3).isPlaying() || !components.get(4).isPlaying()) {
            for (int i = 0; i < resumePositions.length; i++) {
                components.get(i).seekTo(resumePositions[i]);
                components.get(i).start();
            }
        }
    }

    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseMedia();
        }
    };

    private void registerBecomingNoisyReceiver() {
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver, intentFilter);
    }

    private void callStateListener() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (components.get(0) != null || components.get(1) != null || components.get(2) != null || components.get(3) != null || components.get(4) != null) {
                            pauseMedia();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (components.get(0) != null || components.get(1) != null || components.get(2) != null || components.get(3) != null || components.get(4) != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                resumeMedia();
                            }
                        }
                        break;
                }
            }
        };
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void initMediaSession() throws RemoteException {
        if (mediaSessionManager != null) return;

        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                resumeMedia();
            }

            @Override
            public void onPause() {
                super.onPause();
                pauseMedia();
            }

            @Override
            public void onStop() {
                super.onStop();
                stopSelf();
            }

            @Override
            public void onSeekTo(long position) {
                super.onSeekTo(position);
            }
        });
    }

    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, MediaPlayerService.class);
        switch (actionNumber) {
            case 0:
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 1:
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;
        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }

    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            stopMedia();
            for (MediaPlayer comp : components) {
                comp.reset();
            }
            initMediaPlayer();
            setDataSource(new ArrayList<>(Arrays.asList("song1.mp3", "song2.mp3", "song3.mp3", "song4.mp3", "song5.mp3")));
        }
    };

    private void register_playNewAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter("com.kitebe.wave.playAudio");
        registerReceiver(playNewAudio, filter);
    }

}
