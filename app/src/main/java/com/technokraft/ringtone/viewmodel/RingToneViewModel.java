package com.technokraft.ringtone.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.technokraft.ringtone.model.ItunesResponse;
import com.technokraft.ringtone.model.Song;

import org.json.JSONObject;

import java.util.List;

public class RingToneViewModel extends AndroidViewModel{

    private static final String TAG = "RingToneViewModel";
    public MutableLiveData<String> searchKey = new MutableLiveData<>();
    private MutableLiveData<List<Song>> songList = new MutableLiveData<>();
    public MutableLiveData<Boolean> spinner = new MutableLiveData<>();


    RequestQueue requestQueue;

    Response.Listener onSuccess = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Gson gson = new Gson();
            ItunesResponse itunesResponse = gson.fromJson(response.toString(), ItunesResponse.class);
            spinner.setValue(false);
            songList.setValue(itunesResponse.getResults());
        }
    };

    Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Log.d(TAG, error.toString());
        }
    };


    public RingToneViewModel(@NonNull Application application) {
        super(application);
        requestQueue = Volley.newRequestQueue(application);
        spinner.setValue(false);
    }

    public void searchSong() {
        spinner.setValue(true);
        String key = searchKey.getValue().trim().replace(" ", "+");
        String url = "https://itunes.apple.com/search?term=" + key;
        requestQueue.add(new JsonObjectRequest(Request.Method.GET, url, onSuccess, onError));
    }

    public MutableLiveData<List<Song>> getSongList() {
        return songList;
    }
}
