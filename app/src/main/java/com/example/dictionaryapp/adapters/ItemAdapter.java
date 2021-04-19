package com.example.dictionaryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionaryapp.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private Listener listener;
    private ArrayList<String> sources;
    private ArrayList<String> translates;

    public interface Listener {
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public ItemAdapter(Context context, ArrayList<String> sources, ArrayList<String> translates) {
        this.sources = sources;
        this.translates = translates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_translate, parent, false);
        return new ViewHolder(cv);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView textViewTranslate = cardView.findViewById(R.id.translate_text_view);
        textViewTranslate.append(" " + translates.get(position));
        TextView textViewSource = cardView.findViewById(R.id.source_text_view);
        textViewSource.append(" " + sources.get(position));

        CheckBox cb = cardView.findViewById(R.id.favorite);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(@NonNull CardView itemView) {
            super(itemView);
            cardView = itemView;

        }
    }
}