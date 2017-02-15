package cn.utsoft.update.simple;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.utsoft.cd.utupdater.UTUpdaterManager;
import cn.utsoft.update.simple.adapter.DownloadAdapter;
import cn.utsoft.update.simple.entity.UpdaterEntity;
import cn.utsoft.update.simple.test.TestDate;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvDownloadList;
    private ArrayList<UpdaterEntity> mDownloadList;
    private DownloadAdapter mDownloadAdapter;
    private TextView tvDelete;

    private void initViews() {
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        rvDownloadList = (RecyclerView) findViewById(R.id.rv_download_list);
        rvDownloadList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mDownloadAdapter = new DownloadAdapter(this, mDownloadList);
        rvDownloadList.setAdapter(mDownloadAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initDate();
        initViews();

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog_AppCompat_DEV)
                        .setTitle("提示")
                        .setMessage("是否要清空下载记录?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                UTUpdaterManager.clearDownloadHistory(MainActivity.this);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
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
