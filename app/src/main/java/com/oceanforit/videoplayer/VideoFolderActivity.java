package com.oceanforit.videoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;
import com.oceanforit.videoplayer.adapter.VideoAdapter;
import com.oceanforit.videoplayer.common.Common;
import com.oceanforit.videoplayer.models.Video;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFolderActivity extends AppCompatActivity {
    private static final String TAG = VideoFolderActivity.class.getSimpleName();

    @BindView(R.id._recycler_videos_folder)
    RecyclerView _recyclerVideos;

    private List<Video> listVideos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder);
        ButterKnife.bind(this);

        
        _recyclerVideos.setHasFixedSize(true);
        _recyclerVideos.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent() != null) {
            String folderName = getIntent().getStringExtra(Common.KEY_FOLDER_NAME);
            if (folderName != null) {
                listVideos = getAllVideos(this, folderName);
            }
        }

        if (listVideos.size() > 0) {
            VideoAdapter videoAdapter = new VideoAdapter(this, listVideos);
            _recyclerVideos.setAdapter(videoAdapter);
        }
    }

    private List<Video> getAllVideos(Context context, String folderName) {
        List<Video> tempListVideos = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        };

        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};
        Cursor cursor = context.getContentResolver()
                .query(uri, projection, selection, selectionArgs, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String fileName = cursor.getString(3);
                String size = cursor.getString(4);
                String dateAdded = cursor.getString(5);
                String duration = cursor.getString(6);
                String bucketName = cursor.getString(7);

                Video video = new Video(id, path, title, fileName, size, dateAdded, duration);

               // /storage/emulated/0/videoDir/Abc/filename.mp4
                if (folderName.endsWith(bucketName))
                    tempListVideos.add(video);
                Log.d(TAG, "getAllVIdeos: " + new Gson().toJson(tempListVideos));
            }
            cursor.close();

        }
        return tempListVideos;
    }
}