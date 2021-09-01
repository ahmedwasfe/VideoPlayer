package com.oceanforit.videoplayer;

import static com.oceanforit.videoplayer.adapter.VideoAdapter.listVideos;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.oceanforit.videoplayer.common.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerActivity extends AppCompatActivity {
    private static final String TAG = PlayerActivity.class.getSimpleName();

    @BindView(R.id._player)
    PlayerView _player;

    private SimpleExoPlayer simpleExoPlayer;
    private int position = -1;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {

        if (getIntent() != null) {
            position = getIntent().getIntExtra(Common.KEY_POSITION, -1);
            Log.d(TAG, "initUI: " + position);

            path = listVideos.get(position).getPath();
            Log.d(TAG, "initUI: " + path);
            playVideo(path);
            getSupportActionBar().setTitle(listVideos.get(position).getTitle());
        }

    }

    private void playVideo(String path) {

        Log.d(TAG, "playVideo: " + path);
        if (path != null) {
            Uri uri = Uri.parse(path);
            simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();
            DataSource.Factory factory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, getString(R.string.app_name)));
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ProgressiveMediaSource
                    .Factory(factory, extractorsFactory).createMediaSource(MediaItem.fromUri(uri));
            _player.setPlayer(simpleExoPlayer);
            _player.setKeepScreenOn(true);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    private void setFullScreen() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        simpleExoPlayer.release();
    }
}