package cc.eoma.clipboard;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;

import java.util.List;

import cc.eoma.clipboard.synchronizer.SyncType;

public class MyApplication extends Application {

    private SyncType syncType = SyncType.Broadcast;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public SyncType getSyncType() {
        return syncType;
    }

    public void setSyncType(SyncType syncType) {
        this.syncType = syncType;
    }

}
