package ru.belogurow.socialnetworkclient.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.AppDatabase;
import ru.belogurow.socialnetworkclient.chat.repository.RemoteChatRepository;
import ru.belogurow.socialnetworkclient.chat.repository.RemoteFileRepository;
import ru.belogurow.socialnetworkclient.chat.viewModel.ChatViewModel;
import ru.belogurow.socialnetworkclient.chat.viewModel.FileViewModel;
import ru.belogurow.socialnetworkclient.users.dao.UserDao;
import ru.belogurow.socialnetworkclient.users.repository.LocalUserRepository;
import ru.belogurow.socialnetworkclient.users.repository.RemoteUserRepository;
import ru.belogurow.socialnetworkclient.users.viewModel.UserViewModel;


@Singleton
@Component(modules = {AppModule.class, ChatModule.class, FileModule.class, UserModule.class, DatabaseModule.class})
public interface AppComponent {

    RemoteUserRepository getRemoteUserRepository();

    LocalUserRepository getLocalUserRepository();

    RemoteChatRepository getRemoteChatRepository();

    RemoteFileRepository getRemoteFileRepository();

    AppDatabase getAppDatabase();

    UserDao getUserDao();

    void inject(RemoteChatRepository remoteChatRepository);

    void inject(LocalUserRepository localUserRepository);

    void inject(RemoteUserRepository remoteUserRepository);

    void inject(UserViewModel userViewModel);

    void inject(ChatViewModel chatViewModel);

    void inject(FileViewModel fileViewModel);

    void inject(App app);

}