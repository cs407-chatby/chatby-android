package io.github.cs407_chatby.chatby.ui.auth;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

class AuthPresenter implements AuthContract.Presenter {
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
    public void loginClicked(String email, String password) {
        if (view == null) return;
        view.showLoading();
        Log.d("loginClicked", "email: " + email + ", password: " + password);
    }

    @Override
    public void signUpClicked(String email, String password, String passCheck) {
        if (view == null) return;
        view.showLoading();
        Log.d("loginClicked", "email: " + email + ", password: " + password + ", passCheck: " + passCheck);
    }
}
