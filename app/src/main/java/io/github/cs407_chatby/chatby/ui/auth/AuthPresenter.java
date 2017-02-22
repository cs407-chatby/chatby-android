package io.github.cs407_chatby.chatby.ui.auth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.AuthRequest;
import io.github.cs407_chatby.chatby.data.model.AuthResponse;
import io.github.cs407_chatby.chatby.data.model.PostUser;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

class AuthPresenter implements AuthContract.Presenter {

    private final AccountHolder accountHolder;

    private final ChatByService service;

    @Nullable
    private AuthContract.View view = null;

    private boolean working = false;

    private AuthContract.Form formType = AuthContract.Form.Login;

    @Inject
    public AuthPresenter(ChatByService service, AccountHolder accountHolder) {
        this.service = service;
        this.accountHolder = accountHolder;
    }

    @Override
    public void attachView(@NonNull AuthContract.View view) {
        this.view = view;
        if (accountHolder.readToken() != null) {
            // TODO make a request to check the token is valid
            working = true;
            showSuccess();
        }
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void submitClicked(String email, String password, String passCheck) {
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
                .subscribe(new SingleObserver<AuthResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(AuthResponse authResponse) {
                        Log.d("response", authResponse.toString());
                        accountHolder.saveToken(authResponse.getToken());
                        showSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("response", "login failure", e);
                        showFailure("Failed to log in!");
                    }
                });
    }

    private void signup(final String email, final String password) {
        PostUser user = new PostUser(email, email, "", "", password);
        service.postUser(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(User user) {
                        Log.d("response", user.toString());
                        login(email, password);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("response", "signup failure", e);
                        showFailure("Failed to sign up!");
                    }
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
    public void switchFormsClicked() {
        if (view == null)
            return;
        view.toggleForm();
        if (formType == AuthContract.Form.Login)
            formType = AuthContract.Form.SignUp;
        else formType = AuthContract.Form.Login;
    }

    @Override
    public boolean cancelClicked() {
        if (view == null) return false;

        if (working)
            view.hideLoading();
        else if (formType == AuthContract.Form.SignUp)
            switchFormsClicked();
        else
            return false;

        working = false;
        return true;
    }
}
