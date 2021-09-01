package com.oceanforit.videoplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oceanforit.videoplayer.R;
import com.oceanforit.videoplayer.VideoFolderActivity;
import com.oceanforit.videoplayer.callback.ItemClickListener;
import com.oceanforit.videoplayer.common.Common;
import com.oceanforit.videoplayer.models.Video;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderHolder> {

    private Context context;
    private List<String> listFolders;
    private List<Video> listVideos;
    private LayoutInflater inflater;
    private @LayoutRes int resource;

    public FolderAdapter(Context context, List<String> listFolders, List<Video> listVideos) {
        this.context = context;
        this.listFolders = listFolders;
        this.listVideos = listVideos;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FolderHolder(inflater.inflate(R.layout.row_folders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        int index = listFolders.get(position).lastIndexOf("/");
        String folderName = listFolders.get(position).substring(index + 1);
        holder._txtFolderName.setText(folderName);
        holder._txtVideosCounts.setText(String.valueOf(numberOfFiles(listFolders.get(position))));

        holder.setItemClickListener((view, position1) -> {
            context.startActivity(new Intent(context, VideoFolderActivity.class)
                    .putExtra(Common.KEY_FOLDER_NAME, listFolders.get(position)));
        });
    }

    @Override
    public int getItemCount() {
        return listFolders.size();
    }

    private int numberOfFiles(String fileName) {
        int countFiles = 0;
        for (Video video : listVideos){
            if (video.getPath().substring(0, video.getPath().lastIndexOf("/"))
            .endsWith(fileName)){
                countFiles++;
            }
        }
        return countFiles;
    }

    class FolderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id._txt_folder_name)
        TextView _txtFolderName;
        @BindView(R.id._txt_videos_count)
        TextView _txtVideosCounts;

        private ItemClickListener itemClickListener;

        public FolderHolder(@NonNull View itemView) {
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
