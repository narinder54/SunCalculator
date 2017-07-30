package swindroid.suntime.ui;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import swindroid.suntime.R;

/**
 * Created by sandhu on 22/09/2016.
 */

public class RangeListAdapter extends ArrayAdapter<RangeItem> {

    public RangeListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public RangeListAdapter(Context context, int resource, List<RangeItem> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.range_list_item, null);
        }

        RangeItem item = getItem(position);

        if (item != null) {
            TextView list_tv_date = (TextView) v.findViewById(R.id.list_tv_date);
            TextView list_tv_sunrise = (TextView) v.findViewById(R.id.list_tv_sunrise);
            TextView list_tv_sunset = (TextView) v.findViewById(R.id.list_tv_sunset);
                    list_tv_date.setText(item.getDate());
                    list_tv_sunrise.setText(item.getSunrise());
                    list_tv_sunset.setText(item.getSunset());
        }

        return v;
    }

}