package com.hazyala.pickday.kopo.ac.kr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResponseSelectionActivity extends AppCompatActivity {

    private AppCompatButton btnBack;
    private TextView btnComplete;

    private TextView tvDateCount;
    private TextView tvSelectedDateCount;
    private TextView tvExcludeCount;

    private LinearLayout layoutSelectedDates;
    private LinearLayout layoutExcludeDates;

    private LinearLayout responseCalendar;
    private GridLayout calendarGrid;

    private final int MAX_SELECT_COUNT = 10;

    private final List<DateItem> candidateDates = new ArrayList<>();
    private final Set<String> selectedDates = new HashSet<>();
    private final Set<String> selectedTimes = new HashSet<>();
    private final Set<String> excludedDates = new HashSet<>();

    private final List<TextView> timeViews = new ArrayList<>();
    private final List<TextView> excludeViews = new ArrayList<>();

    private final int PURPLE = Color.parseColor("#5B4CDB");
    private final int DARK_TEXT = Color.parseColor("#232336");
    private final int SUB_TEXT = Color.parseColor("#8D8AA5");
    private final int DISABLED_TEXT = Color.parseColor("#B8B8C8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_selection);

        initViews();
        initCandidateDates();

        setupCalendar();
        setupTimeOptions();
        setupExcludeOptions();
        updateSelectedDateArea();

        setListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnComplete = findViewById(R.id.btnComplete);

        tvDateCount = findViewById(R.id.tvDateCount);
        tvSelectedDateCount = findViewById(R.id.tvSelectedDateCount);
        tvExcludeCount = findViewById(R.id.tvExcludeCount);

        layoutSelectedDates = findViewById(R.id.layoutSelectedDates);
        layoutExcludeDates = findViewById(R.id.layoutExcludeDates);

        responseCalendar = findViewById(R.id.responseCalendar);
        calendarGrid = responseCalendar.findViewById(R.id.calendarGrid);
    }

    private void initCandidateDates() {
        candidateDates.add(new DateItem("21", "5.21", "수"));
        candidateDates.add(new DateItem("22", "5.22", "목"));
        candidateDates.add(new DateItem("23", "5.23", "금"));
        candidateDates.add(new DateItem("24", "5.24", "토"));
        candidateDates.add(new DateItem("25", "5.25", "일"));
        candidateDates.add(new DateItem("26", "5.26", "월"));
        candidateDates.add(new DateItem("27", "5.27", "화"));
    }

    private void setupCalendar() {
        for (int i = 0; i < calendarGrid.getChildCount(); i++) {
            View child = calendarGrid.getChildAt(i);

            if (child instanceof TextView) {
                TextView dayView = (TextView) child;
                String dayText = dayView.getText().toString();

                dayView.setTextColor(DARK_TEXT);
                dayView.setBackgroundColor(Color.TRANSPARENT);
                dayView.setClickable(false);
                dayView.setFocusable(false);

                DateItem item = findCandidateDate(dayText);

                if (item != null) {
                    dayView.setTextColor(PURPLE);
                    dayView.setBackgroundResource(R.drawable.pickday_card_soft);
                    dayView.setClickable(true);
                    dayView.setFocusable(true);

                    dayView.setOnClickListener(v -> toggleDate(item));
                } else if (!dayText.isEmpty()) {
                    dayView.setTextColor(DISABLED_TEXT);
                }
            }
        }
    }

    private DateItem findCandidateDate(String day) {
        for (DateItem item : candidateDates) {
            if (item.day.equals(day)) {
                return item;
            }
        }
        return null;
    }

    private void toggleDate(DateItem item) {
        if (selectedDates.contains(item.label)) {
            selectedDates.remove(item.label);
        } else {
            if (selectedDates.size() >= MAX_SELECT_COUNT) {
                Toast.makeText(this, "최대 10개까지 선택할 수 있어요", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedDates.add(item.label);
        }

        updateCalendarState();
        updateSelectedDateArea();
    }

    private void updateCalendarState() {
        for (int i = 0; i < calendarGrid.getChildCount(); i++) {
            View child = calendarGrid.getChildAt(i);

            if (child instanceof TextView) {
                TextView dayView = (TextView) child;
                String dayText = dayView.getText().toString();

                DateItem item = findCandidateDate(dayText);

                if (item == null) {
                    continue;
                }

                if (selectedDates.contains(item.label)) {
                    dayView.setText(item.day + "\n✓");
                    dayView.setTextColor(Color.WHITE);
                    dayView.setBackgroundResource(R.drawable.pickday_button_primary);
                } else {
                    dayView.setText(item.day);
                    dayView.setTextColor(PURPLE);
                    dayView.setBackgroundResource(R.drawable.pickday_card_soft);
                }
            }
        }
    }

    private void updateSelectedDateArea() {
        layoutSelectedDates.removeAllViews();

        for (DateItem item : candidateDates) {
            if (selectedDates.contains(item.label)) {
                TextView chip = createSelectedDateChip(item);
                layoutSelectedDates.addView(chip);
            }
        }

        tvDateCount.setText("▣ 최대 10개까지 선택할 수 있어요 (" + selectedDates.size() + "/10)");
        tvSelectedDateCount.setText(selectedDates.size() + "개 선택  ^");
    }

    private TextView createSelectedDateChip(DateItem item) {
        TextView chip = new TextView(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp(58), dp(52));
        params.setMargins(0, 0, dp(8), 0);
        chip.setLayoutParams(params);

        chip.setGravity(android.view.Gravity.CENTER);
        chip.setText(item.label + "\n" + item.week + "\n×");
        chip.setTextSize(11);
        chip.setTextColor(PURPLE);
        chip.setTypeface(null, android.graphics.Typeface.BOLD);
        chip.setBackgroundResource(R.drawable.pickday_unselected);
        chip.setIncludeFontPadding(false);
        chip.setClickable(true);
        chip.setFocusable(true);

        chip.setOnClickListener(v -> {
            selectedDates.remove(item.label);
            updateCalendarState();
            updateSelectedDateArea();
        });

        return chip;
    }

    private void setupTimeOptions() {
        addTimeView(R.id.timeMorning, "오전");
        addTimeView(R.id.timeAfternoon, "오후");
        addTimeView(R.id.timeLateAfternoon, "늦은 오후");
        addTimeView(R.id.timeEvening, "저녁");
        addTimeView(R.id.timeLateEvening, "늦은 저녁");
        addTimeView(R.id.timeAny, "상관없음");
    }

    private void addTimeView(int id, String key) {
        TextView view = findViewById(id);
        timeViews.add(view);

        view.setOnClickListener(v -> {
            if (selectedTimes.contains(key)) {
                selectedTimes.remove(key);
            } else {
                selectedTimes.add(key);
            }

            updateTimeState();
        });
    }

    private void updateTimeState() {
        updateSingleTimeState(R.id.timeMorning, "오전", "오전\n09:00~12:00");
        updateSingleTimeState(R.id.timeAfternoon, "오후", "오후\n12:00~15:00");
        updateSingleTimeState(R.id.timeLateAfternoon, "늦은 오후", "늦은 오후\n15:00~18:00");
        updateSingleTimeState(R.id.timeEvening, "저녁", "저녁\n18:00~21:00");
        updateSingleTimeState(R.id.timeLateEvening, "늦은 저녁", "늦은 저녁\n21:00~24:00");
        updateSingleTimeState(R.id.timeAny, "상관없음", "상관없음\n하루 종일 가능");
    }

    private void updateSingleTimeState(int id, String key, String label) {
        TextView view = findViewById(id);

        if (selectedTimes.contains(key)) {
            view.setText("● " + label);
            view.setTextColor(PURPLE);
            view.setBackgroundResource(R.drawable.pickday_selected);
        } else {
            view.setText("○ " + label);
            view.setTextColor(Color.parseColor("#59566F"));
            view.setBackgroundResource(R.drawable.pickday_unselected);
        }
    }

    private void setupExcludeOptions() {
        addExcludeView(R.id.exclude0529, "5.29");
        addExcludeView(R.id.exclude0530, "5.30");
        addExcludeView(R.id.exclude0531, "5.31");
        addExcludeView(R.id.exclude0601, "6.1");
        addExcludeView(R.id.exclude0602, "6.2");
        addExcludeView(R.id.exclude0603, "6.3");
        addExcludeView(R.id.exclude0604, "6.4");
    }

    private void addExcludeView(int id, String key) {
        TextView view = findViewById(id);
        excludeViews.add(view);

        view.setOnClickListener(v -> {
            if (excludedDates.contains(key)) {
                excludedDates.remove(key);
            } else {
                excludedDates.add(key);
            }

            updateExcludeState();
        });
    }

    private void updateExcludeState() {
        updateSingleExcludeState(R.id.exclude0529, "5.29", "5.29\n목");
        updateSingleExcludeState(R.id.exclude0530, "5.30", "5.30\n금");
        updateSingleExcludeState(R.id.exclude0531, "5.31", "5.31\n토");
        updateSingleExcludeState(R.id.exclude0601, "6.1", "6.1\n일");
        updateSingleExcludeState(R.id.exclude0602, "6.2", "6.2\n월");
        updateSingleExcludeState(R.id.exclude0603, "6.3", "6.3\n화");
        updateSingleExcludeState(R.id.exclude0604, "6.4", "6.4\n수");

        tvExcludeCount.setText(excludedDates.size() + "개 선택  ˅");
    }

    private void updateSingleExcludeState(int id, String key, String label) {
        TextView view = findViewById(id);

        if (excludedDates.contains(key)) {
            view.setText(label + "\n✓");
            view.setTextColor(PURPLE);
            view.setBackgroundResource(R.drawable.pickday_selected);
        } else {
            view.setText(label);
            view.setTextColor(DARK_TEXT);
            view.setBackgroundResource(R.drawable.pickday_unselected);
        }
    }

    private void setListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnComplete.setOnClickListener(v -> {
            if (selectedDates.isEmpty()) {
                Toast.makeText(this, "가능한 날짜를 1개 이상 선택해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedTimes.isEmpty()) {
                Toast.makeText(this, "가능한 시간대를 1개 이상 선택해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "응답이 저장되었습니다", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ResponseSelectionActivity.this, RoomDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private static class DateItem {
        String day;
        String label;
        String week;

        DateItem(String day, String label, String week) {
            this.day = day;
            this.label = label;
            this.week = week;
        }
    }
}