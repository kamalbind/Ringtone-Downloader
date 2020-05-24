package com.technokraft.ringtone.view.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technokraft.ringtone.viewmodel.RingToneViewModel;
import com.technokraft.ringzone.R;
import com.technokraft.ringzone.databinding.FragmentSongPreviewBinding;
import com.technokraft.ringzone.databinding.RingtoneItemBinding;

/**
 * A simple {@link Fragment} subclass.¡
 */
public class SongPreview extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSongPreviewBinding fragmentSongPreviewBinding = FragmentSongPreviewBinding.inflate(inflater, container, false);
        RingToneViewModel ringToneViewModel = ViewModelProviders.of(requireActivity()).get(RingToneViewModel.class);
        fragmentSongPreviewBinding.setRingToneViewModel(ringToneViewModel);

        return fragmentSongPreviewBinding.getRoot();
    }

}
