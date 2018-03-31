package ru.belogurow.socialnetworkclient.common;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.belogurow.socialnetworkclient.users.service.WebUserService;
import ru.belogurow.socialnetworkclient.web.SelfSigningClientBuilder;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public class App extends Application {

    public static final String BASE_URL = "https://10.0.2.2:8080";
//    public static final String BASE_URL = "https://192.168.1.64:8080";


    public static WebUserService sWebUserService;

    @Override
    public void onCreate() {
        super.onCreate();

        initRetrofit();
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(SelfSigningClientBuilder.createClient(getApplicationContext()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sWebUserService = retrofit.create(WebUserService.class);
    }

}
