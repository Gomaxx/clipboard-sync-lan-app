package cc.eoma.clipboard;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Process;

import java.util.List;

public class ScreenBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName componentName = new ComponentName(context, ReceiverService.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        this.ensureMyNotificationListenerServiceRunning(context);
    }

    //确认 MyNotificationListenerService 是否开启
    private void ensureMyNotificationListenerServiceRunning(Context context) {
        ComponentName componentName = new ComponentName(context, ReceiverService.class);
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = manager.getRunningServices(Integer.MAX_VALUE);
        if (services == null || services.isEmpty()) {
            this.toggleNotificationListenerService(context);
            return;
        }

        for (ActivityManager.RunningServiceInfo service : services) {
            if (service.service.equals(componentName)) {
                if (service.pid == Process.myPid()) {
                    return;
                }
            }
        }
        toggleNotificationListenerService(context);
    }

    //重新开启 MyNotificationListenerService
    private void toggleNotificationListenerService(Context context) {
        ComponentName componentName = new ComponentName(context, ReceiverService.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

}

