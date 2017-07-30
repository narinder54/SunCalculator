package swindroid.suntime.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import swindroid.suntime.R;
import swindroid.suntime.calc.AstronomicalCalendar;
import swindroid.suntime.calc.GeoLocation;

public class RangeFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private DatePicker datePicker;
    private Calendar calendar;
    private Location location;
    private TextView tv_start_date, tv_end_date, tv_timezone;
    private Date start_date , end_date;
    private int year, month, day;
    private SimpleDateFormat sdf;
    private RangeListAdapter listAdapter;
    private ArrayList<RangeItem> rangeItems;
    private ListView lv_range;
    private OnFragmentInteractionListener mListener;
    View root;

    public RangeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RangeFragment newInstance(int param1) {
        RangeFragment fragment = new RangeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_range, container, false);
        TimeZone tz = TimeZone.getDefault();
        this.location = ((MainActivity)getActivity()).getLocation();
        rangeItems = new ArrayList<RangeItem>();
        listAdapter = new RangeListAdapter(getActivity().getApplicationContext(),R.layout.range_list_item,rangeItems);
        tv_start_date = (TextView) root.findViewById(R.id.tv_start_date);
        tv_end_date = (TextView) root.findViewById(R.id.tv_end_date);
        tv_timezone = (TextView) root.findViewById(R.id.tv_timezone);
        tv_timezone.setText(location.getCity_name());
        lv_range = (ListView) root.findViewById(R.id.range_list);
        lv_range.setAdapter(listAdapter);
            sdf= new SimpleDateFormat("dd/MM/yyyy");
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        tv_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DatePickerDialog dp =  new DatePickerDialog(getActivity(),  myStartDateListener, year, month,
                        day);
                dp.getDatePicker().setMinDate(new Date().getTime());
                dp.show();
            }
        });
        tv_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp = new DatePickerDialog(getActivity(),  myEndDateListener, year, month,
                        day);
                dp.getDatePicker().setMinDate(start_date.getTime());
                dp.show();
            }
        });
        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            this.location = ((MainActivity)getActivity()).getLocation();
            tv_timezone.setText(location.getCity_name());
            if(start_date!=null && end_date != null)
                UpdateAdapter();
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


    private DatePickerDialog.OnDateSetListener myStartDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker dp, int arg1, int arg2, int arg3) {
            tv_start_date.setText(new StringBuilder().append(arg3).append("/")
                    .append(arg2+1).append("/").append(arg1));
            try {
                start_date = sdf.parse(arg3+"/"+(arg2+1)+"/"+arg1);
                if(end_date!=null)
                    UpdateAdapter();
                else
                    Toast.makeText(getActivity().getApplicationContext(),"Please choos end date also",Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                Log.e("Error","Date set is not correct");
            }
        }
    };
    private DatePickerDialog.OnDateSetListener myEndDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            tv_end_date.setText(new StringBuilder().append(arg3).append("/")
                    .append(arg2+1).append("/").append(arg1));
            try {
                end_date = sdf.parse(arg3+"/"+(arg2+1)+"/"+arg1);
                if(start_date!=null)
                    UpdateAdapter();
                else
                    Toast.makeText(getActivity().getApplicationContext(),"Please choos start date also",Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                Log.e("Error","Date set is not correct");
            }
        }
    };

    private void UpdateAdapter(){
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(start_date);
        rangeItems.clear();
        while (!gcal.getTime().after(end_date)) {
            Date d = gcal.getTime();
            TimeZone tz = TimeZone.getDefault();
            GeoLocation geolocation = new GeoLocation(location.getCity_name(), Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()), TimeZone.getTimeZone(location.getTime_zone()));
            AstronomicalCalendar ac = new AstronomicalCalendar(geolocation);
            ac.getCalendar().set(d.getYear(),d.getMonth(),d.getDate());
            Date srise = ac.getSunrise();
            Date sset = ac.getSunset();
            SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
            rangeItems.add(new RangeItem(sdf.format(d),sd.format(srise),sd.format(sset)));
               Log.e("List Data",d.toString()+sd.format(srise)+sd.format(sset));
            gcal.add(Calendar.DAY_OF_MONTH, 1);
        }
        listAdapter.notifyDataSetChanged();
    }
}
