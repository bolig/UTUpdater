package cn.utsoft.update.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import cn.utsoft.cd.utupdater.UTLoadManager;
import cn.utsoft.cd.utupdater.event.UTUpdateCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String sUrl1 = "http://img02.tooopen.com/images/20141231/sy_78327074576.jpg";
    public static final String sUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1486635384733&di=59c1bedbc3d159feb09e8e67bd397b11&imgtype=0&src=http%3A%2F%2Fbbsimg.qianlong.com%2Fdata%2Fattachment%2Fforum%2F201409%2F30%2F105858f62a7uum6i446770.jpg";

    private ImageView iv;
    private ProgressBar pro;
    private ImageView ivOptions;
    private TextView tv;

    private void assignViews() {
        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.iv);
        pro = (ProgressBar) findViewById(R.id.pro);
        ivOptions = (ImageView) findViewById(R.id.iv_options);

        ivOptions.setOnClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
        load();
    }

    private void load() {

        UTLoadManager.load(this, sUrl, "下载图片", System.currentTimeMillis() + "", new UTUpdateCallback() {

            @Override
            public void onStart() {
                Log.e("MainActivity", "onStart");
                ivOptions.setSelected(!ivOptions.isSelected());
                pro.setProgress(0);
            }

            @Override
            public void onProgress(long current, long total) {
                final int progress = (int) (current * 100 / total);
                Log.e("MainActivity", "onProgress --- " + progress);
                pro.setProgress(progress);
                pro.invalidate();
                tv.setText(progress + "");
            }

            @Override
            public void onFinish(String path) {
                Log.e("MainActivity", "onFinish");
                Glide.with(MainActivity.this).load(path).into(iv);
            }

            @Override
            public void onPause() {

            }

            @Override
            public void onError(int code, String msg) {
                Log.e("MainActivity", "onProgress $$$ " + code + " $$$ " + msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
