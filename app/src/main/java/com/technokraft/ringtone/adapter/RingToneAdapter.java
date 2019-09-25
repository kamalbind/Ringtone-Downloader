package com.technokraft.ringtone.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.technokraft.ringtone.interfaces.SongEventListener;
import com.technokraft.ringtone.model.Song;
import com.technokraft.ringzone.R;
import com.technokraft.ringzone.databinding.RingtoneItemBinding;

import java.util.List;


public class RingToneAdapter extends RecyclerView.Adapter<RingToneAdapter.ViewHolder> implements SongEventListener {

    private List<Song> songList;

    public RingToneAdapter() {
    }

    public void setSongList(List<Song> sList){
        songList = sList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RingtoneItemBinding ringtoneItemBinding
                = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.ringtone_item,viewGroup,false);
        return new ViewHolder(ringtoneItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.ringtoneItemBinding.setSong(songList.get(i));
        viewHolder.ringtoneItemBinding.setItemClickListener(this);
    }

    @Override
    public int getItemCount() {
        if (songList == null) return 0;
        else return songList.size();
    }

    @Override
    public void previewSongClick(Song song) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private RingtoneItemBinding ringtoneItemBinding;

        private ViewHolder(RingtoneItemBinding ringtoneItemLayoutBinding) {
            super(ringtoneItemLayoutBinding.getRoot());
            ringtoneItemBinding = ringtoneItemLayoutBinding;
        }
    }

}
