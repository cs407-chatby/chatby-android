package io.github.cs407_chatby.chatby.ui.auth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.AuthHolder;
import io.github.cs407_chatby.chatby.data.model.AuthRequest;
import io.github.cs407_chatby.chatby.data.model.PostUser;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.github.cs407_chatby.chatby.di.PerActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;

@PerActivity
class AuthPresenter implements AuthContract.Presenter {

    private final AuthHolder authHolder;

    private final ChatByService service;

    @Nullable
    private AuthContract.View view = null;

    private boolean working = false;

    private AuthContract.Form formType = AuthContract.Form.Login;

    @Inject
    public AuthPresenter(ChatByService service, AuthHolder authHolder) {
        this.service = service;
        this.authHolder = authHolder;
    }

    @Override
    public void onAttach(@NonNull AuthContract.View view) {
        this.view = view;
        if (authHolder.readToken() != null) {
            // TODO make a request to check the token is valid
            working = true;
            showSuccess();
        }
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    @Override
    public void onSubmit(String email, String password, String passCheck) {
        if (view == null)
            return;
        view.showLoading();
        switch (formType) {
            case Login:
                Log.d("clicked", "login");
                working = true;
                login(email, password);
                break;
            case SignUp:
                if (password.equals(passCheck)) {
                    Log.d("clicked", "signup");
                    working = true;
                    signup(email, password);
                } else {
                    view.hideLoading();
                    view.showError("Passwords do not match!");
                }
                break;
        }
    }

    private void login(String email, String password) {
        AuthRequest request = new AuthRequest(email, password);
        service.postAuth(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authResponse -> {
                    Log.d("response", authResponse.toString());
                    authHolder.saveToken(authResponse.getToken());
                    showSuccess();
                }, error -> {
                    Log.e("response", "login failure", error);
                    showFailure("Failed to log in!");
                });
    }

    private void signup(final String email, final String password) {
        PostUser postUser = new PostUser(email, email, "", "", password);
        service.postUser(postUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    Log.d("response", user.toString());
                    login(email, password);
                }, error -> {
                    Log.e("response", "signup failure", error);
                    showFailure("Failed to sign up!");
                });
    }

    private void showFailure(String message) {
        if (view != null && working) {
            view.hideLoading();
            view.showError(message);
            working = false;
        }
    }

    private void showSuccess() {
        if (view != null && working) {
            view.hideLoading();
            view.showLoggedIn();
            working = false;
        }
    }

    @Override
    public void onSwitchForms() {
        if (view == null)
            return;
        view.toggleForm();
        if (formType == AuthContract.Form.Login)
            formType = AuthContract.Form.SignUp;
        else formType = AuthContract.Form.Login;
    }

    @Override
    public boolean onCancel() {
        if (view == null) return false;

        if (working)
            view.hideLoading();
        else if (formType == AuthContract.Form.SignUp)
            onSwitchForms();
        else
            return false;

        working = false;
        return true;
    }
}
