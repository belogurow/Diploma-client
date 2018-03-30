package ru.belogurow.socialnetworkclient.common;

import android.app.Application;

import ru.belogurow.socialnetworkclient.web.ControllerWebUserService;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public class App extends Application {

//    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        ControllerWebUserService.setContext(getApplicationContext());

//        this.appComponent = DaggerAppComponent.builder()
//                .appModule(new AppModule(this))
//                .roomModule(new RoomModule(this))
//                .build();
//
//        appComponent.inject(this);
    }

//    public AppComponent getAppComponent() {
//        return appComponent;
//    }


//    public static Context getContext() {
//        return getApplicationContext();
//    }

}
