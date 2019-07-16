package com.androidvoyage.ncsmusicplayer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
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

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
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

//        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
//            @Override
//            public Shader resize(int width, int height) {
//                LinearGradient linearGradient = new LinearGradient(0, 0, width, 0,
//                        new int[]{
//                                0xFFFFFFFF,
//                                0xFFFFFFFF,
//                                0xFFFFFFFF,
//                                0xB3000000,
//                                0x66000000,
//                                0x1A000000,
//                                0x00FFFFFF}, //substitute the correct colors for these
//                        new float[]{
//                                0, 0.40f, 0.60f,0.70f,0.80f,0.90f, 1},
//                        Shader.TileMode.REPEAT);
//                return linearGradient;
//            }
//        };
//        PaintDrawable paint = new PaintDrawable();
//        paint.setShape(new RectShape());
//        paint.setShaderFactory(shaderFactory);

//        holder.gradientView.setBackgroundDrawable(paint);


        new Thread(() -> {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mData.get(position).get(AppConstants.SONG_PATH));
            byte[] data = mmr.getEmbeddedPicture();
            if (data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                new Handler(Looper.getMainLooper()).post(() -> {
                    holder.ivSongImage.setImageBitmap(bitmap); //associated cover art in bitmap
                    holder.ivSongImage.setVisibility(View.VISIBLE);
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
        CardView mainCardView;

        SongsViewHolder(View itemView) {
            super(itemView);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            tvSongAlbum = itemView.findViewById(R.id.tvSongAlbum);
            tvSongDuration = itemView.findViewById(R.id.tvSongDuration);
            ivSongImage = itemView.findViewById(R.id.ivSongImage);
            mainCardView=itemView.findViewById(R.id.mainCardView);
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