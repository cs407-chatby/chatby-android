package io.github.cs407_chatby.chatby.ui.main.account;


import android.content.SharedPreferences;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.AuthHolder;
import io.github.cs407_chatby.chatby.data.CurrentUserCache;
import io.github.cs407_chatby.chatby.data.model.AuthRequest;
import io.github.cs407_chatby.chatby.data.model.PatchPassword;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AccountPresenter implements AccountContract.Presenter {

    private final AuthHolder authHolder;
    private final ChatByService service;
    private final CurrentUserCache userCache;
    private final SharedPreferences preferences;

    private AccountContract.View view;

    @Inject
    public AccountPresenter(ChatByService service, AuthHolder authHolder,
                            CurrentUserCache userCache, SharedPreferences preferences) {
        this.service = service;
        this.authHolder = authHolder;
        this.userCache = userCache;
        this.preferences = preferences;
    }

    @Override
    public void onAttach(AccountContract.View view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    @Override
    public void onRefresh() {
        service.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (view != null) view.updateInfo(user);
                });

        if (view != null) view.showAnonymous(preferences.getBoolean("anon", false));
    }

    @Override
    public void onUpdateEmail(String email) {
        service.getCurrentUser()
                .flatMap(user -> service.putUser(user.getId(), user.withEmail(email)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (view != null) view.updateInfo(user);
                }, error -> {
                    if (view != null) view.showError("Failed to update email");
                });
    }

    @Override
    public void onUpdateUsername(String username) {
        service.getCurrentUser()
                .flatMap(user -> service.putUser(user.getId(), user.withUsername(username)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (view != null) view.updateInfo(user);
                }, error -> {
                    if (view != null) view.showError("Failed to update username");
                });
    }

    @Override
    public void onUpdateName(String name) {
        int split = name.indexOf(' ');
        String first, last;
        if (split < 0) {
            first = name;
            last = "";
        } else {
            first = name.substring(0, split);
            last = name.substring(split);
        }
        service.getCurrentUser()
                .flatMap(user -> service.putUser(user.getId(),
                        user.withFirstName(first).withLastName(last)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (view != null) view.updateInfo(user);
                }, error -> {
                    if (view != null) view.showError("Failed to update name");
                });
    }

    @Override
    public void onToggleAnonymous(boolean toggled) {
        preferences.edit().putBoolean("anon", toggled).apply();
        if (view != null) view.showAnonymous(toggled);
    }

    @Override
    public void onUpdatePassword(String password) {
        service.getCurrentUser()
                .flatMap(user -> service.patchUserPassword(user.getId(), new PatchPassword(password)))
                .map(user -> {
                    authHolder.deleteToken();
                    return user;
                })
                .flatMap(user -> service.postAuth(new AuthRequest(user.getUsername(), password)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    authHolder.saveToken(response.getToken());
                }, error -> {
                    if (view != null) view.showError("Failed to update password");
                });
    }

    @Override
    public void onLogout() {
        authHolder.deleteToken();
        userCache.setCurrentUser(null);
        if (view != null) view.showLoggedOut();
    }

    @Override
    public void onDeleteAccount() {
        service.getCurrentUser()
                .flatMapCompletable(user -> service.deleteUser(user.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLogout, error -> {
                    if (view != null)
                        view.showError("Failed to delete account!");
                });
    }
}
