package com.oceanforit.videoplayer.ui.videos;

import static com.oceanforit.videoplayer.MainActivity.listVideos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oceanforit.videoplayer.R;
import com.oceanforit.videoplayer.adapter.VideoAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideosFragment extends Fragment {

    @BindView(R.id._recycler_videos)
    RecyclerView _recyclerVideos;

    private VideoAdapter videoAdapter;

    private static VideosFragment instance;

    public static VideosFragment getInstance(){
        return instance == null ? new VideosFragment() : instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_videos, container, false);
        ButterKnife.bind(this, layoutView);
        initUI();
        loadVideos();
        return layoutView;
    }

    private void loadVideos() {

        if (listVideos != null && listVideos.size() > 0){
            videoAdapter = new VideoAdapter(getActivity(), listVideos);
            _recyclerVideos.setAdapter(videoAdapter);
        }
    }

    private void initUI() {

        _recyclerVideos.setHasFixedSize(true);
        _recyclerVideos.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
