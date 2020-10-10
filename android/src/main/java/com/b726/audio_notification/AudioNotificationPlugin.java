package com.b726.audio_notification;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class AudioNotificationPlugin implements MethodCallHandler {
    private static final String CHANNEL_ID = "audio_notification";
    private static Registrar registrar;
    private static NotificationPanel nPanel;
    private static MethodChannel channel;

    private AudioNotificationPlugin(Registrar r) {
        registrar = r;
    }

    public static void registerWith(Registrar registrar) {
      AudioNotificationPlugin plugin = new AudioNotificationPlugin(registrar);

      AudioNotificationPlugin.channel = new MethodChannel(registrar.messenger(), "audio_notification");
      AudioNotificationPlugin.channel.setMethodCallHandler(new AudioNotificationPlugin(registrar));
    }

 @Override
 public void onMethodCall(MethodCall call, Result result) {
      switch (call.method) {
          case "show":
              final String cover = call.argument("cover");
              final String title = call.argument("title");
              final String author = call.argument("author");
              final boolean play = call.argument("play");
              final boolean like = call.argument("like");
              show(cover, title, author, play, like);
              result.success(null);
              break;
            case "hide":
              hide();
              result.success(null);
              break;
          default:
              result.notImplemented();
      }
  }

  public static void callEvent(String event) {
      AudioNotificationPlugin.channel.invokeMethod(event, null, new Result() {
          @Override
          public void success(Object o) {
          }
          @Override
          public void error(String s, String s1, Object o) {}

          @Override
          public void notImplemented() {}
      });
  }

  public static void show(String cover, String title, String author, boolean play, boolean like) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          int importance = NotificationManager.IMPORTANCE_DEFAULT;
          NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
          channel.enableVibration(false);
          channel.setSound(null, null);
          NotificationManager notificationManager = registrar.context().getSystemService(NotificationManager.class);
          notificationManager.createNotificationChannel(channel);
      }

      nPanel = new NotificationPanel(registrar.context(), cover, title, author, play, like);
  }

  private void hide() {
      nPanel.notificationCancel();
  }
}




