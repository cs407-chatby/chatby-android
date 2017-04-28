package io.github.cs407_chatby.chatby.ui.main.account;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.ui.auth.AuthActivity;
import io.github.cs407_chatby.chatby.utils.ActivityUtils;
import io.github.cs407_chatby.chatby.utils.ViewUtils;


public class AccountFragment extends Fragment implements AccountContract.View {

    @Inject AccountPresenter presenter;

    TextView email;
    TextView username;
    TextView name;
    Button editEmail;
    Button editUsername;
    Button editName;
    Switch anonToggle;
    View changePassword;
    View logout;
    View deleteAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((ChatByApp) getActivity().getApplication()).getComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Set views
        email = ViewUtils.findView(view, R.id.email_text);
        username = ViewUtils.findView(view, R.id.username_text);
        name = ViewUtils.findView(view, R.id.name_text);
        editEmail = ViewUtils.findView(view, R.id.email_edit);
        editUsername = ViewUtils.findView(view, R.id.username_edit);
        editName = ViewUtils.findView(view, R.id.name_edit);
        anonToggle = ViewUtils.findView(view, R.id.toggle_anonymous);
        changePassword = ViewUtils.findView(view, R.id.change_password);
        logout = ViewUtils.findView(view, R.id.logout);
        deleteAccount = ViewUtils.findView(view, R.id.delete_account);

        editEmail.setOnClickListener(v -> {
            showDialog("Email", (dialog, text) -> {
                if (presenter != null) presenter.onUpdateEmail(text);
            });
        });

        editUsername.setOnClickListener(v -> {
            showDialog("Username", (dialog, text) -> {
                if (presenter != null) presenter.onUpdateUsername(text);
            });
        });

        editName.setOnClickListener(v -> {
            showDialog("Full Name", (dialog, text) -> {
                if (presenter != null) presenter.onUpdateName(text);
            });
        });

        anonToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (presenter != null) presenter.onToggleAnonymous(isChecked);
        });

        changePassword.setOnClickListener(v -> {
            showDialog("Password", (dialog, text) -> {
                if (presenter != null) presenter.onUpdatePassword(text);
            });
        });

        logout.setOnClickListener(v -> {
            if (presenter != null) presenter.onLogout();
        });

        deleteAccount.setOnClickListener(v -> {
            if (presenter != null) presenter.onDeleteAccount();
        });

        return view;
    }

    interface UpdateListener {
        void onUpdateClicked(DialogInterface dialog, String text);
    }

    private void showDialog(String topic, UpdateListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update " + topic);

        @SuppressLint("InflateParams") View view = getActivity()
                .getLayoutInflater()
                .inflate(R.layout.layout_update_form, null);
        EditText input = ViewUtils.findView(view, R.id.edit_text);
        input.setHint(topic);

        builder.setView(view);
        builder.setPositiveButton("Update", (dialog, which) -> {
            positiveListener.onUpdateClicked(dialog, input.getText().toString());
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) presenter.onAttach(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.onRefresh();
    }

    @Override
    public void onStop() {
        if (presenter != null) presenter.onDetach();
        super.onStop();
    }

    @Override
    public void updateInfo(User user) {
        email.setText(user.getEmail());
        username.setText(user.getUsername());
        String fullName = user.getFirstName() + " " + user.getLastName();
        name.setText(fullName);
    }

    @Override
    public void showError(String message) {
        if (getView() != null)
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLoggedOut() {
        ActivityUtils.start(getActivity(), AuthActivity.class, null, true);
    }

    @Override
    public void showAnonymous(boolean toggled) {
        anonToggle.setChecked(toggled);
    }

    public static AccountFragment newInstance(Bundle args) {
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
