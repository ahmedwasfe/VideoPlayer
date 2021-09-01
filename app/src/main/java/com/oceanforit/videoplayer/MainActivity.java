package com.oceanforit.videoplayer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.oceanforit.videoplayer.models.Video;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int CODE_REQUEST_STORAGE_PERMISSION = 88;


    @BindView(R.id.bottom_navigation)
    BottomNavigationView _btnNav;

    private AppBarConfiguration _appBarConfig;
    private NavController _navController;

    public static List<Video> listVideos = new ArrayList<>();
    public static List<String> listFolders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestPermission();
        initNavigation();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, CODE_REQUEST_STORAGE_PERMISSION);
        else
            listVideos = getAllVideos(this);
    }

    private void initNavigation() {

        _appBarConfig = new AppBarConfiguration.Builder(
                R.id.nav_folders,
                R.id.nav_videos)
                .build();

        _navController = Navigation.findNavController(this, R.id.nav_fragments);
        NavigationUI.setupActionBarWithNavController(this, _navController, _appBarConfig);
        NavigationUI.setupWithNavController(_btnNav, _navController);

        _btnNav.setOnNavigationItemSelectedListener(item -> {

            item.setChecked(true);

            switch (item.getItemId()) {
                case R.id.nav_folders:
                    item.setChecked(true);
                    _navController.navigate(R.id.nav_folders);
                    break;
                case R.id.nav_videos:
                    item.setChecked(true);
                    _navController.navigate(R.id.nav_videos);
                    break;
            }

            return false;
        });
        _btnNav.bringToFront();
    }

    private List<Video> getAllVideos(Context context){
        List<Video> tempListVideos = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION
        };

        Cursor cursor = context.getContentResolver()
                .query(uri, projection,null,null,null);
        if (cursor != null){
            while(cursor.moveToNext()){
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String fileName = cursor.getString(3);
                String size = cursor.getString(4);
                String dateAdded = cursor.getString(5);
                String duration = cursor.getString(6);

                Video video = new Video(id, path, title, fileName, size, dateAdded, duration);

                /* /storage/emulated/0/videoDir/Abc/filename.mp4
                * /storage/emulated/0/DCIM/Camera/4c50963711c1f1cf62074c9a9b94a8e6.mp4
                 */
                int slashFirstIndex = path.lastIndexOf("/");
                String subString = path.substring(0, slashFirstIndex);
                // storage/emulated/0/videoDir/Abc - becuase list index excluded so slash
                // excluded
                // after doing this it will give us Abc(Camera) also folder name
                if (!listFolders.contains(subString))
                    listFolders.add(subString);
                tempListVideos.add(video);
                Log.d(TAG, "getAllVIdeos: " + new Gson().toJson(tempListVideos));
            }
            cursor.close();

        }
        return tempListVideos;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_REQUEST_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                listVideos = getAllVideos(this);
            else
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, CODE_REQUEST_STORAGE_PERMISSION);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_about:
                showDialogAboutApp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogAboutApp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_about, null);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);

        TextView _appVersion = dialogView.findViewById(R.id._txt_app_version);
        _appVersion.setText(getAppVersion());

        dialogView.findViewById(R.id._txt_close)
                .setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private String getAppVersion() {

        String appVersion = "";
        try {
            PackageInfo packageInfo = getPackageManager()
                    .getPackageInfo(getPackageName(), 0);
            appVersion = "App Version: " + packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return appVersion;
    }
}
