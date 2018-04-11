package ru.belogurow.socialnetworkclient;

import android.app.Application;

import com.facebook.stetho.Stetho;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.belogurow.socialnetworkclient.di.AppComponent;
import ru.belogurow.socialnetworkclient.di.AppModule;
import ru.belogurow.socialnetworkclient.di.DaggerAppComponent;
import ru.belogurow.socialnetworkclient.di.DatabaseModule;
import ru.belogurow.socialnetworkclient.users.service.WebUserService;
import ru.belogurow.socialnetworkclient.web.SelfSigningClientBuilder;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public class App extends Application {

    public static final String BASE_URL = "https://10.0.2.2:8080";
//    public static final String BASE_URL = "https://192.168.1.64:8080";

    private static  AppComponent sComponent;
    public static WebUserService sWebUserService;

    @Override
    public void onCreate() {
        super.onCreate();

        initRetrofit();
        initDagger();
        initStetho();
    }

    public static AppComponent getComponent() {
        return sComponent;
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(SelfSigningClientBuilder.createClient(getApplicationContext()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sWebUserService = retrofit.create(WebUserService.class);
    }

    private void initDagger() {
        sComponent = DaggerAppComponent.builder()
                .databaseModule(new DatabaseModule(this))
                .appModule(new AppModule(this)).build();
    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }

}
