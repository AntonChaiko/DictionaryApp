package com.example.dictionaryapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;


import com.example.dictionaryapp.model.TranslateData;
import com.example.dictionaryapp.model.TranslateServiceApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


import java.util.ArrayList;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

//    private EditText editText;
//    private SQLiteDatabase db;
//    private ArrayList<String> translates;
//    private ArrayList<String> sources;
//    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
//
//        SQLiteOpenHelper translateDatabaseHelper = new TranslateDatabaseHelper(this);
//        db = translateDatabaseHelper.getReadableDatabase();

        if(savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.frameLayout,new MainFragment()).commit();
        }
    }

//
//    public Call<TranslateData> getData(String string) {
//        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.mymemory.translated.net/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
//                .build();
//
//        TranslateServiceApi api = retrofit.create(TranslateServiceApi.class);
//        return api.getTranslate(string, "en|ru");
//    }
//
//    public void getResultOnClick(View view) {
//        editText = findViewById(R.id.editText);
//        String content = editText.getText().toString();
//
//
//        Call<TranslateData> call = getData(content);
//        call.enqueue(new Callback<TranslateData>() {
//
//            @Override
//            public void onResponse(Call<TranslateData> call, Response<TranslateData> response) {
//                ArrayList<TranslateData.Matches> p = new ArrayList(response.body().getMatches());
//
//                translates = new ArrayList<>();
//                sources = new ArrayList<>();
//
//                for (TranslateData.Matches matches : p) {
//                    translates.add(matches.getTranslation());
//                    sources.add(matches.getSegment());
//                }
//
//                ItemAdapter adapter = new ItemAdapter(context, sources, translates);
//                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.translate_recycler);
//                recyclerView.setAdapter(adapter);
//                GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
//                recyclerView.setLayoutManager(layoutManager);
//
//                editText.getText().clear();
//
//                adapter.setListener(new ItemAdapter.Listener() {
//                    @Override
//                    public void onClick(int position) {
//                        insertDatabase(position);
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFailure(Call<TranslateData> call, Throwable t) {
//
//            }
//
//        });
//
//    }
//
//    private void insertDatabase(int position) {
//        ContentValues cv = new ContentValues();
//        cv.put("SOURCE", sources.get(position));
//        cv.put("TRANSLATE", translates.get(position));
//        cv.put("FAVORITE", 1);
//        db.insert("DICTIONARY", null, cv);
//    }
//
//    public void dataBaseActivity(View view) {
//        Intent intent = new Intent(this, DatabaseActivity.class);
//        startActivity(intent);
//    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Intent intent = null;
        switch (id) {
            case R.id.page_1:
                fragment = new MainFragment();
                break;
            case R.id.page_2:
                fragment = new TranslateFragment();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.frameLayout, fragment);
            ft.commit();
        } else {
            startActivity(intent);
        }
        return true;
    }
}