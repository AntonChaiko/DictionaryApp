package com.example.dictionaryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dictionaryapp.model.TranslateData;
import com.example.dictionaryapp.model.TranslateServiceApi;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TranslateFragment extends Fragment implements View.OnClickListener {
    private SQLiteDatabase db;
    private ArrayList<String> translates;
    private ArrayList<String> sources;
    private EditText editText;
    Context context;

    public TranslateFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_translate, container, false);

        SQLiteOpenHelper translateDatabaseHelper = new TranslateDatabaseHelper(getActivity());
        db = translateDatabaseHelper.getReadableDatabase();

        Button button = myView.findViewById(R.id.button);
        button.setOnClickListener(this);


        return myView;

    }

    public Call<TranslateData> getData(String string) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mymemory.translated.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        TranslateServiceApi api = retrofit.create(TranslateServiceApi.class);
        return api.getTranslate(string, "en|ru");
    }

    @Override
    public void onClick(View view) {
        editText = getView().findViewById(R.id.editText);
        String content = editText.getText().toString();


        Call<TranslateData> call = getData(content);
        call.enqueue(new Callback<TranslateData>() {

            @Override
            public void onResponse(Call<TranslateData> call, Response<TranslateData> response) {
                ArrayList<TranslateData.Matches> p = new ArrayList(response.body().getMatches());

                translates = new ArrayList<>();
                sources = new ArrayList<>();

                for (TranslateData.Matches matches : p) {
                    translates.add(matches.getTranslation());
                    sources.add(matches.getSegment());
                }

                ItemAdapter adapter = new ItemAdapter(context, sources, translates);
                RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.translate_recycler);
                recyclerView.setAdapter(adapter);
                GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
                recyclerView.setLayoutManager(layoutManager);

                editText.getText().clear();

                adapter.setListener(new ItemAdapter.Listener() {
                    @Override
                    public void onClick(int position) {

                        insertDatabase(position);
                    }
                });

            }

            @Override
            public void onFailure(Call<TranslateData> call, Throwable t) {

            }

        });

    }

    private void insertDatabase(int position) {
        ContentValues cv = new ContentValues();
        cv.put("SOURCE", sources.get(position));
        cv.put("TRANSLATE", translates.get(position));
        cv.put("FAVORITE", 1);
        db.insert("DICTIONARY", null, cv);
    }
}