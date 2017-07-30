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

public class ListAdapter extends ArrayAdapter<Location> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<Location> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.location_list_item, null);
        }

        Location p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.location);
            if (tt1 != null) {
                tt1.setText(p.getCity_name());
            }


        }

        return v;
    }

}