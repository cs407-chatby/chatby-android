package io.github.cs407_chatby.chatby.ui.main.account;


import javax.inject.Inject;

public class AccountPresenter implements AccountContract.Presenter {
    AccountContract.View view;

    @Inject
    public AccountPresenter() {}

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
        // TODO
    }

    @Override
    public void onUpdatePassPressed(String password) {
        // TODO
    }
}
