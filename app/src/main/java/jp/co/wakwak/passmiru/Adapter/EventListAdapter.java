package jp.co.wakwak.passmiru.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jp.co.wakwak.passmiru.Data.Event;
import jp.co.wakwak.passmiru.R;

/**
 * Created by RyoSakaguchi on 15/04/29.
 */
public class EventListAdapter extends ArrayAdapter<Event> {

    final static String TAG = EventListAdapter.class.getSimpleName();

    public EventListAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.listrow, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listrow, parent, false);
        }

        TextView mEvent_id = (TextView)convertView.findViewById(R.id.id);
        TextView mTitle = (TextView)convertView.findViewById(R.id.title);

        Event event = getItem(position);

        mEvent_id.setText(String.valueOf(event.getEvent_id()));
        mTitle.setText(event.getTitle());

        return convertView;
    }

    /*public void swap(List<Event> objects) {
        clear();
        for (Event event : objects) {
            add(event);
        }
        notifyDataSetChanged();
    }*/
}
