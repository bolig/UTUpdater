package cn.utsoft.update.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageActivity extends AppCompatActivity {
    private ImageView ivImg;

    public static void start(Activity act, String path) {
        Intent intent = new Intent(act, ImageActivity.class);
        intent.putExtra("path", path);
        act.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ivImg = (ImageView) findViewById(R.id.iv_Img);

        String path = getIntent().getStringExtra("path");

        Glide.with(this).load(path).into(ivImg);
    }
}
