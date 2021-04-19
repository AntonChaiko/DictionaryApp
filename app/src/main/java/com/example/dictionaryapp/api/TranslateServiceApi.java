package com.example.dictionaryapp.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TranslateServiceApi {
    @GET("get")
    Call<TranslateData> getTranslate(
            @Query("q") String text,
            @Query("langpair") String lang
    );

}
