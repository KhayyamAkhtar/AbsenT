package kastigator.absent;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AboutDevActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            //do whatever you want the 'Back' button to do
            //as an example the 'Back' button is set to start a new Activity named 'NewActivity'
            this.startActivity(new Intent(AboutDevActivity.this, MainDisplay.class));
            finish();
        }
        return true;
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
