package com.example.dictionaryapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MainFragment extends Fragment {

    private static final String CHANNEL_ID = "channelId";
    private SQLiteDatabase db;
    TranslateDatabaseHelper databaseHelper;
    NotificationManagerCompat notificationManager;
    private ResultAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_main, container, false);


        databaseHelper = new TranslateDatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        RecyclerView recyclerView = myView.findViewById(R.id.translate_recycler);
        adapter = new ResultAdapter(getActivity(), getAllData());

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        createNotificationChannel();
        notificationManager = NotificationManagerCompat.from(getActivity());
//        Cursor cursor = getAllData();
        getNotification(myView);

        return myView;
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


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "ANTON";
            String description = "best of the best";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    public void getNotification(View v) {

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getAllData().getCount() != 0) {

                    Cursor c = getAllData();
                    c.moveToPosition(0);
                    String source = c.getString(c.getColumnIndex("SOURCE"));
                    String translate = c.getString(c.getColumnIndex("TRANSLATE"));
                    notificationManager.notify(1, setNotificationBuilder(source, translate).build());
                    adapter.swapCursor(c);
                    handler.postDelayed(this, 5000);
                }


            }
        });
    }

    public NotificationCompat.Builder setNotificationBuilder(String source, String translate) {
        return new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle(source)
                .setContentText(translate)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
    }

}