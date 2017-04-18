package io.github.cs407_chatby.chatby.ui.main.create;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.util.Calendar;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.utils.ViewUtils;


public class CreateFragment extends Fragment implements CreateContract.View {

    EditText title;
    EditText radius;
    TextView date;
    Button pickDate;
    Toolbar toolbar;

    FloatingActionButton fab;

    @Inject
    @Nullable
    CreatePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((ChatByApp) getActivity().getApplication()).getComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        title = ViewUtils.findView(view, R.id.title);
        radius = ViewUtils.findView(view, R.id.radius);
        date = ViewUtils.findView(view, R.id.date);
        pickDate = ViewUtils.findView(view, R.id.pick_date);
        toolbar = ViewUtils.findView(view, R.id.create_toolbar);
        fab = ViewUtils.findView(view, R.id.fab);

        fab.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(radius.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
            if (presenter != null)
                presenter.onFinalizeClicked(title.getText().toString(),
                        radius.getText().toString());
        });

        toolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
            clearText();
        });

        if (pickDate != null) {
            pickDate.setOnClickListener(v -> {
                if (presenter != null) presenter.onPickDate();
            });
        }

        return view;
    }

    private void clearText() {
        title.setText("");
        radius.setText("");
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
        getActivity().onBackPressed();
        clearText();
    }

    @Override
    public void showError(String message) {
        if (getView() != null)
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void updateEndDate(@NonNull String date) {
        this.date.setText(date);
    }

    @Override
    public void showDatePicker(int year, int month, int day) {
        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(presenter)
                .setPreselectedDate(year, month, day)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDoneText("Set")
                .setCancelText("Cancel")
                .setThemeCustom(R.style.PickerTheme);
        cdp.show(getActivity().getSupportFragmentManager(), "Date");
    }

    @Override
    public void showTimePicker(int hour, int min) {
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(presenter)
                .setStartTime(hour, min)
                .setDoneText("Set")
                .setCancelText("Cancel")
                .setThemeCustom(R.style.PickerTheme);
        rtpd.show(getActivity().getSupportFragmentManager(), "Time");
    }

    public static CreateFragment newInstance() {
        Bundle args = new Bundle();
        CreateFragment fragment = new CreateFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
