package cc.eoma.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import cc.eoma.clipboard.synchronizer.Synchronizer;

public class MainActivity extends AppCompatActivity {
    private Handler handler;
    private MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ClipboardService.class);
        startService(intent);


        Intent intentx = new Intent();
        intentx.setClass(MainActivity.this, ReceiverService.class);
        startService(intentx);

        handler = new Handler();
        application = (MyApplication) getApplication();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        ScreenBroadcastReceiver screenOnReceiver = new ScreenBroadcastReceiver();
        registerReceiver(screenOnReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(this::getClipboardContent, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(MainActivity.this, ClipboardService.class);
        stopService(intent);
    }

    private void getClipboardContent() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData == null || clipData.getItemCount() == 0) {
            return;
        }
        CharSequence text = clipData.getItemAt(0).getText();

        new Thread(() -> Synchronizer.send(application.getSyncType(), text.toString())).start();
    }
}