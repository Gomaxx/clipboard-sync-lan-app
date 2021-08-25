package cc.eoma.clipboard.synchronizer.receiver;

import android.content.ClipData;
import android.content.ClipboardManager;

import cc.eoma.clipboard.synchronizer.Synchronizer;

public class MyHandler {
    private ClipboardManager clipboardManager;

    public MyHandler(ClipboardManager clipboardManager) {
        this.clipboardManager = clipboardManager;
    }

    public void process(byte[] bytes) {
        String message = new String(bytes, 0, bytes.length);
        System.out.println("-----------receive:" + message);
        Synchronizer.addReceiveMessage(message);
        ClipData clipData = ClipData.newPlainText(null, message);
        clipboardManager.setPrimaryClip(clipData);
    }
}
