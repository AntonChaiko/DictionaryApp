package com.example.dictionaryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    public static final String CHANNEL_ID = "channelId";
    private Context context;
    private Cursor cursor;


    public ResultAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_result, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!cursor.moveToNext()) {
            return;
        }

        CardView cardView = holder.cardView;

        TextView textViewTranslate = cardView.findViewById(R.id.translated_text);
        TextView textViewSource = cardView.findViewById(R.id.source_text);

        String source = cursor.getString(cursor.getColumnIndex("SOURCE"));
        String translate = cursor.getString(cursor.getColumnIndex("TRANSLATE"));
        long id = cursor.getLong(cursor.getColumnIndex("_id"));

        holder.itemView.setTag(id);

        textViewSource.setText(source);
        textViewTranslate.setText(translate);

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }
}
