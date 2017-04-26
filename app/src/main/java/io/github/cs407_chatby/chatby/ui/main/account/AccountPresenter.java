package io.github.cs407_chatby.chatby.ui.main.account;


import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.AuthRequest;
import io.github.cs407_chatby.chatby.data.model.PatchPassword;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.github.cs407_chatby.chatby.data.AuthHolder;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class AccountPresenter implements AccountContract.Presenter {

    private final AuthHolder authHolder;
    private AccountContract.View view;

    private final ChatByService service;

    @Inject
    public AccountPresenter(ChatByService service, AuthHolder authHolder) {
        this.service = service;
        this.authHolder = authHolder;
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (view != null) view.updateInfo(user);
                });
    }

    @Override
    public void onUpdateEmail(String email) {
        service.getCurrentUser()
                .flatMap(user -> service.putUser(user.getId(), user.withEmail(email)))
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (view != null) view.updateInfo(user);
                }, error -> {
                    if (view != null) view.showError("Failed to update name");
                });
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
        if (view != null) view.showLoggedOut();
    }

    @Override
    public void onDeleteAccount() {
        service.getCurrentUser()
                .flatMapCompletable(user -> service.deleteUser(user.getId()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLogout, error -> {
                    if (view != null)
                        view.showError("Failed to delete account!");
                });
    }
}
