package jp.co.wakwak.passmiru.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.co.wakwak.passmiru.Data.CreatedEvent;
import jp.co.wakwak.passmiru.R;

public class CreatedEventListAdapter extends ArrayAdapter<CreatedEvent> {
    final static String TAG = CreatedEventListAdapter.class.getSimpleName();

    static class ViewHolder {
        @InjectView(R.id.listImage)
        NetworkImageView mListImage;
        @InjectView(R.id.startedAt)
        TextView mStartedAt;
        @InjectView(R.id.accepted)
        TextView mAccepted;
        @InjectView(R.id.limit)
        TextView mLimit;
        @InjectView(R.id.ownerNickname)
        TextView mOwnerNickname;
        @InjectView(R.id.koukai_date)
        TextView mKoukaiDate;
        @InjectView(R.id.updateDate)
        TextView mUpdateDate;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public CreatedEventListAdapter(Context context, ArrayList<CreatedEvent> createdEvents) {
        super(context, R.layout.listrow, createdEvents);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listrow, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}
