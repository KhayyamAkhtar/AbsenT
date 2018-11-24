package kastigator.absent;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class SettingActivity extends AppCompatActivity {

    RadioButton darkBtn, lightBtn;
    Button applyBtn;
    String themeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        darkBtn = (RadioButton) findViewById(R.id.darkTheme);
        lightBtn = (RadioButton) findViewById(R.id.lightTheme);
        applyBtn = (Button) findViewById(R.id.apllyButton);

        darkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lightBtn.setChecked(false);
                themeStatus = "Dark";
            }
        });

        lightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darkBtn.setChecked(false);
                themeStatus = "Light";
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (darkBtn.isChecked() == true || lightBtn.isChecked() == true) {
                    saveTheme();
                    finish();
                    startActivity(new Intent(SettingActivity.this , SettingActivity.class));
                } else {
                    Toast.makeText(getBaseContext() , "Select a theme first!" , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void saveTheme() {
        try {
            FileOutputStream fileout = openFileOutput("ThemeFile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(themeStatus);
            outputWriter.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext() , "Theme settings did not work! 107 ;(" , Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            //do whatever you want the 'Back' button to do
            finish();
            startActivity(new Intent(SettingActivity.this, MainDisplay.class));
        }
        return true;
    }

}
