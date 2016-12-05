package com.shawn.shawnannlibrary;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shawnann.emoji.EmojiUtils;
import com.shawnann.enviews.ENDownloadView;
import com.shawnann.enviews.ENLoadingView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ENDownloadView enDownloadView = (ENDownloadView) findViewById(R.id.enDownloadView);
        enDownloadView.setDownloadConfig(20000, 12345678, ENDownloadView.DownloadUnit.KB);
        enDownloadView.setOnDownloadStateListener(new ENDownloadView.OnDownloadStateListener() {
            @Override
            public void onDownloadFinish() {

            }

            @Override
            public void onResetFinish() {

            }
        });
        enDownloadView.start();

        ENLoadingView enLoadingView = (ENLoadingView) findViewById(R.id.enLoadingView);
        enLoadingView.show();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                PlayActivity.actionStart(MainActivity.this, PlayActivity.class, null);
                finish();
            }
        }, 2000);*/
    }
}
