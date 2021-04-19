package com.example.dictionaryapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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


    public NotificationCompat.Builder setNotificationBuilder(String source, String translate) {
        return new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle(source)
                .setContentText(translate)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
    }




}