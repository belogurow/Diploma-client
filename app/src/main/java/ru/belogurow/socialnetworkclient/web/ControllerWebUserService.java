package ru.belogurow.socialnetworkclient.web;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.belogurow.socialnetworkclient.users.service.WebUserService;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public class ControllerWebUserService {
//    public static final String BASE_URL = "https://10.0.2.2:8080";
    public static final String BASE_URL = "https://192.168.1.64:8080";

    private static Context sContext;

    private static WebUserService sWebUserService;
    private static Retrofit mRetrofit;

    public static void setContext(Context context) {
        sContext = context;
    }

    public static WebUserService getWebUserService() {
        if (sWebUserService == null) {

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(SelfSigningClientBuilder.createClient(sContext))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            sWebUserService = mRetrofit.create(WebUserService.class);
        }
        return sWebUserService;
    }
}
