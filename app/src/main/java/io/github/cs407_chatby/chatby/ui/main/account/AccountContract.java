package io.github.cs407_chatby.chatby.ui.main.account;


public class AccountContract {

    public interface Presenter {
        void onAttach(View view);
        void onDetach();
        void onSavePressed(String email, String username, String firstName, String lastName);
        void onUpdatePassPressed(String password);
    }

    public interface View {
        void showSuccess();
        void showError(String message);
    }
}
