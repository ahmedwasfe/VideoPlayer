package com.oceanforit.videoplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oceanforit.videoplayer.PlayerActivity;
import com.oceanforit.videoplayer.R;
import com.oceanforit.videoplayer.callback.ItemClickListener;
import com.oceanforit.videoplayer.common.Common;
import com.oceanforit.videoplayer.models.Video;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private Context context;
    public static List<Video> listVideos;
    private LayoutInflater inflater;

    public VideoAdapter(Context context, List<Video> listVideos) {
        this.context = context;
        this.listVideos = listVideos;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoHolder(inflater.inflate(R.layout.row_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {

        holder._txtVideoName.setText(listVideos.get(position).getTitle());
        holder._txtDuration.setText(Common.formattedTime(Integer.parseInt(listVideos.get(position).getDuration()) / 1000));
        Glide.with(context)
                .asBitmap()
                .load(new File(listVideos.get(position).getPath()))
                .into(holder._imgThumbnail);

        holder.setItemClickListener((view, position1) -> {
            context.startActivity(new Intent(context, PlayerActivity.class)
                    .putExtra(Common.KEY_POSITION, position));
        });
    }

    @Override
    public int getItemCount() {
        return listVideos.size();
    }

    class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id._img_thumbnail)
        ImageView _imgThumbnail;
        @BindView(R.id._txt_video_duration)
        TextView _txtDuration;
        @BindView(R.id._txt_video_name)
        TextView _txtVideoName;
        @BindView(R.id._img_more)
        ImageView _more;

        private ItemClickListener itemClickListener;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClickListener(v, getAdapterPosition());
        }
    }
}
