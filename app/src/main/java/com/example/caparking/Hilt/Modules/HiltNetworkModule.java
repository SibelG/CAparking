package com.example.caparking.Hilt.Modules;

import com.example.caparking.MainRepository;
import com.example.caparking.Remote.IGoogleAPIService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class HiltNetworkModule {

    private static Retrofit retrofit = null;
    @Provides
    @Singleton
    Retrofit ProvideRetrofit() {
        String BASE_URL = "https://maps.googleapis.com/";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS).build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    IGoogleAPIService ProvideRequestApi(Retrofit retrofit) {
        return retrofit.create(IGoogleAPIService.class);
    }

    @Provides
    @Singleton
    MainRepository ProvideMainRepository(IGoogleAPIService requestApi) {
        return new MainRepository(requestApi);
    }
}
