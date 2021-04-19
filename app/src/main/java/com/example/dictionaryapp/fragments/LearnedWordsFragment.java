package com.example.dictionaryapp.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dictionaryapp.R;
import com.example.dictionaryapp.TranslateDatabaseHelper;
import com.example.dictionaryapp.adapters.ResultAdapter;


public class LearnedWordsFragment extends Fragment {

    private ResultAdapter adapter;
    private SQLiteDatabase db;
    TranslateDatabaseHelper databaseHelper;

    public LearnedWordsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_learned_words, container, false);

        databaseHelper = new TranslateDatabaseHelper(getActivity());
        db = databaseHelper.getReadableDatabase();

        RecyclerView recyclerView = myView.findViewById(R.id.learned_recycler);
        adapter = new ResultAdapter(getActivity(), getAllData());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        return myView;
    }

    public Cursor getAllData() {
        return db.rawQuery("select * from " + "LEARNED", null);
    }

    public void removeItem(long id) {
        db.delete("LEARNED", "_id" + " = " + id, null);
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

}