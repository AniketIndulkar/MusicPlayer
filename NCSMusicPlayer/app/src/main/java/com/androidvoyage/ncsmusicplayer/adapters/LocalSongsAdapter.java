package com.androidvoyage.ncsmusicplayer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidvoyage.ncsmusicplayer.R;
import com.androidvoyage.ncsmusicplayer.utils.AppConstants;

import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class LocalSongsAdapter extends RecyclerView.Adapter<LocalSongsAdapter.SongsViewHolder> {

    private List<HashMap<String, String>> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public LocalSongsAdapter(Context context, List<HashMap<String, String>> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public SongsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.songs_row, parent, false);
        return new SongsViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(SongsViewHolder holder, int position) {
        holder.tvSongName.setText(mData.get(position).get(AppConstants.SONG_TITLE));
        holder.tvSongAlbum.setText(mData.get(position).get(AppConstants.ALBUM));
        holder.tvSongDuration.setText(mData.get(position).get(AppConstants.Duration) + "m");


        new Thread(() -> {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mData.get(position).get(AppConstants.SONG_PATH));
            byte[] data = mmr.getEmbeddedPicture();
            if (data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                new Handler(Looper.getMainLooper()).post(() -> {
                    holder.ivSongImage.setImageBitmap(bitmap); //associated cover art in bitmap
                });
            } else {
                new Handler(Looper.myLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        holder.ivSongImage.setImageResource(R.drawable.ic_launcher_background); //any default cover resourse folder
                    }
                });
            }
        }).start();

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class SongsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvSongName, tvSongAlbum, tvSongDuration;
        ImageView ivSongImage;

        SongsViewHolder(View itemView) {
            super(itemView);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            tvSongAlbum = itemView.findViewById(R.id.tvSongAlbum);
            tvSongDuration = itemView.findViewById(R.id.tvSongDuration);
            ivSongImage = itemView.findViewById(R.id.ivSongImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}