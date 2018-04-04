package ru.belogurow.socialnetworkclient.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.users.repository.UserRepository;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;


@Singleton
@Component(modules = {AppModule.class, UserModule.class})
public interface AppComponent {

    UserRepository getUserRepository();

    void inject(UserViewModel userViewModel);

    void inject(App app);

}