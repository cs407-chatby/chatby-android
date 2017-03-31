package io.github.cs407_chatby.chatby.ui.main.account;


import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.PatchPassword;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class AccountPresenter implements AccountContract.Presenter {

    private AccountContract.View view;

    private final ChatByService service;

    @Inject
    public AccountPresenter(ChatByService service) {
        this.service = service;
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> view.showSuccess(), error -> view.showError("Failed to update password!"));
    }
}
