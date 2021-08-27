package cc.eoma.clipboard;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;

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
        this.createHasDataNotification(getApplicationContext(), message);
        Synchronizer.addReceiveMessage(message);
        ClipData clipData = ClipData.newPlainText(null, message);
        clipboardManager.setPrimaryClip(clipData);
    }

    public static void createHasDataNotification(Context context, String content) {
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("11", "11", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            Notification notification = new Notification.Builder(context, "11")
                    .setContentText(content)//设置通知内容
                    .setAutoCancel(true)//设置点击通知后自动删除通知
                    .setContentIntent(pendingIntent) //设置点击通知时的响应事件
                    .setSmallIcon(R.mipmap.ic_launcher)//通知左侧的小图标
                    .build();
            manager.createNotificationChannel(channel);
            manager.notify(11, notification);
        } else {
            Notification notification = new Notification.Builder(context)
                    .setContentText(content)//设置通知内容
                    .setAutoCancel(true)//设置点击通知后自动删除通知
                    .setContentIntent(pendingIntent) //设置点击通知时的响应事件
                    .setSmallIcon(R.mipmap.ic_launcher)//通知左侧的小图标
                    .setDefaults(Notification.DEFAULT_SOUND)//使用系统默认的声音或震动
                    .build();
            manager.notify(11, notification);
        }
    }
}