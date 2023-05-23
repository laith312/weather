package com.example.weather.repository;

import android.util.Log;

import com.example.weather.WeatherApplication;
import com.example.weather.network.OpenWeatherMapAPI;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class is not a true Repository. I did this to save time. Normally we should cache
 * data in a DB using Room, however I didnt have time to add Room, so I made a network cache.
 * The Repo should be designed to check if DB data is stale before fetching from network.
 * <p>
 * This should be injected into the viewmodels
 */
public class OpenWeatherRepository {
    private static final long cacheSize = (3 * 1024 * 1024);
    private static final String BASE_URL = "https://api.openweathermap.org/";

    public static OpenWeatherMapAPI instance =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(buildOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(OpenWeatherMapAPI.class);

    private static OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder()
                .cache(buildCache())
                .addNetworkInterceptor(buildNetworkInterceptor())
                .build();
    }

    private static Cache buildCache() {
        return new Cache(new File(WeatherApplication.appContext.getCacheDir(), "id"), cacheSize);
    }

    private static Interceptor buildNetworkInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.d("RetrofitAPI", "INTERCEPTION!!!!!!!");
                Response response = chain.proceed(chain.request());
                CacheControl cacheControl =
                        new CacheControl.Builder().maxAge(15, TimeUnit.MINUTES).build();

                return response.newBuilder()
                        .removeHeader("Cache-Control")
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControl.toString())
                        .build();
            }
        };
    }
}
