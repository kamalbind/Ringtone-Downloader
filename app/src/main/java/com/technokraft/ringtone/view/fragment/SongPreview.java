package com.technokraft.ringtone.view.fragment;


import android.Manifest;
import android.app.DownloadManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.technokraft.ringtone.interfaces.CheckPermission;
import com.technokraft.ringtone.viewmodel.RingToneViewModel;
import com.technokraft.ringzone.R;
import com.technokraft.ringzone.databinding.FragmentSongPreviewBinding;

/**
 * A simple {@link Fragment} subclass.¡
 */
public class SongPreview extends Fragment implements CheckPermission {

    RingToneViewModel ringToneViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final FragmentSongPreviewBinding fragmentSongPreviewBinding = FragmentSongPreviewBinding.inflate(inflater, container, false);
        ringToneViewModel = ViewModelProviders.of(requireActivity()).get(RingToneViewModel.class);
        fragmentSongPreviewBinding.setRingToneViewModel(ringToneViewModel);
        ringToneViewModel.mCheckPermissionListner = this;
        ringToneViewModel.isPrepaired.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isPrepaired) {
                if (isPrepaired) {
                    fragmentSongPreviewBinding.playButton.setImageResource(R.drawable.ic_play_arrow_black_75dp);
                } else {
                    fragmentSongPreviewBinding.playButton.setImageResource(R.drawable.ic_prepair_async_black_75dp);
                }
            }
        });

        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // logic to set the ringtone
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        return fragmentSongPreviewBinding.getRoot();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Toast.makeText(getActivity(), "I did came here!", Toast.LENGTH_SHORT).show();

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ringToneViewModel.downloadSound();
        } else {
            Toast.makeText(getActivity(), "Cant Download Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean storagePermission() {
        return getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }
}
