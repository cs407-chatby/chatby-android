package io.github.cs407_chatby.chatby.ui.main.account;


import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.AuthRequest;
import io.github.cs407_chatby.chatby.data.model.PatchPassword;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.github.cs407_chatby.chatby.ui.auth.AuthHolder;
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
    public void onSavePressed(String email, String username, String firstName, String lastName) {
        service.getCurrentUser()
                .flatMap(user -> service.putUser(user.getId(), user
                        .withEmail(email)
                        .withUsername(username)
                        .withFirstName(firstName)
                        .withLastName(lastName)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> view.showSuccess(), error -> view.showError("Failed to update user!"));
    }

    @Override
    public void onUpdatePassPressed(String password) {
        service.getCurrentUser()
                .flatMap(user -> service.patchUserPassword(user.getId(), new PatchPassword(password)))
                .map(user -> {
                    authHolder.deleteToken();
                    return user;
                })
                .flatMap(user -> service.postAuth(new AuthRequest(user.getUsername(), password)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authResponse -> {
                    authHolder.saveToken(authResponse.getToken());
                    view.showSuccess();
                }, error -> view.showError("Failed to update password!"));
    }
}
