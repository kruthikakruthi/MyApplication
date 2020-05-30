package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookingCompletedActivity extends AppCompatActivity
{


    private EditText pickdate,picktime;
    private Button Bookingbtn;
    private RadioButton gpay,phnpe,paytm;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_completed);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        pickdate = findViewById(R.id.pickdate);
        picktime = findViewById(R.id.picktime);
        gpay = findViewById(R.id.gpay);
        phnpe = findViewById(R.id.phonepe);
        paytm = findViewById(R.id.paytm);
        Bookingbtn = findViewById(R.id.bookingbtn);


        pickdate.setInputType(InputType.TYPE_NULL);
        picktime.setInputType(InputType.TYPE_NULL);



        pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdatedialog(pickdate);
            }
        });
        picktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showtimedialog(picktime);
            }
        });
    }

    private void showtimedialog(final EditText picktime) {

        final Calendar calendar=Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY,mHour);
                calendar.set(Calendar.MINUTE,mMinute);

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                picktime.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };
        new TimePickerDialog(BookingCompletedActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
    }

    private void showdatedialog(final EditText pickdate) {

        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener  dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR,mYear);
                calendar.set(Calendar.MONTH,mMonth);
                calendar.set(Calendar.DAY_OF_MONTH,mDay);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM/dd/yy");

                pickdate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new  DatePickerDialog(BookingCompletedActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }





















}
