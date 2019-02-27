package com.saiyanstudio.gamerack.retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.saiyanstudio.gamerack.common.Constants;

import java.io.IOException;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by deekshith on 16-11-2017.
 */

public class RetrofitClient {

    private static IGDBService igdbApiService = null;

    private Random random;
    private SharedPreferences prefs;

    public RetrofitClient(Context context)
    {
        random = new Random();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int apiCounter = prefs.getInt(Constants.SharedPrefTags.apiCounter,random.nextInt(10) + 1);

        // Incrementing the apiCounter so that the next api-key will be used next time
        apiCounter = (apiCounter + 1) % 10;
        if(apiCounter == 0) apiCounter = 10;

        prefs.edit().putInt(Constants.SharedPrefTags.apiCounter, apiCounter).apply();

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(new Interceptor() {
              @Override
              public Response intercept(Interceptor.Chain chain) throws IOException {
                  Request request = chain.request().newBuilder().addHeader("user-key", Constants.IGDBApi.UserKey).build();
                  return chain.proceed(request);
              }
          });

        OkHttpClient client = httpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.IGDBApi.baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        igdbApiService = retrofit.create(IGDBService.class);
    }

    public static  IGDBService getIgdbApiService(Context context)
    {
        if (igdbApiService == null)
            igdbApiService = new RetrofitClient(context).getIgdbApiService(context);
        return igdbApiService;
    }
}