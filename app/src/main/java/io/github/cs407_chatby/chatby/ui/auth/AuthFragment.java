package io.github.cs407_chatby.chatby.ui.auth;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.ui.home.MainActivity;
import io.github.cs407_chatby.chatby.utils.ViewUtils;

import static io.github.cs407_chatby.chatby.utils.ActivityUtils.finishAndStart;
import static io.github.cs407_chatby.chatby.utils.ViewUtils.viewIsVisible;

public class AuthFragment extends Fragment implements AuthContract.View, OnBackPressedListener {
    EditText email;
    EditText password;
    EditText passConfirm;
    Button switchForm;
    Button submit;
    View progressBar;
    View dimBackdrop;

    @Nullable
    AuthPresenter presenter = new AuthPresenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);

        // Init views
        email = (EditText) view.findViewById(R.id.form_email);
        password = (EditText) view.findViewById(R.id.form_password);
        passConfirm = (EditText) view.findViewById(R.id.form_confirm_password);
        switchForm = (Button) view.findViewById(R.id.button_switch_form);
        submit = (Button) view.findViewById(R.id.button_submit);
        progressBar = view.findViewById(R.id.progress_bar);
        dimBackdrop = view.findViewById(R.id.dim_backdrop);

        // Click handlers
        switchForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) presenter.switchFormsClicked();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) presenter.submitClicked(
                        email.getText().toString(),
                        password.getText().toString(),
                        passConfirm.getText().toString()
                );
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
        if (viewIsVisible(passConfirm)) {
            switchForm.setText(R.string.login);
        } else {
            switchForm.setText(R.string.sign_up);
        }
    }

    @Override
    public boolean backPressed() {
        return presenter != null && presenter.cancelClicked();
    }

    @Override
    public void showLoading() {
        if (getView() == null) return;
        progressBar.setVisibility(View.VISIBLE);
        dimBackdrop.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (getView() == null) return;
        progressBar.setVisibility(View.GONE);
        dimBackdrop.setVisibility(View.GONE);
    }

    @Override
    public void showLoggedIn() {
        Activity activity = getActivity();
        if (activity != null) finishAndStart(activity, MainActivity.class);
    }

    @Override
    public void showSignedUp() {
        Activity activity = getActivity();
        if (activity != null) finishAndStart(activity, MainActivity.class);
    }

    @Override
    public void showError(String message) {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }
}
