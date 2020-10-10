package com.b726.audio_notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
// import android.graphics.Typeface;
import java.io.BufferedInputStream;
import java.io.IOException;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;

public class NotificationPanel {
    private Context parent;
    private NotificationManager nManager;
    private NotificationCompat.Builder nBuilder;
    private RemoteViews remoteView;
    private String cover;
    private String title;
    private String author;
    private boolean play;
    private boolean like;
    private ImageView imageView;
    
    // 网络获取图片
    private Bitmap getBitmapFromUrl(String urlString) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert is != null;
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public NotificationPanel(Context parent, String cover, String title, String author, boolean play, boolean like) {
        final String coverUrl = cover;
        this.parent = parent;
        this.cover = cover;
        this.title = title;
        this.author = author;
        this.play = play;
        this.like = like;

        // Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        nBuilder = new NotificationCompat.Builder(parent, "audio_notification")
                .setSmallIcon(R.drawable.music)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(this.play)
                .setOnlyAlertOnce(true)
                .setVibrate(new long[]{0L})
                .setSound(null);

        remoteView = new RemoteViews(parent.getPackageName(), R.layout.notificationlayout);

        remoteView.setTextViewText(R.id.title, title);
        remoteView.setTextViewText(R.id.author, author);
        nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);

        if (this.play) {
            remoteView.setImageViewResource(R.id.toggle, R.drawable.play);
        } else {
            remoteView.setImageViewResource(R.id.toggle, R.drawable.pause);
        }

        if (this.like) {
            remoteView.setImageViewResource(R.id.like, R.drawable.like);
        } else {
            remoteView.setImageViewResource(R.id.like, R.drawable.unlike);
        }

        setListeners(remoteView);
        // 普通通知栏高度
        // nBuilder.setContent(remoteView);
        // 使用setCustomBigContentView可设置更大通知栏高度
        nBuilder.setCustomBigContentView(remoteView);
        Notification notification = nBuilder.build();

        new Thread(new Runnable() {
			@Override
            public void run() {
                if (!coverUrl.contains("http"))
                    return;
                Bitmap bitmap = getBitmapFromUrl(coverUrl);
                if (bitmap != null) {
                    remoteView.setImageViewBitmap(R.id.audio_cover, bitmap);
                    nManager.notify(1, nBuilder.build());
                }
			}
		}).start();

        nManager.notify(1, notification);
    }

    public void setListeners(RemoteViews view) {
        Intent intent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("toggle")
                .putExtra("cover", this.cover)
                .putExtra("title", this.title)
                .putExtra("author", this.author)
                .putExtra("action", !this.play ? "play" : "pause")
                .putExtra("like", this.like ? "like" : "unlike");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(parent, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.toggle, pendingIntent);

        Intent likeIntent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("like")
                .putExtra("like", !this.like ? "like" : "unlike");
        PendingIntent pendingLikeIntent = PendingIntent.getBroadcast(parent, 0, likeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.like, pendingLikeIntent);

        Intent nextIntent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("next");
        PendingIntent pendingNextIntent = PendingIntent.getBroadcast(parent, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.next, pendingNextIntent);

        Intent prevIntent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("prev");
        PendingIntent pendingPrevIntent = PendingIntent.getBroadcast(parent, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.prev, pendingPrevIntent);

        Intent selectIntent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("select");
        PendingIntent selectPendingIntent = PendingIntent.getBroadcast(parent, 0, selectIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        view.setOnClickPendingIntent(R.id.layout, selectPendingIntent);
    }


    public void notificationCancel() {
        nManager.cancel(1);
    }
}

