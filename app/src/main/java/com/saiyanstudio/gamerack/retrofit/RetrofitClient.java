package com.saiyanstudio.gamerack.retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.saiyanstudio.gamerack.common.Constants;

import java.io.IOException;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by deekshith on 16-11-2017.
 */

public class RetrofitClient {

    // default values
    private static String BASE_URL = "https://api-2445582011268.apicast.io";
    private static String USER_KEY = "b43255555bc23a4bd6869670770936d4";

    private static String ACCEPT = "application/json";
    private static IGDBService igdbApiService = null;

    private Random random;
    private SharedPreferences prefs;

    public RetrofitClient(Context context)
    {
        random = new Random();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int apiCounter = prefs.getInt(Constants.SharedPrefTags.apiCounter,random.nextInt(10) + 1);

        getRandomIGDBApiKey(apiCounter);

        // Incrementing the apiCounter so that the next api-key will be used next time
        apiCounter = (apiCounter + 1) % 10;
        if(apiCounter == 0) apiCounter = 10;

        prefs.edit().putInt(Constants.SharedPrefTags.apiCounter, apiCounter).apply();

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(new Interceptor() {
              @Override
              public Response intercept(Interceptor.Chain chain) throws IOException {
                  Request request = chain.request().newBuilder().addHeader("user-key", USER_KEY).build();
                  return chain.proceed(request);
              }
          });

        OkHttpClient client = httpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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

    private void getRandomIGDBApiKey(int index) {
        switch(index) {
            case 1:
                // game.1@sharklasers.com, Game@12345
                USER_KEY = "d74132e5c3c6d6981c898e99f6d9eaff";
                break;

            case 2:
                // game.2@sharklasers.com, Game@12345
                USER_KEY = "b2a126cf166b5593ad783efd19339a62";
                break;

            case 3:
                // game.3@sharklasers.com, Game@12345
                USER_KEY = "c8b92415dd6a3bb7a65600b833c7ae8a";
                break;

            case 4:
                // game.4@sharklasers.com, Game@12345
                USER_KEY = "40c486e67f43023bd562daf1b464bdbe";
                break;

            case 5:
                // game.5@sharklasers.com, Game@12345
                USER_KEY = "4add94ab754fb2946870db078238b38e";
                break;

            case 6:
                // game.6@sharklasers.com, Game@12345
                USER_KEY = "9cb3ad2ab7c7f1a5f2f41ac799f01355";
                break;

            case 7:
                // game.7@sharklasers.com, Game@12345
                USER_KEY = "39a1cda1a04fae5a643817f706e65a5b";
                break;

            case 8:
                // game.8@sharklasers.com, Game@12345
                USER_KEY = "328cec2108985013afc30626918e21a1";
                break;

            case 9:
                // game.9@sharklasers.com, Game@12345
                USER_KEY = "31ca8f5cbe772cabeefa3867e2678db6";
                break;

            case 10:
                // game.10@sharklasers.com, Game@12345
                USER_KEY = "f3085c51f1b5dd7e1ae524537f24143e";
                break;
        }
    }
}