package io.github.cs407_chatby.chatby.di.auth;

import dagger.Subcomponent;
import io.github.cs407_chatby.chatby.di.PerActivity;
import io.github.cs407_chatby.chatby.ui.auth.AuthActivity;
import io.github.cs407_chatby.chatby.ui.auth.AuthFragment;

@PerActivity
@Subcomponent(modules = AuthModule.class)
public interface AuthComponent {
    void inject(AuthActivity activity);
    void inject(AuthFragment fragment);
}
