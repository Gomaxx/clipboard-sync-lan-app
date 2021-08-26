package cc.eoma.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

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

        handler = new Handler();
        application = (MyApplication) getApplication();
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
        Synchronizer.send(application.getSyncType(), text.toString());
    }
}