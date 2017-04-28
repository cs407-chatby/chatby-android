package io.github.cs407_chatby.chatby.ui.main.account;


import io.github.cs407_chatby.chatby.data.model.User;

public class AccountContract {

    public interface Presenter {
        void onAttach(View view);
        void onDetach();
        void onRefresh();
        void onUpdateEmail(String email);
        void onUpdateUsername(String username);
        void onUpdateName(String name);
        void onToggleAnonymous(boolean toggled);
        void onUpdatePassword(String password);
        void onLogout();
        void onDeleteAccount();
    }

    public interface View {
        void updateInfo(User user);
        void showError(String message);
        void showLoggedOut();
        void showAnonymous(boolean toggled);
    }
}
