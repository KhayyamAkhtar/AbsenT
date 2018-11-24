package kastigator.absent;

import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;

public class MainDisplay extends AppCompatActivity {

    FloatingActionButton addBtn;
    ListView dataList;

    Stack<String> dataStack;

    String[] dataArray;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display);

         dataStack = new Stack();

        addBtn = (FloatingActionButton) findViewById(R.id.addButton);
        dataList = (ListView) findViewById(R.id.fileList);

        fillStack();

        if (!(dataStack.size() > 0)) {
            showFirstScreen();
        } else {
            dataArray = new String[dataStack.size()];
            fillArray();
            adapter = new ArrayAdapter<String>(this, R.layout.single_list, dataArray);
            dataList.setAdapter(adapter);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNewData();
                    finish();
                }
            });
            dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String listValue = (String) adapter.getItem(position);
                    Intent fullDetailIntent = new Intent(MainDisplay.this , FullDetailView.class);
                    fullDetailIntent.putExtra("listItem" , listValue);
                    startActivity(fullDetailIntent);
                }
            });
        }


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
                aboutIntent = new Intent(MainDisplay.this , AboutAppActivity.class);
                startActivity(aboutIntent);
                //finish();
                return true;
            case R.id.aboutDev:
                aboutIntent = new Intent(MainDisplay.this , AboutDevActivity.class);
                startActivity(aboutIntent);
                //finish();
                return true;
            case R.id.settings:
                aboutIntent = new Intent(MainDisplay.this , SettingActivity.class);
                startActivity(aboutIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
            //finish();
            finishAffinity();
        }
        return true;
    }



    void fillArray() {      //Adds data in an array for listview
        String fullData = "";
        String dataPart = "";
        String tempChar, data;

        //string changes to data effects visualization only
        //does not effect data in file
        boolean subjectData = true;
        int counter, size = dataStack.size();
        for (int index = 0; index < size ; index++) {
            data = dataStack.pop();
            counter = 0;
            while (counter < data.length()) {
                tempChar = data.substring(counter , counter + 1);
                if (!tempChar.equals("=") && !tempChar.equals("*")) {
                    dataPart = dataPart + data.substring(counter , counter + 1);
                } else {
                    if (dataPart.length() > 10) {
                        dataPart = dataPart.substring(0 , 7) + "...";
                    }
                    if (subjectData == true) {
                        dataPart = dataPart + ".\n";
                        fullData = fullData + dataPart;
                        subjectData = false;
                    } else {
                        fullData = fullData + dataPart + "  ";
                    }
                    dataPart = "";
                }
                counter = counter + 1;
            }
            dataArray[index] = fullData;
            fullData = "";
            subjectData = true;
        }
    }


    void fillStack() {      //Adds data in stack basically to reverse sort the data
        try {
            FileOutputStream fileout = openFileOutput("DataFile.txt", MODE_APPEND);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.append("");
            outputWriter.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext() , "Error in Initializing! 101 ;(" , Toast.LENGTH_SHORT).show();
        }

        StringBuilder oldData = new StringBuilder();

        try {
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


    void showFirstScreen() {
        Intent screenIntent = new Intent(MainDisplay.this, NoDataActivity.class);
        startActivity(screenIntent);
    }

    void addNewData() {
        Intent addIntent = new Intent(MainDisplay.this , AddAbsent.class);
        startActivity(addIntent);
    }
}
