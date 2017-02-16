package cn.utsoft.update.simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import cn.utsoft.update.simple.adapter.DownloadAdapter;
import cn.utsoft.update.simple.entity.UpdaterEntity;
import cn.utsoft.update.simple.test.TestDate;

public class DownloadListActivity extends AppCompatActivity {

    private RecyclerView rvDownloadList;
    private ArrayList<UpdaterEntity> mDownloadList;
    private DownloadAdapter mDownloadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_list);
        initDate();
        initViews();
    }

    private void initViews() {
        rvDownloadList = (RecyclerView) findViewById(R.id.rv_download_list);
        rvDownloadList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mDownloadAdapter = new DownloadAdapter(this, mDownloadList);
        rvDownloadList.setAdapter(mDownloadAdapter);
    }

    private void initDate() {
        mDownloadList = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            String url = TestDate.APK_URL;
            UpdaterEntity entity = new UpdaterEntity("updater_" + i, url);
            entity.version = i;
            entity.versionName = "version_" + i;
            entity.name = "name_" + i;
            entity.isAPK = true;
            mDownloadList.add(entity);
        }
    }
}
