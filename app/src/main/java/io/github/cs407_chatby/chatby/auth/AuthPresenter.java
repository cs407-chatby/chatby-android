package io.github.cs407_chatby.chatby.auth;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class AuthPresenter implements AuthContract.Presenter {
    @Nullable private AuthContract.View view = null;

    @Override
    public void attachView(@NonNull AuthContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void submitClicked(boolean isLoggingIn) {

    }
}
