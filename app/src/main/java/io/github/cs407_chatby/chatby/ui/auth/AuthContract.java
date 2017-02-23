package io.github.cs407_chatby.chatby.ui.auth;


class AuthContract {

    enum Form {
        Login, SignUp
    }

    interface Presenter {
        void onAttach(View view);
        void onDetach();
        void onSubmit(String email, String password, String passCheck);
        void onSwitchForms();
        boolean onCancel();
    }

    public interface View {
        void showLoading();
        void hideLoading();
        void showLoggedIn();
        void showError(String message);
        void toggleForm();
    }
}
