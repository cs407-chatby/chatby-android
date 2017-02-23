package io.github.cs407_chatby.chatby.ui.main.create;


import android.util.Log;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.PostRoom;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.github.cs407_chatby.chatby.utils.LocationManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.HttpException;

public class CreatePresenter implements CreateContract.Presenter,
        CalendarDatePickerDialogFragment.OnDateSetListener,
        RadialTimePickerDialogFragment.OnTimeSetListener {

    private final LocationManager locationManager;
    private final ChatByService service;
    private CreateContract.View view;
    private Calendar calendar;

    @Inject
    public CreatePresenter(LocationManager locationManager, ChatByService service) {
        Date date = new Date();
        calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        this.locationManager = locationManager;
        this.service = service;
    }

    @Override
    public void onAttach(CreateContract.View view) {
        this.view = view;
        updateDate();
        locationManager.start();
    }

    @Override
    public void onDetach() {
        view = null;
        locationManager.stop();
    }

    @Override
    public void onFinalizeClicked(String title, String radius) {
        if (title.isEmpty() || radius.isEmpty() && view != null) {
            view.showError("Please fill out all forms");
            return;
        }

        locationManager.getObservable()
                .firstOrError()
                .timeout(10, TimeUnit.SECONDS)
                .map(location -> new PostRoom(title, Double.parseDouble(radius),
                        calendar.getTime(), null, location.getLatitude(), location.getLongitude()))
                .flatMap(service::postRoom)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(room -> {
                    Log.d("create", room.toString());
                    if (view != null)
                        view.showSuccess();
                }, error -> {
                    Log.e("create", error.getMessage(), error);
                    if (view != null)
                        view.showError("Failed to create room!");
                });
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
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d 'at' h:mm", Locale.US);
        view.updateEndDate(format.format(calendar.getTime()));
    }
}
