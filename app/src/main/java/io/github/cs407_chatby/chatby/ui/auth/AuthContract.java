package io.github.cs407_chatby.chatby.ui.auth;


class AuthContract {

    enum Form {
        Login, SignUp
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void submitClicked(String email, String password, String passCheck);
        void switchFormsClicked();
        boolean cancelClicked();
    }

    public interface View {
        void showLoading();
        void hideLoading();
        void showLoggedIn();
        void showSignedUp();
        void showError(String message);
        void toggleForm();
    }
}
