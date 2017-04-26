package io.github.cs407_chatby.chatby.di;


import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.di.auth.AuthComponent;
import io.github.cs407_chatby.chatby.di.auth.AuthModule;
import io.github.cs407_chatby.chatby.ui.main.account.AccountFragment;
import io.github.cs407_chatby.chatby.ui.main.active.ActiveFragment;
import io.github.cs407_chatby.chatby.ui.main.create.CreateFragment;
import io.github.cs407_chatby.chatby.ui.main.nearby.NearbyFragment;
import io.github.cs407_chatby.chatby.ui.room.main.RoomFragment;
import io.github.cs407_chatby.chatby.ui.room.member.MemberListFragment;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(ChatByApp application);
    void inject(NearbyFragment fragment);
    void inject(CreateFragment fragment);
    void inject(RoomFragment fragment);
    void inject(MemberListFragment fragment);
    void inject(AccountFragment fragment);
    void inject(ActiveFragment fragment);

    AuthComponent plus(AuthModule module);

    @Named("Application")
    Context getContext();
}
