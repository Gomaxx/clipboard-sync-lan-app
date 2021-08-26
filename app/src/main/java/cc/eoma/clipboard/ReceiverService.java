package cc.eoma.clipboard;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import cc.eoma.clipboard.MyApplication;
import cc.eoma.clipboard.synchronizer.ReceiverHandler;
import cc.eoma.clipboard.synchronizer.Synchronizer;

public class ReceiverService extends Service implements ReceiverHandler {

    private MyApplication application;
    private ClipboardManager clipboardManager;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = (MyApplication) getApplication();
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> Synchronizer.receive(application.getSyncType(), ReceiverService.this)).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void process(String message) {
        Synchronizer.addReceiveMessage(message);
        ClipData clipData = ClipData.newPlainText(null, message);
        clipboardManager.setPrimaryClip(clipData);
    }
}