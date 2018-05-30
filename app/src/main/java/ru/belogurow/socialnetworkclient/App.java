package ru.belogurow.socialnetworkclient;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.mikepenz.iconics.context.IconicsContextWrapper;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.belogurow.socialnetworkclient.chat.service.ChatWebService;
import ru.belogurow.socialnetworkclient.chat.service.FileWebService;
import ru.belogurow.socialnetworkclient.di.AppComponent;
import ru.belogurow.socialnetworkclient.di.AppModule;
import ru.belogurow.socialnetworkclient.di.DaggerAppComponent;
import ru.belogurow.socialnetworkclient.di.DatabaseModule;
import ru.belogurow.socialnetworkclient.users.service.UserWebService;
import ru.belogurow.socialnetworkclient.web.SelfSigningClientBuilder;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public class App extends Application {

    private static final String URL = "10.0.2.2:8090";
//    private static final String URL = "94.250.254.169:8090";
//    private static final String URL = "192.168.1.64:8090";

    public static final String BASE_URL = "https://" + URL;
    public static final String BASE_WEB_SOCKET_URL = "wss://" + URL;

    private static  AppComponent sComponent;
    public static UserWebService sWebUserService;
    public static ChatWebService sChatWebService;
    public static FileWebService sFileWebService;

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

        sWebUserService = retrofit.create(UserWebService.class);
        sChatWebService = retrofit.create(ChatWebService.class);
        sFileWebService = retrofit.create(FileWebService.class);
    }

    private void initDagger() {
        sComponent = DaggerAppComponent.builder()
                .databaseModule(new DatabaseModule(this))
                .appModule(new AppModule(this)).build();
    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(IconicsContextWrapper.wrap(base));
    }
}
