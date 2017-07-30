package swindroid.suntime.ui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import swindroid.suntime.R;
import swindroid.suntime.calc.AstronomicalCalendar;
import swindroid.suntime.calc.GeoLocation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;

public class Main extends Activity 
{
	private TextView loc;
	private Button add;
	private Location location;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		SharedPreferences settings = getSharedPreferences("My_Preferences", 0);
		boolean firstStart = settings.getBoolean("firstStart", true);

		if(firstStart) {
			moveToStorage();
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("firstStart", false);
			editor.commit();
		}

        TimeZone tz = TimeZone.getDefault();
		this.location = new Location("Melbourne", "-37.50", "145.01", tz.toString());
        initializeUI();
		loc = (TextView) findViewById(R.id.locationTV);
		add = (Button) findViewById(R.id.add_loc);
		loc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent in = new Intent(getBaseContext(), LocationActivity.class);
				startActivityForResult(in,0);

			}
		});
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getBaseContext(), AddLocationActivity.class);
				startActivity(i);
			}
		});
    }

	//Method triggered On Activity Intent result back
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		if (intent == null)
			Log.i("ACTIVITY-RESULT-Intent", "IS NULL");
		else
		{
			Log.i("ACTIVITY-RESULT-Intent", "Has DATA");
			ArrayList<Location> locationData = intent.getParcelableArrayListExtra("LOCATION_DATA");
			Location l = locationData.get(0);
			loc.setText(l.getCity_name());
            Log.e("Location", l.toString());
			this.location = l;
            initializeUI();
		}
	}
	private void initializeUI()
	{
		DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		dp.init(year,month,day,dateChangeHandler); // setup initial values and reg. handler
		updateTime(year, month, day);
	}
    
	private void updateTime(int year, int monthOfYear, int dayOfMonth)
	{

        TimeZone tz = TimeZone.getDefault();
        GeoLocation geolocation = new GeoLocation(location.getCity_name(), Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()), TimeZone.getTimeZone(location.getTime_zone()));
		AstronomicalCalendar ac = new AstronomicalCalendar(geolocation);
		ac.getCalendar().set(year, monthOfYear, dayOfMonth);
		Date srise = ac.getSunrise();
		Date sset = ac.getSunset();
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		TextView sunriseTV = (TextView) findViewById(R.id.sunriseTimeTV);
		TextView sunsetTV = (TextView) findViewById(R.id.sunsetTimeTV);
		Log.d("SUNRISE Unformatted", srise+"");
		
		sunriseTV.setText(sdf.format(srise));
		sunsetTV.setText(sdf.format(sset));		
	}
	
	OnDateChangedListener dateChangeHandler = new OnDateChangedListener()
	{
		public void onDateChanged(DatePicker dp, int year, int monthOfYear, int dayOfMonth)
		{
			updateTime(year, monthOfYear, dayOfMonth);
		}	
	};

	public void moveToStorage() {
		String FILE_NAME = "au_location.txt";

			InputStream in = null;
			OutputStream out = null;
			try {
				in = getAssets().open(FILE_NAME);
				out = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

}