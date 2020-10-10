package com.b726.audio_notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class NotificationReturnSlot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case "prev":
                AudioNotificationPlugin.callEvent("prev");
                break;
            case "next":
                AudioNotificationPlugin.callEvent("next");
                break;
            case "toggle":
                String cover = intent.getStringExtra("cover");
                String title = intent.getStringExtra("title");
                String author = intent.getStringExtra("author");
                String action = intent.getStringExtra("action");
                String like = intent.getStringExtra("like");
                AudioNotificationPlugin.show(cover, title, author, action.equals("play"), like.equals("like"));
                AudioNotificationPlugin.callEvent(action);
                break;
            case "like":
                String likeToggle = intent.getStringExtra("like");
                AudioNotificationPlugin.callEvent(likeToggle);
                break;
            case "select":
                Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(closeDialog);
                String packageName = context.getPackageName();
                PackageManager pm = context.getPackageManager();
                Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
                context.startActivity(launchIntent);

                AudioNotificationPlugin.callEvent("select");
        }
    }
}

