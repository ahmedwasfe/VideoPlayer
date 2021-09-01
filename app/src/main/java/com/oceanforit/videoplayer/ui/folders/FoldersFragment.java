package com.oceanforit.videoplayer.ui.folders;

import static com.oceanforit.videoplayer.MainActivity.listFolders;
import static com.oceanforit.videoplayer.MainActivity.listVideos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oceanforit.videoplayer.R;
import com.oceanforit.videoplayer.adapter.FolderAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoldersFragment extends Fragment {

    @BindView(R.id._recycler_folders)
    RecyclerView _recyclerFolders;

    private FolderAdapter folderAdapter;

    private static FoldersFragment instance;

    public static FoldersFragment getInstance() {
        return instance == null ? new FoldersFragment() : instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_folders, container, false);
        ButterKnife.bind(this, layoutView);
        initUI();
        getFolders();
        return layoutView;
    }

    private void getFolders() {

        if (listFolders != null && listFolders.size() > 0) {

            folderAdapter = new FolderAdapter(getActivity(),
                    listFolders, listVideos);

            _recyclerFolders.setAdapter(folderAdapter);
        }
    }

    private void initUI() {

        _recyclerFolders.setHasFixedSize(true);
        _recyclerFolders.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getFolders();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_grid:
                _recyclerFolders.setHasFixedSize(true);
                _recyclerFolders.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                break;
            case R.id.action_Linear:
                _recyclerFolders.setHasFixedSize(true);
                _recyclerFolders.setLayoutManager(new LinearLayoutManager(getActivity()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
