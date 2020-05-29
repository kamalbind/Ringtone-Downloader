package com.technokraft.ringtone.viewmodel;

import android.app.Application;
import android.app.DownloadManager;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.technokraft.ringtone.interfaces.CheckPermission;
import com.technokraft.ringtone.model.ItunesResponse;
import com.technokraft.ringtone.model.Song;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class RingToneViewModel extends AndroidViewModel {

    private static final String TAG = "RingToneViewModel";
    public MutableLiveData<String> mSearchKey = new MutableLiveData<>();
    public MutableLiveData<List<Song>> mSongList = new MutableLiveData<>();
    public MutableLiveData<Boolean> mSpinner = new MutableLiveData<>();
    public MutableLiveData<Song> mSong = new MutableLiveData<>();
    public MediaPlayer mMediaPlayer = new MediaPlayer();
    public MutableLiveData<Boolean> isPrepaired = new MutableLiveData<>();
    public CheckPermission mCheckPermissionListner;

    RequestQueue requestQueue;

    private Response.Listener onSuccess = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Gson gson = new Gson();
            ItunesResponse itunesResponse = gson.fromJson(response.toString(), ItunesResponse.class);
            mSpinner.setValue(false);
            mSongList.setValue(itunesResponse.getResults());
        }
    };

    private Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Log.d(TAG, error.toString());
        }
    };

    public RingToneViewModel(@NonNull Application application) {
        super(application);
        requestQueue = Volley.newRequestQueue(application);
        mSpinner.setValue(false);
    }

    public void searchSong() {
        mSpinner.setValue(true);
        String key = mSearchKey.getValue().trim().replace(" ", "+");
        String url = "https://itunes.apple.com/search?term=" + key;
        requestQueue.add(new JsonObjectRequest(Request.Method.GET, url, onSuccess, onError));
    }

    @BindingAdapter("android:imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get().load(imageUrl).into(view);
    }

    public void loadSound() {
        isPrepaired.setValue(false);
        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(mSong.getValue().getPreviewUrl());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                isPrepaired.setValue(true);
            }
        });
    }

    public void playSound() {
        if (isPrepaired.getValue()) {
            mMediaPlayer.start();
        }
    }

    public void stopSound() {
        mMediaPlayer.pause();
        mMediaPlayer.seekTo(0);
    }

    public void downloadSound() {
        if (mCheckPermissionListner.storagePermission()) {
            Uri uri = Uri.parse(mSong.getValue().getPreviewUrl());
            DownloadManager downloadManager = (DownloadManager) getApplication().getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setTitle("Ringtone Downloading");
            request.setDescription(mSong.getValue().getTrackName());
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_RINGTONES,
                    mSong.getValue().getArtistName() + " - " + mSong.getValue().getTrackName()+".m4a");
            request.setMimeType("*/*");
            downloadManager.enqueue(request);
        } else {
            mCheckPermissionListner.requestStoragePermission();
        }
    }

}
