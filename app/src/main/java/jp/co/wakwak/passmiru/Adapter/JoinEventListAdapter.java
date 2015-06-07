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
import jp.co.wakwak.passmiru.Data.JoinEvent;
import jp.co.wakwak.passmiru.R;

public class JoinEventListAdapter extends ArrayAdapter<JoinEvent> {
    final static String TAG = JoinEventListAdapter.class.getSimpleName();

    static class ViewHolder {
        @InjectView(R.id.listImage)
        NetworkImageView mListImage;
        @InjectView(R.id.startedAt)
        TextView mTitle;
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

    public JoinEventListAdapter(Context context, ArrayList<JoinEvent> joinEvents) {
        super(context, R.layout.listrow, joinEvents);
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

        JoinEvent joinEvent = getItem(position);
        String limit = joinEvent.getLimit();

        if (limit.equals("null")) {
            holder.mLimit.setText("定員なし");
        } else {
            holder.mLimit.setText(limit);
        }

        holder.mTitle.setText(joinEvent.getTitle());
        holder.mAccepted.setText(joinEvent.getAccepted());
        holder.mOwnerNickname.setText(joinEvent.getOwner_nickname());
        holder.mUpdateDate.setText(joinEvent.getUpdated_at());
        holder.mListImage.setImageUrl(joinEvent.getImgUrl(), AppController.getInstance().getImageLoader());

        return convertView;
    }
}
