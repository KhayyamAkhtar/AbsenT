package kastigator.absent;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class NoDataActivity extends AppCompatActivity {

    ImageView lightLogo;
    ImageView darkLogo;
    String themeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_data);

        FloatingActionButton addBtn = (FloatingActionButton) findViewById(R.id.addButton);
        lightLogo = (ImageView) findViewById(R.id.lightImage);
        darkLogo = (ImageView) findViewById(R.id.darkImage);

        if (themeStatus.equals("Dark")) {
            lightLogo.setVisibility(View.INVISIBLE);
        }
        if (themeStatus.equals("Light")) {
            darkLogo.setVisibility(View.INVISIBLE);
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewData();
            }
        });
    }

    void addNewData() {
        Intent addIntent = new Intent(NoDataActivity.this , AddAbsent.class);
        startActivity(addIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     //creates menu using menu resource directory
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //uses menu items
        Intent aboutIntent;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.aboutApp:
                aboutIntent = new Intent(NoDataActivity.this , AboutAppActivity.class);
                startActivity(aboutIntent);
                //finish();
                return true;
            case R.id.aboutDev:
                aboutIntent = new Intent(NoDataActivity.this , AboutDevActivity.class);
                startActivity(aboutIntent);
                //finish();
                return true;
            case R.id.settings:
                aboutIntent = new Intent(NoDataActivity.this , SettingActivity.class);
                startActivity(aboutIntent);
                //finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            //do whatever you want the 'Back' button to do
            //finish();
            finishAffinity();
        }
        return true;
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public Resources.Theme getTheme() {
        boolean useAlternativeTheme = false;
        themeStatus = "Light";
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
            themeStatus = "Dark";
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
