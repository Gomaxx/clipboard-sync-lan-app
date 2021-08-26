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
        this.ensureMyNotificationListenerServiceRunning();
    }

    //确认 ClipboardService 是否开启
    private void ensureMyNotificationListenerServiceRunning() {
        ComponentName componentName = new ComponentName(this, ClipboardService.class);
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = manager.getRunningServices(Integer.MAX_VALUE);
        if (services == null || services.isEmpty()) {
            this.toggleNotificationListenerService();
            return;
        }

        for (ActivityManager.RunningServiceInfo service : services) {
            if (service.service.equals(componentName)) {
                if (service.pid == Process.myPid()) {
                    return;
                }
            }
        }
        toggleNotificationListenerService();
    }

    //重新开启 ClipboardService
    private void toggleNotificationListenerService() {
        ComponentName componentName = new ComponentName(this, ClipboardService.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }


    public SyncType getSyncType() {
        return syncType;
    }

    public void setSyncType(SyncType syncType) {
        this.syncType = syncType;
    }

}
