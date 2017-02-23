package io.github.cs407_chatby.chatby.ui.main.create;


import android.support.annotation.NonNull;

class CreateContract {

    interface Presenter {
        void onAttach(View view);
        void onDetach();
        void onFinalizeClicked(String title, String radius);
        void onPickDate();
    }

    public interface View {
        void showSuccess();
        void showError(String message);
        void updateEndDate(@NonNull String date);
        void showDatePicker(int year, int month, int day);
        void showTimePicker(int hour, int min);
    }
}
