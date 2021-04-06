package com.example.dictionaryapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import com.example.dictionaryapp.model.TranslateData;
import com.example.dictionaryapp.model.TranslateServiceApi;


import java.util.ArrayList;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private SQLiteDatabase db;
    private ArrayList<String> translates;
    private ArrayList<String> sources;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteOpenHelper translateDatabaseHelper = new TranslateDatabaseHelper(this);
        db = translateDatabaseHelper.getReadableDatabase();


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

    public void getResultOnClick(View view) {
        editText = findViewById(R.id.editText);
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
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.translate_recycler);
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

    public void dataBaseActivity(View view) {
        Intent intent = new Intent(this, DatabaseActivity.class);
        startActivity(intent);
    }

}