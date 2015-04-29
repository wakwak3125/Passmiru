package jp.co.wakwak.passmiru.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jp.co.wakwak.passmiru.Data.Event;
import jp.co.wakwak.passmiru.R;

/**
 * Created by RyoSakaguchi on 15/04/29.
 */
public class EventListAdapter extends ArrayAdapter<Event> {

    final static String TAG = EventListAdapter.class.getSimpleName();

    private Context context;

    public EventListAdapter(Context context) {
        super(context, R.layout.listrow);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listrow, parent, false);
        }

        TextView mEvent_id = (TextView)convertView.findViewById(R.id.id);
        TextView mTitle = (TextView)convertView.findViewById(R.id.title);

        Event event = getItem(position);
        mEvent_id.setText(String.valueOf(event.getId()));
        mTitle.setText(event.getTitle());

        Log.d(TAG, "ITEM AT ADAPTER = " + event.getTitle());

        return convertView;
    }

    public void swap(List<Event> objects) {
        clear();
        for (Event event : objects) {
            add(event);
        }
        notifyDataSetChanged();
    }
}
