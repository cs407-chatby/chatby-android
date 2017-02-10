package io.github.cs407_chatby.chatby.ui.auth;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class AuthPresenter implements AuthContract.Presenter {
    @Nullable private AuthContract.View view = null;
    private boolean working = false;
    private AuthContract.Form formType = AuthContract.Form.Login;

    @Override
    public void attachView(@NonNull AuthContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void submitClicked(String email, String password, String passCheck) {
        if (view == null) return;
        view.showLoading();
        if (formType == AuthContract.Form.Login) {
            Log.d("loginClicked", "email: " + email + ", password: " + password);
            login();
            working = true;
        } else if (password.equals(passCheck)) {
            Log.d("loginClicked", "email: " + email + ", password: " + password + ", passCheck: " + passCheck);
            login();
            working = true;
        } else {
            view.hideLoading();
            view.showError("Passwords do not match!");
        }
    }

    private void login() {
        Observable.just(1)
                .delay(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    public void onSubscribe(Disposable d) {}
                    public void onNext(Integer o) {}
                    public void onError(Throwable e) {}
                    public void onComplete() {
                        showSuccess();
                    }
                });
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
        if (view == null) return;
        view.toggleForm();
        if (formType == AuthContract.Form.Login) formType = AuthContract.Form.SignUp;
        else formType = AuthContract.Form.Login;
    }

    @Override
    public boolean cancelClicked() {
        if (view == null) return false;

        if (working) view.hideLoading();
        else if (formType == AuthContract.Form.SignUp) switchFormsClicked();
        else return false;

        working = false;
        return true;
    }
}
