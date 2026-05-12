package com.hazyala.pickday.kopo.ac.kr.ui;

import android.app.DatePickerDialog;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PickDayDatePicker {

    public interface OnDateSelectedListener {
        void onDateSelected(Calendar selectedDate, String displayText, String summaryText);
    }

    public static void show(
            Context context,
            Calendar currentDate,
            OnDateSelectedListener listener
    ) {
        Calendar baseDate = currentDate == null ? Calendar.getInstance() : currentDate;

        DatePickerDialog dialog = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(Calendar.YEAR, year);
                    selected.set(Calendar.MONTH, month);
                    selected.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    String displayText = formatDisplayDate(selected);
                    String summaryText = formatSummaryDate(selected);

                    if (listener != null) {
                        listener.onDateSelected(selected, displayText, summaryText);
                    }
                },
                baseDate.get(Calendar.YEAR),
                baseDate.get(Calendar.MONTH),
                baseDate.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private static String formatDisplayDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd (E)", Locale.KOREAN);
        return sdf.format(calendar.getTime());
    }

    private static String formatSummaryDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("M.dd E", Locale.KOREAN);
        return sdf.format(calendar.getTime());
    }
}