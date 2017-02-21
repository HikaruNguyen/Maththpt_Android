package com.app.maththpt.config;

import android.content.Context;

import com.app.maththpt.BuildConfig;
import com.app.maththpt.modelresult.BaseResult;
import com.app.maththpt.modelresult.DetailTestsResult;
import com.app.maththpt.modelresult.LoginResult;
import com.app.maththpt.modelresult.TestsResult;
import com.app.maththpt.network.NullOnEmptyConverterFactory;
import com.app.maththpt.network.RxErrorHandlingCallAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 16/02/2017.
 */

public interface MathThptService {
    @GET("content/get-test.php")
    Observable<TestsResult> getTests();

    @GET("content/get-content.php")
    Observable<DetailTestsResult> getContentbyTestID(@Query("type") int type,
                                                     @Query("testID") String testID,
                                                     @Query("page") int page);

    @GET("content/get-content.php")
    Observable<DetailTestsResult> getContentbyCateID(@Query("type") int type,
                                                     @Query("cateID") int testID,
                                                     @Query("page") int page);

    @GET("content/exam.php")
    Observable<DetailTestsResult> getExamByCateID(@Query("cateIDs") String cateIDs,
                                                  @Query("number") int number);

    @FormUrlEncoded
    @POST("user/login.php")
    Observable<LoginResult> postLogin(@Field("username") String username,
                                      @Field("password") String password);

    @FormUrlEncoded
    @POST("user/register.php")
    Observable<BaseResult> postRegister(@Field("username") String username,
                                        @Field("password") String password,
                                        @Field("fullname") String fullname,
                                        @Field("email") String email,
                                        @Field("avatar") String avatar);

    class Factory {

        public static MathThptService create(Context context) {
            // Create Retrofit Adapter
            Gson gson = new GsonBuilder().serializeNulls().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(APIConfig.DOMAIN_HOST)
                    .client(getOkHttp(context))
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .build();
            return retrofit.create(MathThptService.class);
        }

        public static OkHttpClient getOkHttp(Context context) {
            // Config Log
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Create Http Client
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
//                        .addHeader("Accept", "application/json")
                        .addHeader("X-Math-Api-Key", APIConfig.X_Math_Api_Key).build();
                return chain.proceed(request).newBuilder().build();
            });
            httpClient.connectTimeout(60L, TimeUnit.SECONDS);
            httpClient.readTimeout(60L, TimeUnit.SECONDS);
            httpClient.writeTimeout(60L, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(logging);
            }

            // Config Http Cache
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(context.getCacheDir(), cacheSize);
            httpClient.cache(cache);

            return httpClient.build();
        }
    }
}
