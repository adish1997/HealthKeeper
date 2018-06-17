package com.example.android.healthkeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Reminder extends AppCompatActivity {

    public static String goal = "goal";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        String reminder = "Your goal is pending";
        TextView reminder_TextView = findViewById(R.id.reminder_TextView);
        reminder_TextView.setText(reminder);
    }
}
