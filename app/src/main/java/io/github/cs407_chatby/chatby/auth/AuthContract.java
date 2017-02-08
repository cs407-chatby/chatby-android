package io.github.cs407_chatby.chatby.auth;


public class AuthContract {

    public interface Presenter {
        void attachView(View view);
        void detachView();
        void submitClicked(boolean isLoggingIn);
    }

    public interface View {
        void showLoading();
        void showLoggedIn();
        void showSignedUp();
        void showError();
        void toggleForm();
        String getEmail();
        String getPassword();
        String getPassConfirmation();
    }
}
