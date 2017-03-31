package io.github.cs407_chatby.chatby.ui.main.account;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.utils.ViewUtils;


public class AccountFragment extends Fragment implements AccountContract.View {

    @Inject AccountContract.Presenter presenter;

    EditText email;
    EditText username;
    EditText firstName;
    EditText lastName;
    Button save;

    EditText password;
    Button updatePass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        if (getArguments() != null) {
            String json = getArguments().getString("user");
            User user = new Gson().fromJson(json, User.class);
            username = ViewUtils.findView(view, R.id.edit_username);
            firstName = ViewUtils.findView(view, R.id.edit_first_name);
            lastName = ViewUtils.findView(view, R.id.edit_last_name);
            save = ViewUtils.findView(view, R.id.button_save);
            password = ViewUtils.findView(view, R.id.edit_password);
            updatePass = ViewUtils.findView(view, R.id.button_save_password);

            email.setText(user.getEmail());
            username.setText(user.getUsername());
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());

            save.setOnClickListener(v -> {
                if (presenter != null) presenter.onSavePressed(
                        email.getText().toString(), username.getText().toString(),
                        firstName.getText().toString(), lastName.getText().toString());
            });
            updatePass.setOnClickListener(v -> {
                if (presenter != null)
                    presenter.onUpdatePassPressed(password.getText().toString());
            });
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) presenter.onAttach(this);
    }

    @Override
    public void onStop() {
        if (presenter != null) presenter.onDetach();
        super.onStop();
    }

    @Override
    public void showSuccess() {
        showError("Account settings saved");
    }

    @Override
    public void showError(String message) {
        if (getView() != null)
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    public static AccountFragment newInstance(Bundle args) {
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
