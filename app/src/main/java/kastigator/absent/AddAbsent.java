package kastigator.absent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class AddAbsent extends AppCompatActivity {

    Calendar c;
    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;

    String status = "", saveDetail, date, time, subjectTitle;

    TextInputLayout subject;
    Button dateBtn, timeBtn, saveBtn;
    RadioButton presentBtn, absentBtn, attendBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_absent);

        subject = (TextInputLayout) findViewById(R.id.subjectName);
        dateBtn = (Button) findViewById(R.id.dateInput);
        timeBtn = (Button) findViewById(R.id.timeInput);
        saveBtn = (Button) findViewById(R.id.saveButton);
        presentBtn = (RadioButton) findViewById(R.id.presentButton);
        absentBtn = (RadioButton) findViewById(R.id.absentButton);
        attendBtn = (RadioButton) findViewById(R.id.attendButton);


        dateBtn.setOnClickListener(new OnClickListener() {      //opens date picker
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        timeBtn.setOnClickListener(new OnClickListener() {      //opens time picker
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });

        presentBtn.setOnClickListener(new OnClickListener() {       //disables absent radio button
            @Override
            public void onClick(View v) {
                absentBtn.setChecked(false);
                attendBtn.setChecked(false);
                status = "Present";
            }
        });

        absentBtn.setOnClickListener(new OnClickListener() {        //disables present radio button
            @Override
            public void onClick(View v) {
                presentBtn.setChecked(false);
                attendBtn.setChecked(false);
                status = "Absent";
            }
        });

        attendBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                presentBtn.setChecked(false);
                absentBtn.setChecked(false);
                status = "To Attend";
            }
        });

        saveBtn.setOnClickListener(new OnClickListener() {      //saves all the detail to file
            @Override
            public void onClick(View v) {
                //Save all the details
                saveDetail = "";
                date = dateBtn.getText().toString();
                time = timeBtn.getText().toString();
                subjectTitle = subject.getEditText().getText().toString();

                if (!date.equals("DATE")) {
                    if (!time.equals("TIME")) {
                        if (!subjectTitle.equals("")) {

                            if (!status.equals("")) {
                                saveDetail = subjectTitle + "=" + date + "=" + time + "=" + status + "*";
                                addDataToFile(saveDetail);
                            } else {
                                Toast.makeText(getBaseContext() , "Please select Present/Absent" , Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext() , "Please enter subject" , Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext() , "Please select a time" , Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext() , "Please select a date" , Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            //do whatever you want the 'Back' button to do
            //as an example the 'Back' button is set to start a new Activity named 'NewActivity'
            this.startActivity(new Intent(AddAbsent.this, MainDisplay.class));
            finish();
        }
        return true;
    }




    void addDataToFile(String data) {
        try {
            FileOutputStream fileout = openFileOutput("DataFile.txt", MODE_APPEND);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.append(data + "\n");
            outputWriter.close();
            Toast.makeText(getBaseContext(), "Saved! :)",
                    Toast.LENGTH_SHORT).show();
            this.startActivity(new Intent(AddAbsent.this, MainDisplay.class));
            finish();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Something went wrong! ;(",
                    Toast.LENGTH_SHORT).show();
        }
    }


    void datePicker() {     //shows date picker for selecting date

        // Get Current Date
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateData = "";
                        if (dayOfMonth < 10) {
                            dateData = "0" + dayOfMonth + "/";
                        } else {
                            dateData = dayOfMonth + "/";
                        }

                        if (monthOfYear < 10) {
                            dateData = dateData + "0" + (monthOfYear + 1) + "/" + year;
                        } else {
                            dateData = dateData + (monthOfYear + 1) + "/" + year;
                        }

                        dateBtn.setText(dateData);
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }



    void timePicker() {     //shows time picker for selecting time
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        /*
                        mHour = hourOfDay;
                        mMinute = minute;
                        */
                        String timeData = "";
                        if (hourOfDay < 10) {
                            timeData = "0" + hourOfDay + ":";
                        } else {
                            timeData = hourOfDay + ":";
                        }

                        if (minute < 10) {
                            timeData = timeData + "0" + minute;
                        } else {
                            timeData = timeData + minute;
                        }

                        timeBtn.setText(timeData);

                        //et_show_date_time.setText(dateString +" "+hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public Resources.Theme getTheme() {
        boolean useAlternativeTheme = false;
        StringBuilder themeText = new StringBuilder();
        try {
            FileOutputStream fileout = openFileOutput("ThemeFile.txt", MODE_APPEND);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.append("");
            outputWriter.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Themes may not work properly! 105 ;(",
                    Toast.LENGTH_SHORT).show();
        }
        try {
            FileInputStream fin = openFileInput("ThemeFile.txt");
            InputStreamReader myFile = new InputStreamReader(fin);
            BufferedReader fileRead = new BufferedReader(myFile);


            String line = "0";
            while (line != null) {      //Reads entire file
                line = fileRead.readLine();
                if (line != null) {
                    themeText.append(line);
                }
            }

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Themes may not work properly! 106 ;(",
                    Toast.LENGTH_SHORT).show();
        }

        if (themeText.toString().equals("Dark")) {
            useAlternativeTheme = true;
        }


        Resources.Theme theme = super.getTheme();
        if(useAlternativeTheme){
            theme.applyStyle(R.style.Theme_AppCompat, true);
        } else {
            theme.applyStyle(R.style.AppTheme, true);
        }
        // you could also use a switch if you have many themes that could apply
        return theme;
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

}