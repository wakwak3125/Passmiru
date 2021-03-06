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
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.Data.Event;
import jp.co.wakwak.passmiru.R;

public class EventListAdapter extends ArrayAdapter<Event> {

    final static String TAG = EventListAdapter.class.getSimpleName();

    static class ViewHolder {

        @InjectView(R.id.listImage)
        NetworkImageView listImage;
        @InjectView(R.id.startedAt)
        TextView title;
        @InjectView(R.id.updateDate)
        TextView updateDate;
        @InjectView(R.id.limit)
        TextView limit;
        @InjectView(R.id.accepted)
        TextView accepted;
        @InjectView(R.id.ownerNickname)
        TextView ownerNickname;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

    }

    public EventListAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.listrow, events);
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

        Event event = getItem(position);
        String limit = event.getLimit();
        String imgUrl = event.getImgUrl();

        holder.title.setText(event.getTitle());
        if (limit.equals("null")) {
            holder.limit.setText("定員なし");
        } else {
            holder.limit.setText(limit);
        }

        holder.accepted.setText(event.getAccepted());
        holder.ownerNickname.setText(event.getOwner_nickname());
        holder.updateDate.setText(event.getUpdated_at());

        holder.listImage.setDefaultImageResId(R.drawable.noimage);
        holder.listImage.setErrorImageResId(R.drawable.noimage);
        holder.listImage.setImageUrl(imgUrl, AppController.getInstance().getImageLoader());

        return convertView;
    }

}
