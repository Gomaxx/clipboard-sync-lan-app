package cc.eoma.clipboard;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import cc.eoma.clipboard.MyApplication;
import cc.eoma.clipboard.synchronizer.Synchronizer;

public class ClipboardService extends Service {
    MyApplication application;
    ClipboardManager clipboardManager;
    ClipboardManager.OnPrimaryClipChangedListener primaryClipChangedListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = (MyApplication) getApplication();
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        primaryClipChangedListener = () -> {
            if (clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClip().getItemCount() > 0) {
                CharSequence content = clipboardManager.getPrimaryClip().getItemAt(0).getText();
                sender(content.toString());
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        clipboardManager.addPrimaryClipChangedListener(primaryClipChangedListener);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clipboardManager.removePrimaryClipChangedListener(primaryClipChangedListener);
    }

    private void sender(String content) {
        new Thread(() -> Synchronizer.send(application.getSyncType(), content)).start();
    }
}
