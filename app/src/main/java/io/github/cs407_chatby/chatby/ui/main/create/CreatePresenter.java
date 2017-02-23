package io.github.cs407_chatby.chatby.ui.main.create;


import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.inject.Inject;

public class CreatePresenter implements CreateContract.Presenter,
        CalendarDatePickerDialogFragment.OnDateSetListener,
        RadialTimePickerDialogFragment.OnTimeSetListener {
    private CreateContract.View view;
    private Calendar calendar;

    @Inject
    public CreatePresenter() {
        Date date = new Date();
        calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
    }

    @Override
    public void onAttach(CreateContract.View view) {
        this.view = view;
        updateDate();
    }

    @Override
    public void onDetach() {
        view = null;
    }

    @Override
    public void onFinalizeClicked(String title, String radius) {
        if (title.isEmpty() || radius.isEmpty() && view != null) {
            view.showError("Please fill out all forms");
            return;
        }

        // TODO obtain location and use PostRoom for the creation request


        if (view != null) view.showSuccess();
    }

    @Override
    public void onPickDate() {
        if (view != null) {
            view.showDatePicker(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        if (view != null) {
            updateDate();
            view.showTimePicker(
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE));
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, hourOfDay, minute);

        if (view != null) updateDate();
    }

    private void updateDate() {
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d 'at' h", Locale.US);
        view.updateEndDate(format.format(calendar.getTime()));
    }
}
