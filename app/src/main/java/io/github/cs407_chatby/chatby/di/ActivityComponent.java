package io.github.cs407_chatby.chatby.di;


import dagger.Component;
import io.github.cs407_chatby.chatby.ui.auth.AuthActivity;
import io.github.cs407_chatby.chatby.ui.main.MainActivity;
import io.github.cs407_chatby.chatby.ui.account.AccountActivity;
import io.github.cs407_chatby.chatby.ui.room.RoomActivity;

@PerActivity
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(AuthActivity activity);
    void inject(RoomActivity activity);
    void inject(AccountActivity activity);
}
