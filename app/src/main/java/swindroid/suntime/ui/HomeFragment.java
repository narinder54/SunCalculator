package swindroid.suntime.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ShareActionProvider;
import android.widget.TextView;

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

public class HomeFragment extends Fragment {
    private TextView loc;
    private Button add;
    private View rootView;
    private String shareMsg;

    private static final String ARG_SECTION_NUMBER = "section_number";


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
           int mParam1 = getArguments().getInt(ARG_SECTION_NUMBER);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences settings = getActivity().getSharedPreferences("My_Preferences", 0);
        boolean firstStart = settings.getBoolean("firstStart", true);

        if(firstStart) {
            moveToStorage();
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstStart", false);
            editor.commit();
        }
        loc = (TextView) rootView.findViewById(R.id.locationTV);
        add = (Button) rootView.findViewById(R.id.add_loc);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity().getBaseContext(), LocationActivity.class);
                startActivityForResult(in,0);

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getBaseContext(), AddLocationActivity.class);
                startActivity(i);
            }
        });
            initializeUI();
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_share);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_share){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMsg);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initializeUI()
    {
        DatePicker dp = (DatePicker) rootView.findViewById(R.id.datePicker);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        dp.init(year,month,day,dateChangeHandler); // setup initial values and reg. handler
        updateTime(year, month, day);
    }

    //Method triggered On Activity Intent result back
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
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
            ((MainActivity)getActivity()).setLocation(l);
            initializeUI();
        }
    }
    private void updateTime(int year, int monthOfYear, int dayOfMonth)
    {

        Location l = ((MainActivity)getActivity()).getLocation();
        GeoLocation geolocation = new GeoLocation(l.getCity_name(), Double.valueOf(l.getLatitude()), Double.valueOf(l.getLongitude()), TimeZone.getTimeZone(l.getTime_zone()));
        AstronomicalCalendar ac = new AstronomicalCalendar(geolocation);
        ac.getCalendar().set(year, monthOfYear, dayOfMonth);
        Date srise = ac.getSunrise();
        Date sset = ac.getSunset();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        TextView sunriseTV = (TextView) rootView.findViewById(R.id.sunriseTimeTV);
        TextView sunsetTV = (TextView) rootView.findViewById(R.id.sunsetTimeTV);
        Log.d("SUNRISE Unformatted", srise+"");
        loc.setText(((MainActivity)getActivity()).getLocation().getCity_name());
        sunriseTV.setText(sdf.format(srise));
        sunsetTV.setText(sdf.format(sset));
        this.shareMsg = "Location: "+l.getCity_name()+" Sun Rise: "+sdf.format(srise)+" and Sun Set: "+sdf.format(sset);
         }

    DatePicker.OnDateChangedListener dateChangeHandler = new DatePicker.OnDateChangedListener()
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
            in = getActivity().getAssets().open(FILE_NAME);
            out = getActivity().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser ) {
           // loc.setText(((MainActivity)getActivity()).getLocation().getCity_name());
        }
        else {
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
