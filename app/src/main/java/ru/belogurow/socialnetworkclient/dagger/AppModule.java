package ru.belogurow.socialnetworkclient.dagger;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

@Module(includes = RoomModule.class)
public class AppModule {

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    public Context providesAppContext() {
        return application;
    }

//    @Provides
//    @Singleton
//    public WebUserService providesUserService() {
//        return new Retrofit.Builder()
//                .baseUrl(App.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(WebUserService.class);
//    }

//    @Provides
//    @Singleton
//    public UserRepository providesUserRepository() {
//        return new UserRepository();
//    }
}
