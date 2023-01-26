package com.example.notify;

import static com.example.notify.App.CHANNEL_1_ID;
import static com.example.notify.App.CHANNEL_2_ID;
import static com.example.notify.App.CHANNEL_3_ID;
import static com.example.notify.App.CHANNEL_4_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;

    static List<com.example.notify.Message> MESSAGES = new ArrayList<com.example.notify.Message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.purple_500));

        notificationManager = NotificationManagerCompat.from(this);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextMessage = findViewById(R.id.edit_text_message);

        MediaSessionCompat mediaSession = new MediaSessionCompat(this, "tag");

        //MESSAGES.add(new Message("Good Morning", "Diana"));
        MESSAGES.add(new com.example.notify.Message("Good Morning", "Diana"));
        MESSAGES.add(new com.example.notify.Message("Hello", "Me"));
        MESSAGES.add(new com.example.notify.Message("Good morning too!", "Avery"));
    }

    public void sendOnChannel1(View v) {
        sendChannel1Notification(this);
    }

    public static void sendChannel1Notification(Context context) {
        //String title = editTextTitle.getText().toString();
        //String message = editTextMessage.getText().toString();

        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

        RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply")
                .setLabel("Your Answer..")
                .build();

        Intent replyIntent = new Intent(context, DirectReplyReceiver.class);
        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(context, 0, replyIntent, 0);

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_send,
                "Reply",
                replyPendingIntent
        ).addRemoteInput(remoteInput).build();

        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle("Me");
        //messagingStyle.setConversationTitle("Group Chat");

        for (com.example.notify.Message chatMessage : MESSAGES) {
            NotificationCompat.MessagingStyle.Message notificationMessage = new NotificationCompat.MessagingStyle.Message(
                    chatMessage.getText(),
                    chatMessage.getTimeStamp(),
                    chatMessage.getSender()
            );
            messagingStyle.addMessage(notificationMessage);
        }

        //Bitmap LargeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.contacts);

        android.app.Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                //.setContentTitle(title)
                //.setContentText(message)
                //.setLargeIcon(LargeIcon)
                .setStyle(messagingStyle)
                .addAction(replyAction)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(ContextCompat.getColor(context, R.color.purple_500))
                //.setColor(Color.GREEN)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, notification);
    }

    @SuppressLint("MissingPermission")
    public void sendOnChannel2(View v) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        /*Intent broadcastIntent = new Intent(this,NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
         */

        Bitmap LargeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.contacts);
        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.car);

        android.app.Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_two)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(LargeIcon)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(picture)
                        .bigLargeIcon(null))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setColor(ContextCompat.getColor(MainActivity.this, R.color.purple_500))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificationManager.notify(2, notification);
    }

    @SuppressLint("MissingPermission")
    public void sendOnChannel3(View v) {

        String artist = editTextTitle.getText().toString();
        String song = editTextMessage.getText().toString();

        Bitmap MusicCover = BitmapFactory.decodeResource(getResources(), R.drawable.music);

        android.app.Notification notification = new NotificationCompat.Builder(this, CHANNEL_3_ID)
                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle(artist)
                .setContentText(song)
                .setLargeIcon(MusicCover)
                .addAction(R.drawable.ic_dislike, "Dislike", null)
                .addAction(R.drawable.ic_skip_previous, "Previous", null)
                .addAction(R.drawable.ic_pause, "Pause", null)
                .addAction(R.drawable.ic_skip_next, "Next", null)
                .addAction(R.drawable.ic_like, "Like", null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                .setShowActionsInCompactView(1, 2, 3)
                        /*.setMediaSession(mediaSession.getSessionToken())*/)
                .setSubText("Dedication 4")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
        notificationManager.notify(3, notification);
    }

    @SuppressLint("MissingPermission")
    public void downloadButton(View v) {

        final int max = 100;
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_4_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Download")
                .setContentText("Download in progress")
                .setColor(Color.GREEN)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress(max,0,false);
        notificationManager.notify(4, notification.build());

        new Thread(new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                SystemClock.sleep(2000);
                for (int progress = 0; progress <= max; progress += 10) {
                    notification.setProgress(max,progress,false);
                    notificationManager.notify(4,notification.build());
                    SystemClock.sleep(1000);
                }
                notification.setContentText("Download finised")
                        .setProgress(0,0,false)
                        .setOngoing(false);
                notificationManager.notify(4,notification.build());
            }
        }).start();
    }

}