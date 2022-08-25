package com.example.meowing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.meowing.databinding.FragmentPrenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PrenFragment extends Fragment implements View.OnClickListener {

    private static String m1 = "8.30";
    private static String m2 = "10.30";
    private static String p1 = "15.00";
    private static String p2 = "17.00";
    private FragmentPrenBinding binding;
    private CalendarView calendarView;
    private Button matt1Button;
    private Button matt2Button;
    private Button pom1Button;
    private Button pom2Button;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference prenRef;
    private String selectedData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPrenBinding.inflate(inflater);

        calendarView = binding.calendar;
        matt1Button = binding.matt1;
        matt1Button.setOnClickListener(this);
        matt2Button = binding.matt2;
        matt2Button.setOnClickListener(this);
        pom1Button = binding.pom1;
        pom1Button.setOnClickListener(this);
        pom2Button = binding.pom2;
        pom2Button.setOnClickListener(this);

        calendarView.setMinDate(System.currentTimeMillis());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayofmonth) {
                String selectedYear = String.valueOf(year);
                String selectedMonth = String.valueOf(month);
                String selectedDay = String.valueOf(dayofmonth);
                selectedData = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                Log.i("selected", selectedData);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        prenRef = firebaseDatabase.getReference("Reservations");

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.matt1 :
                prenota(m1);
                break;
            case R.id.matt2 :
                prenota(m2);
                break;
            case R.id.pom1:
                prenota(p1);
                break;
            case R.id.pom2:
                prenota(p2);
                break;
        }

    }

    private void prenota(String time) {
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i("prenotazione", time + " " + selectedData);
        DatabaseReference reservation = prenRef.push();
        reservation.setValue(new Reservation(selectedData, time, user));
    }

}