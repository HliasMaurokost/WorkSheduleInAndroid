package com.example.wsg;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wsg.helpers.DailyShifts;
import com.example.wsg.helpers.ScheduleReader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewSchedule extends AppCompatActivity {

    private static final String TABLE_SCHEDULE = "Schedule";
    private static final String WEEK = "Week";
    private static final int COLOR_BLACK = Color.parseColor("#000000");
    private static final int PIXELS_2 = 2;
    private static final int MAX_WEEKS = Schedule.NUMBER_OF_WEEKS + 1;
    private LinearLayout scheduleList;

    /**
     * Λαμβάνει δεδομένα από την βάση και  απεικονίζει το πρόγραμμα εργασίας
     * σε μια λίστα εύκολη ανάγνωσης
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        scheduleList = findViewById(R.id.scheduleList);

        DatabaseReference employeeDbRef = FirebaseDatabase.getInstance().getReference(TABLE_SCHEDULE).child("Weeks");
        employeeDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, List<DailyShifts>> dailyShiftsMap = new HashMap<>();

                for (DataSnapshot weekDataSnap : dataSnapshot.getChildren()) {
                    ScheduleReader currentWeek = new ScheduleReader(
                            (Map<String, Map<String, Map<String, String>>>) weekDataSnap.getValue());
                    dailyShiftsMap.put(weekDataSnap.getKey(), currentWeek.getWorkingEmployees());
                }

                for (int j = 1; j < MAX_WEEKS; j++) {

                    TextView weekName = new TextView(ViewSchedule.this);
                    weekName.setText(StringUtils.join(WEEK, j));
                    textViewConfigs(weekName, Color.parseColor("#a25ead"));

                    List<DailyShifts> week = dailyShiftsMap.get((StringUtils.join(WEEK, j)));

                    for (DailyShifts day : Objects.requireNonNull(week)) {

                        TextView dayName = new TextView(ViewSchedule.this);
                        dayName.setText(day.getDay());
                        textViewConfigs(dayName, Color.parseColor("#6c007d"));

                        TextView labelMorning = new TextView(ViewSchedule.this);
                        labelMorning.setText("Morning Shift");
                        textViewConfigs(labelMorning, COLOR_BLACK);

                        TextView morningShift = new TextView(ViewSchedule.this);
                        morningShift.setText(day.getMorningShift());
                        textViewConfigs(morningShift, COLOR_BLACK);

                        TextView labelAfternoon = new TextView(ViewSchedule.this);
                        labelAfternoon.setText("Afternoon Shift");
                        textViewConfigs(labelAfternoon, COLOR_BLACK);

                        TextView afternoonShift = new TextView(ViewSchedule.this);
                        afternoonShift.setText(day.getAfternoonShift());
                        textViewConfigs(afternoonShift, COLOR_BLACK);

                        TextView labelNight = new TextView(ViewSchedule.this);
                        labelNight.setText("Night Shift");
                        textViewConfigs(labelNight, COLOR_BLACK);

                        TextView nightShift = new TextView(ViewSchedule.this);
                        nightShift.setText(day.getNightShift());
                        textViewConfigs(nightShift, COLOR_BLACK);

                        View view = new View(ViewSchedule.this);
                        view.setMinimumHeight(PIXELS_2);
                        view.setBackgroundColor(Color.parseColor("#ff0044"));
                        scheduleList.addView(view);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Προκαθορισμένες ρυθμίσεις για την εμφάνιση κειμένου
     * @param textViewToBeAdded: Κείμενο που είναι να προστεθεί στην οθόνη
     * @param color: Το χρώμα του κειμένου
     */
    private void textViewConfigs(TextView textViewToBeAdded, int color) {
        textViewToBeAdded.setTextSize(24);
        textViewToBeAdded.setTextColor(color);
        textViewToBeAdded.setPadding(10, 10, 10, 50);
        scheduleList.addView(textViewToBeAdded);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}

