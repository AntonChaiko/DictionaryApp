package com.example.dictionaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class DatabaseActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "channelId";
    private SQLiteDatabase db;
    TranslateDatabaseHelper databaseHelper;
    NotificationManagerCompat notificationManager;
    private ResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);


        databaseHelper = new TranslateDatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();

        RecyclerView recyclerView = findViewById(R.id.translate_recycler);
        adapter = new ResultAdapter(this, getAllData());
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        createNotificationChannel();
        notificationManager = NotificationManagerCompat.from(this);

    }

    public Cursor getAllData() {
        return db.rawQuery("select * from " + "DICTIONARY", null);
    }

    public void removeItem(long id) {
        db.delete("DICTIONARY", "_id" + " = " + id, null);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            removeItem((long) viewHolder.itemView.getTag());
            adapter.swapCursor(getAllData());
        }
    };

    public NotificationCompat.Builder setNotificationBuilder(String source, String translate) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle(source)
                .setContentText(translate)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "ANTON";
            String description = "best of the best";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    public void getNotification(View v) {
        Cursor c = getAllData();
        c.moveToPosition(0);
        String source = c.getString(c.getColumnIndex("SOURCE"));
        String translate = c.getString(c.getColumnIndex("TRANSLATE"));
        Handler handler = new Handler();
//        int mins = 1000 * 60 * 30;
        handler.post(new Runnable() {
            @Override
            public void run() {
                notificationManager.notify(1, setNotificationBuilder(source, translate).build());
//                handler.postDelayed(this, 5000);
            }
        });

    }
}