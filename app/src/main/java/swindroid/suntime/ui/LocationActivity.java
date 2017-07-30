package swindroid.suntime.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import swindroid.suntime.R;

/**
 * Created by sandhu on 22/09/2016.
 */

public class LocationActivity extends Activity {
    private ArrayAdapter adapter;
    private ArrayList<Location> locations;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ListView lv = (ListView) findViewById(R.id.location_list);
        try {
            locations = getLocations();
            Collections.reverse(locations);
            adapter = new ListAdapter(this,R.layout.location_list_item,locations);


        } catch (IOException e) {
            Log.e("Error", e.toString());
        }
            lv.setAdapter(adapter);
        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent resultIntent = new Intent();
                ArrayList<Location> dataList = new ArrayList<Location>();
                dataList.add(locations.get(i));
                resultIntent.putParcelableArrayListExtra("LOCATION_DATA", dataList);
                setResult(RESULT_OK, resultIntent);
                finish();
                //Toast.makeText(getApplicationContext(),i+"", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private ArrayList<Location> getLocations() throws IOException {
        ArrayList<Location> locations = new ArrayList<Location>() ;
        ArrayList<String> data = FileIO.readFromFile("au_location.txt",this.getApplicationContext());
        for(String d : data)
        {
            String[] s = d.split(",");
                if (s.length == 4) ;
                locations.add(new Location(s[0], s[1], s[2], s[3]));
        }
        return locations;
    }


}
