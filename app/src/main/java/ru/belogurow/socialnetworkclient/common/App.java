package ru.belogurow.socialnetworkclient.common;

import android.app.Application;

/**
 * Created by alexbelogurow on 26.03.2018.
 */

public class App extends Application {

//    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

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
}
