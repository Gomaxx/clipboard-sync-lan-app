package cc.eoma.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cc.eoma.clipboard.synchronizer.SyncType;
import cc.eoma.clipboard.synchronizer.Synchronizer;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ClipboardService.class);
        startService(intent);

        handler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(() -> getClipboardContent(), 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(MainActivity.this, ClipboardService.class);
        stopService(intent);
    }

    private void getClipboardContent() {
        MyApplication application = (MyApplication) getApplication();
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData == null || clipData.getItemCount() == 0) {
            return;
        }
//        new Thread(() -> {
            CharSequence text = clipData.getItemAt(0).getText();
            Synchronizer.send(application.getSyncType(), text.toString());
//        }).start();
    }
}