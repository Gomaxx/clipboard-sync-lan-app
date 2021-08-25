package cc.eoma.clipboard;

import android.app.Application;

import cc.eoma.clipboard.synchronizer.SyncType;

public class MyApplication extends Application {

    private SyncType syncType = SyncType.Broadcast;


    public SyncType getSyncType() {
        return syncType;
    }

    public void setSyncType(SyncType syncType) {
        this.syncType = syncType;
    }

}
