package com.example.android.healthkeeper;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.icu.util.TimeUnit;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.R.attr.delay;

public class MainActivity extends AppCompatActivity {

    String goal = "Not Specified yet";
    double frequency = 1;
    long duration = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button set_Button = findViewById(R.id.set_Button);

        set_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText goal_EditText = findViewById(R.id.goal_EditText);
                goal = String.valueOf(goal_EditText.getText());

                EditText frequency_EditText = findViewById(R.id.frequency_EditText);
                frequency = Double.parseDouble(String.valueOf(frequency_EditText.getText()));

                EditText duration_EditText = findViewById(R.id.duration_EditText);
                duration =(long) Integer.parseInt(String.valueOf(duration_EditText.getText()))*60*6000 + SystemClock.elapsedRealtime();
                showNotification(MainActivity.this, frequency, 0);

            }
        });



    }

    public void showNotification(Context context, double frequency, int notificationId) {


      NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));

        mBuilder.setContentTitle("My Health Goal");
        mBuilder.setContentText(goal);
        mBuilder.setAutoCancel(true);

        Intent reminderIntent = new Intent(this, Reminder.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Reminder.class);
        stackBuilder.addNextIntent(reminderIntent);

       /* PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mBuilder.build());*/

        PendingIntent activity = PendingIntent.getActivity(context, notificationId, reminderIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(activity);

        Notification notification = mBuilder.build();

        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        long futureInMillis = (long) (frequency * 3600000);
        Toast.makeText(this, ""+futureInMillis, Toast.LENGTH_SHORT).show();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() ,futureInMillis, pendingIntent);

        if(SystemClock.elapsedRealtime()>= duration){

            alarmManager.cancel(pendingIntent);
        }

    }
}