package cn.utsoft.update.simple;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.utsoft.cd.utupdater.UTUpdaterManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvDelete;

    private void initViews() {
        tvDelete = (TextView) findViewById(R.id.tv_delete);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete:
                clearHistory();
                break;
            case R.id.tv_updater:
                checkUpdater();
                break;
            case R.id.tv_download_list:
                downloadList();
                break;
        }
    }

    private void downloadList() {
        Intent intent = new Intent(this, DownloadListActivity.class);
        startActivity(intent);
    }

    private void checkUpdater() {
        Toast.makeText(this, "检查更新", Toast.LENGTH_SHORT).show();
    }

    private void clearHistory() {
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

    /**
     * 通知栏下载
     *
     * @param view
     */
    public void toDownloadOnNotification(View view){

    }

    /**
     * 弹窗下载
     *
     * @param view
     */
    public void toDownloadOnDialog(View view){

    }
}
