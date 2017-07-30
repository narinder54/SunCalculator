package swindroid.suntime.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.TimeZone;

import swindroid.suntime.R;

/**
 * Created by sandhu on 07/10/2016.
 */

public class AddLocationActivity extends Activity{
    private EditText l_name, l_latitude, l_longitude;
    private Spinner  l_timezone;
    private Button l_save;
    private String name, longitude, latitude, timezone;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        l_name = (EditText) findViewById(R.id.location_name);
        l_latitude = (EditText) findViewById(R.id.location_latitude);
        l_longitude = (EditText) findViewById(R.id.location_longitude);
        l_timezone = (Spinner) findViewById(R.id.location_timezone);
        l_save = (Button) findViewById(R.id.location_save);
        l_save.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        String[] idArray = TimeZone.getAvailableIDs();
        ArrayAdapter<String> idAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                idArray);
        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        l_timezone.setAdapter(idAdapter);

    }

    private void saveData() {
        name = l_name.getText().toString();
        latitude = l_latitude.getText().toString();
        longitude = l_longitude.getText().toString();
        timezone = l_timezone.getSelectedItem().toString();
        String data = name+","+latitude+","+longitude+","+timezone;
        try {
            if(!latitude.matches("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)$"))
                Toast.makeText(getApplicationContext(),"Latitude should be 0° to (+/–)90", Toast.LENGTH_SHORT).show();
            else if(!longitude.matches("\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$"))
                Toast.makeText(getApplicationContext(),"Longitude should be 0° to (+/–)180", Toast.LENGTH_SHORT).show();
            else {
                FileIO.addToFile(data + "\n", "au_location.txt", getApplicationContext());
                Toast.makeText(getApplicationContext(), "Location added", Toast.LENGTH_SHORT).show();
                l_name.setText(""); l_longitude.setText(""); l_latitude.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
