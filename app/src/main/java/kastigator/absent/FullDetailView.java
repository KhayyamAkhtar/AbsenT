package kastigator.absent;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Stack;

public class FullDetailView extends AppCompatActivity {

    String name, date, time, status;

    TextView subjectText, dateText, timeText, statusText;
    Button deleteBtn;

    Stack<String> dataStack;

    String tempName = "", tempStatus = "";
    String tempDate = "", tempTime = "";
    String tempChar = "";
    String listValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_detail_view);

        subjectText = (TextView) findViewById(R.id.className);
        dateText = (TextView) findViewById(R.id.dateShow);
        timeText = (TextView) findViewById(R.id.timeShow);
        statusText = (TextView) findViewById(R.id.statusShow);
        deleteBtn = (Button) findViewById(R.id.deleteButton);

        dataStack = new Stack();

        Intent mainIntent = getIntent();
        listValue = mainIntent.getExtras().getString("listItem");
        findDataInFile(listValue);
        showData();

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
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
            finish();
        }
        return true;
    }


    void deleteData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete Data?");
        builder.setMessage("Are you sure to permanently delete current data?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                        startActivity(new Intent(FullDetailView.this , MainDisplay.class));
                        finish();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing if canceled
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    void delete() {
        tempName = subjectText.getText().toString();
        tempDate = dateText.getText().toString();
        tempTime = timeText.getText().toString();
        tempStatus = statusText.getText().toString();

        String toAvoid = tempName + "=" + tempDate + "=" + tempTime + "=" + tempStatus + "*";

        StringBuilder newText = new StringBuilder();

        try {       //takes data from file
            FileInputStream fin = openFileInput("DataFile.txt");
            InputStreamReader myFile = new InputStreamReader(fin);
            BufferedReader fileRead = new BufferedReader(myFile);

            String line = "0";
            while (line != null) {      //Reads entire file
                line = fileRead.readLine();
                if (line != null) {
                    if (!line.equals(toAvoid)) {
                        newText.append(line + "\n");
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext() , "Error in Initializing! 104 ;(" , Toast.LENGTH_SHORT).show();
        }

        try {
            FileOutputStream fileout = openFileOutput("DataFile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(newText.toString());
            outputWriter.close();
            Toast.makeText(getBaseContext() , "Deleted!" , Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext() , "Error! Failed to delete! 103 ;(" , Toast.LENGTH_SHORT).show();
        }
    }


    void showData() {
        int typeCounter = 0;
        //0-> subject
        //1-> date
        //2-> time
        //3-> status
        int count, size = dataStack.size();
        String data, dataPart = "";
        for (int index = 0; index < size ; index++) {       //finds the correct data to display
            data = dataStack.pop();
            count = 0;
            while (count < data.length()) {
                tempChar = data.substring(count , count + 1);
                if (!tempChar.equals("=") && !tempChar.equals("*")) {
                    dataPart = dataPart + tempChar;
                } else {
                    if (typeCounter == 0) {
                        if (dataPart.length() > 10) {
                            if (dataPart.substring(0, 7).equals(tempName)) {
                                name = dataPart;
                                typeCounter++;
                            }
                        } else {
                            if (dataPart.equals(tempName)) {
                                name = dataPart;
                                typeCounter++;
                            }
                        }
                    } else {
                        if (typeCounter == 1) {
                            if (dataPart.equals(tempDate)) {
                                date = dataPart;
                                typeCounter++;
                            }
                        } else {
                            if (typeCounter == 2) {
                                if (dataPart.equals(tempTime)) {
                                    time = dataPart;
                                    typeCounter++;
                                }
                            } else {
                                if (typeCounter == 3) {
                                    if (dataPart.equals(tempStatus)) {
                                        status = dataPart;
                                        typeCounter++;
                                    }
                                }
                            }
                        }
                    }
                    dataPart = "";
                }
                count = count + 1;
                if (typeCounter > 3) {
                    break;
                }
            }
        }

        subjectText.setText(name);
        dateText.setText(date);
        timeText.setText(time);
        statusText.setText(status);
    }



    void findDataInFile(String checkData) {     //finds the correct data from file
        int counter = 0;
        while (counter < checkData.length()) {      //Sorts data from list intent
            tempChar = checkData.substring(counter, counter + 1);
            if (tempChar.equals(".")) {
                if (!checkData.substring(counter , counter + 3).equals("...")) {
                    counter = counter + 2;
                } else {
                    counter = counter + 5;
                }
                tempDate = checkData.substring(counter, counter + 10);
                counter = counter + 12;
                tempTime = checkData.substring(counter, counter + 5);
                counter = counter + 7;
                tempStatus = checkData.substring(counter, checkData.length() - 2);
                break;
            } else {
                tempName = tempName + tempChar;
            }
            counter = counter + 1;
        }

        StringBuilder oldData = new StringBuilder();

        try {       //takes data from file
            FileInputStream fin = openFileInput("DataFile.txt");
            InputStreamReader myFile = new InputStreamReader(fin);
            BufferedReader fileRead = new BufferedReader(myFile);

            String line = "0";
            while (line != null) {      //Reads entire file
                line = fileRead.readLine();
                if (line != null) {
                    oldData.append(line);
                    dataStack.push(line);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext() , "Error in Initializing! 102 ;(" , Toast.LENGTH_SHORT).show();
        }
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
