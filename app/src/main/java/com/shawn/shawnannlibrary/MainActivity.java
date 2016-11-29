package com.shawn.shawnannlibrary;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shawnann.emoji.EmojiUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                PlayActivity.actionStart(MainActivity.this, PlayActivity.class, null);
                finish();
            }
        }, 2000);
    }
}
