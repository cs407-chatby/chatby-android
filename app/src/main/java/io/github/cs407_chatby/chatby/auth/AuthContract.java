package io.github.cs407_chatby.chatby.auth;


class AuthContract {

    interface Presenter {
        void attachView(View view);
        void detachView();
        void signUpClicked(String email, String password, String passCheck);
        void loginClicked(String email, String password);
    }

    public interface View {
        void showLoading();
        void showLoggedIn();
        void showSignedUp();
        void showError();
        void toggleForm();
    }
}
