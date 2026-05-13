package com.hazyala.pickday.kopo.ac.kr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class SelectionActivity extends AppCompatActivity {

    private View btnBack;
    private TextView btnNext, btnCalendarToggle;
    private View selectionCalendar;

    private TextView tvDateCount, tvExcludeCount;

    private final Set<View> selectedDates = new HashSet<>();
    private final Set<TextView> selectedTimes = new HashSet<>();
    private final Set<TextView> selectedExcludeDates = new HashSet<>();

    private static final int MAX_DATE_COUNT = 10;

    private final int PURPLE = Color.parseColor("#6A4DFF");
    private final int DARK_TEXT = Color.parseColor("#59566F");
    private final int WHITE = Color.parseColor("#FFFFFF");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        initViews();
        initButtons();
        initDateChips();
        initTimeChips();
        initExcludeDateChips();
        updateDateCount();
        updateExcludeCount();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        btnCalendarToggle = findViewById(R.id.btnCalendarToggle);

        selectionCalendar = findViewById(R.id.selectionCalendar);

        tvDateCount = findViewById(R.id.tvDateCount);
        tvExcludeCount = findViewById(R.id.tvExcludeCount);
    }

    private void initButtons() {
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, CreateMeetupActivity.class);
            startActivity(intent);
            finish();
        });

        btnNext.setOnClickListener(v -> {
            if (selectedDates.isEmpty()) {
                Toast.makeText(this, "후보 날짜를 1개 이상 선택해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedTimes.isEmpty()) {
                Toast.makeText(this, "시간대를 1개 이상 선택해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(SelectionActivity.this, InviteMembersActivity.class);
            startActivity(intent);
        });

        btnCalendarToggle.setOnClickListener(v -> {
            if (selectionCalendar.getVisibility() == View.VISIBLE) {
                selectionCalendar.setVisibility(View.GONE);
                btnCalendarToggle.setText("▣  달력 보기");
            } else {
                selectionCalendar.setVisibility(View.VISIBLE);
                btnCalendarToggle.setText("▣  닫기");
            }
        });
    }

    private void initDateChips() {
        LinearLayout date0521 = findViewById(R.id.date0521);
        LinearLayout date0522 = findViewById(R.id.date0522);
        LinearLayout date0523 = findViewById(R.id.date0523);
        LinearLayout date0524 = findViewById(R.id.date0524);
        LinearLayout date0525 = findViewById(R.id.date0525);
        LinearLayout date0526 = findViewById(R.id.date0526);
        LinearLayout date0527 = findViewById(R.id.date0527);

        View[] dateViews = {
                date0521, date0522, date0523, date0524,
                date0525, date0526, date0527
        };

        for (View dateView : dateViews) {
            dateView.setOnClickListener(v -> toggleDateChip(v));
        }
    }

    private void toggleDateChip(View dateView) {
        boolean isSelected = selectedDates.contains(dateView);

        if (isSelected) {
            selectedDates.remove(dateView);
            setDateChipSelected(dateView, false);
        } else {
            if (selectedDates.size() >= MAX_DATE_COUNT) {
                Toast.makeText(this, "최대 10개까지 선택할 수 있어요", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedDates.add(dateView);
            setDateChipSelected(dateView, true);
        }

        updateDateCount();
    }

    private void setDateChipSelected(View dateView, boolean selected) {
        dateView.setBackgroundResource(selected
                ? R.drawable.pickday_selected
                : R.drawable.pickday_unselected);

        if (dateView instanceof LinearLayout) {
            LinearLayout layout = (LinearLayout) dateView;

            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);

                if (child instanceof TextView) {
                    TextView textView = (TextView) child;

                    if (selected) {
                        textView.setTextColor(PURPLE);
                    } else {
                        textView.setTextColor(DARK_TEXT);
                    }

                    if ("✓".contentEquals(textView.getText())) {
                        textView.setVisibility(selected ? View.VISIBLE : View.GONE);
                        textView.setTextColor(selected ? WHITE : PURPLE);
                        textView.setBackgroundResource(selected
                                ? R.drawable.pickday_button_primary
                                : R.drawable.pickday_card_white);
                    }
                }
            }
        }
    }

    private void updateDateCount() {
        tvDateCount.setText("✓ 최대 10개까지 선택할 수 있어요 (" + selectedDates.size() + "/10)");
    }

    private void initTimeChips() {
        TextView timeMorning = findViewById(R.id.timeMorning);
        TextView timeAfternoon = findViewById(R.id.timeAfternoon);
        TextView timeLateAfternoon = findViewById(R.id.timeLateAfternoon);
        TextView timeEvening = findViewById(R.id.timeEvening);
        TextView timeLateEvening = findViewById(R.id.timeLateEvening);
        TextView timeAny = findViewById(R.id.timeAny);

        TextView[] timeViews = {
                timeMorning, timeAfternoon, timeLateAfternoon,
                timeEvening, timeLateEvening, timeAny
        };

        for (TextView timeView : timeViews) {
            timeView.setOnClickListener(v -> toggleTimeChip((TextView) v));
        }
    }

    private void toggleTimeChip(TextView timeView) {
        boolean isSelected = selectedTimes.contains(timeView);

        if (isSelected) {
            selectedTimes.remove(timeView);
            setTimeChipSelected(timeView, false);
        } else {
            selectedTimes.add(timeView);
            setTimeChipSelected(timeView, true);
        }
    }

    private void setTimeChipSelected(TextView timeView, boolean selected) {
        String text = timeView.getText().toString();
        text = text.replace("✓", "").replace("○", "").trim();

        if (selected) {
            timeView.setBackgroundResource(R.drawable.pickday_selected);
            timeView.setTextColor(PURPLE);
            timeView.setText("✓  " + text);
        } else {
            timeView.setBackgroundResource(R.drawable.pickday_unselected);
            timeView.setTextColor(DARK_TEXT);
            timeView.setText("○  " + text);
        }
    }

    private void initExcludeDateChips() {
        TextView exclude0529 = findViewById(R.id.exclude0529);
        TextView exclude0530 = findViewById(R.id.exclude0530);
        TextView exclude0531 = findViewById(R.id.exclude0531);
        TextView exclude0601 = findViewById(R.id.exclude0601);
        TextView exclude0605 = findViewById(R.id.exclude0605);
        TextView exclude0606 = findViewById(R.id.exclude0606);

        TextView[] excludeViews = {
                exclude0529, exclude0530, exclude0531,
                exclude0601, exclude0605, exclude0606
        };

        for (TextView excludeView : excludeViews) {
            excludeView.setOnClickListener(v -> toggleExcludeChip((TextView) v));
        }
    }

    private void toggleExcludeChip(TextView excludeView) {
        boolean isSelected = selectedExcludeDates.contains(excludeView);

        if (isSelected) {
            selectedExcludeDates.remove(excludeView);
            setExcludeChipSelected(excludeView, false);
        } else {
            selectedExcludeDates.add(excludeView);
            setExcludeChipSelected(excludeView, true);
        }

        updateExcludeCount();
    }

    private void setExcludeChipSelected(TextView excludeView, boolean selected) {
        if (selected) {
            excludeView.setBackgroundResource(R.drawable.pickday_selected);
            excludeView.setTextColor(PURPLE);
        } else {
            excludeView.setBackgroundResource(R.drawable.pickday_unselected);
            excludeView.setTextColor(DARK_TEXT);
        }
    }

    private void updateExcludeCount() {
        tvExcludeCount.setText(selectedExcludeDates.size() + "개 선택  ⌄");
    }
}