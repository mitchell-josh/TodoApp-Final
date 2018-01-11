package com.example.joshmitchell.noteapp.Fragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.joshmitchell.noteapp.Activity.NoteListActivity;
import com.example.joshmitchell.noteapp.DB.NoteModel;
import com.example.joshmitchell.noteapp.Model.Note;
import com.example.joshmitchell.noteapp.R;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.example.joshmitchell.noteapp.Fragment.AlarmReceiver.EXTRA_NOTE_CONTENT;
import static com.example.joshmitchell.noteapp.Fragment.AlarmReceiver.EXTRA_NOTE_TITLE;

/**
 * Created by Josh Mitchell on 10/01/2018.
 */

public class ReminderFragment extends Fragment {

    public static final String EXTRA_NOTE_ID = "ReminderFragment.java";

    private Note mNote;
    private int mYear, mMonth, mDay;
    private int mHour, mMinute;
    Calendar calendar;
    TimeZone utc;

    public static ReminderFragment newInstance(long noteId){
        Bundle args = new Bundle();

        args.putLong(EXTRA_NOTE_ID, noteId);

        ReminderFragment fragment = new ReminderFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        long NoteId = getArguments().getLong(EXTRA_NOTE_ID);
        //Refactor for loaders
        mNote = NoteModel.get(getActivity()).getTextNote(NoteId);
        utc = TimeZone.getTimeZone("GMT");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reminder_fragment, parent, false);

        final TextView dateView = v.findViewById(R.id.dateselector);
        dateView.setText(Calendar.YEAR + " - " + Calendar.MONTH + 1 + " - " + Calendar.DAY_OF_MONTH + "              ");
        dateView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                calendar = Calendar.getInstance(utc);
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(year) +"-"+String.valueOf(monthOfYear)
                                +"-"+String.valueOf(dayOfMonth);
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        calendar.set(mYear, mMonth, mDay);
                        dateView.setText(date);
                    }
                }, mYear, mMonth, mDay);
                datePicker.show();
            }
        });

        final TextView timeView = v.findViewById(R.id.timeselector);
        timeView.setText(Calendar.HOUR_OF_DAY + 1 + " " + Calendar.MINUTE);
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar= Calendar.getInstance();
                calendar.setTimeInMillis(new Date().getTime());
                mHour = calendar.get(Calendar.HOUR_OF_DAY + 1);
                mMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                        Log.d("ReminderFragment", String.valueOf(hourOfDay) + " " + String.valueOf(minuteOfHour));
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minuteOfHour);
                        timeView.setText(mHour + " " + mMinute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePicker.show();
            }
        });

        Spinner spinner = v.findViewById(R.id.repeatspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.repeater_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button submitButton = v.findViewById(R.id.setreminder);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNotification();
            }
        });


        return v;
    }

    public void setNotification(){
        AlarmManager alarmManager = (AlarmManager)getContext()
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra(EXTRA_NOTE_TITLE, mNote.getTitle());
        intent.putExtra(EXTRA_NOTE_CONTENT, mNote.getContent());
        intent.putExtra(EXTRA_NOTE_ID, mNote.getId());
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), (int)mNote.getId(), intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }

}

