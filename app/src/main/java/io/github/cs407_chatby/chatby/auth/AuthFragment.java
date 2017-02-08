package io.github.cs407_chatby.chatby.auth;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.util.ViewUtils;

public class AuthFragment extends Fragment implements AuthContract.View, OnBackPressedListener {
    EditText email;
    EditText password;
    EditText passConfirm;
    Button switchForm;
    Button submit;

    @Nullable
    AuthPresenter presenter = new AuthPresenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);
        email = (EditText) view.findViewById(R.id.form_email);
        password = (EditText) view.findViewById(R.id.form_password);
        passConfirm = (EditText) view.findViewById(R.id.form_confirm_password);
        switchForm = (Button) view.findViewById(R.id.button_switch_form);
        submit = (Button) view.findViewById(R.id.button_submit);

        switchForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleForm();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.submitClicked(passConfirm.getVisibility() == View.GONE);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) presenter.attachView(this);
    }

    @Override
    public void onStop() {
        if (presenter != null) presenter.detachView();
        super.onStop();
    }

    @Override
    public void toggleForm() {
        if (getView() == null) return;
        ViewUtils.toggleVisibility(passConfirm);
        if (passConfirm.getVisibility() == View.VISIBLE)
            switchForm.setText(R.string.login);
        else
            switchForm.setText(R.string.sign_up);
    }

    @Override
    public boolean backPressed() {
        if (passConfirm.getVisibility() == View.VISIBLE) {
            toggleForm();
            return true;
        }
        return false;
    }

    @Override
    public void showLoading() {
        // TODO
    }

    @Override
    public void showLoggedIn() {
        // TODO
    }

    @Override
    public void showSignedUp() {
        // TODO
    }

    @Override
    public void showError() {
        // TODO
    }

    @Override @Nullable
    public String getEmail() {
        if (email != null) return email.getText().toString();
        else return null;
    }

    @Override @Nullable
    public String getPassword() {
        if (password != null) return password.getText().toString();
        else return null;
    }

    @Override @Nullable
    public String getPassConfirmation() {
        if (passConfirm != null) return passConfirm.getText().toString();
        else return null;
    }

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }
}
