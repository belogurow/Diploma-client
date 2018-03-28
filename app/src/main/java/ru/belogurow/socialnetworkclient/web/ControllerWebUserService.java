package ru.belogurow.socialnetworkclient.web;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public class ControllerWebUserService {
    public static final String BASE_URL = "http://10.0.2.2:8080";

    private static WebUserService sWebUserService;
    private static Retrofit mRetrofit;

    public static WebUserService getWebUserService() {
        if (sWebUserService == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            sWebUserService = mRetrofit.create(WebUserService.class);
        }
        return sWebUserService;
    }
}
