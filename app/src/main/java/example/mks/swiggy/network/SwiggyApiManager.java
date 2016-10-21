package example.mks.swiggy.network;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import example.mks.swiggy.SwiggyApplication;
import example.mks.swiggy.utility.CommonUtilities;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mahesh on 19/10/16.
 */

public class SwiggyApiManager {
    // https://api.myjson.com/bins/ngcc
    private static SwiggyApiService API_MANAGER;
    private static long SIZE_OF_CACHE = 100 * 1024 * 1024;

    public static void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.myjson.com/")
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API_MANAGER = retrofit.create(SwiggyApiService.class);
    }

    public static SwiggyApiService getApiManager() {
        if (API_MANAGER == null) {
            init();
        }
        return API_MANAGER;
    }


    private static final Interceptor OFFLINE_CACHE_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            // max-age = 86400 = 1 day and max-stale=2419200 = 4 weeks
            String cacheHeaderValue = CommonUtilities.isConnectedToInternet(SwiggyApplication.context)
                    ? "public, max-age=86400"
                    : "public, only-if-cached, max-stale=2419200";
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", cacheHeaderValue)
                    .build();
        }
    };

    private static OkHttpClient provideOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(provideCache())
                .addNetworkInterceptor(OFFLINE_CACHE_INTERCEPTOR)
                .build();
        return okHttpClient;
    }

    private static Cache provideCache() {
        Cache cache = null;
        try {
            if (cache == null)
                cache = new Cache(new File(SwiggyApplication.context.getCacheDir(), "mg-cache"), SIZE_OF_CACHE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Cache Error", "Could not create cache");
        }
        return cache;
    }
}
