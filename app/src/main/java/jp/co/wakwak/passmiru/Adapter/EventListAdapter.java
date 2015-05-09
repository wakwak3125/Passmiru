package jp.co.wakwak.passmiru.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.co.wakwak.passmiru.Data.Event;
import jp.co.wakwak.passmiru.R;

/**
 * Created by RyoSakaguchi on 15/04/29.
 */
public class EventListAdapter extends ArrayAdapter<Event> {

    final static String TAG = EventListAdapter.class.getSimpleName();

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'listrow.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @InjectView(R.id.listImage)
        ImageView listImage;
        @InjectView(R.id.title)
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
            holder = (ViewHolder)convertView.getTag();
        }

        Event event = getItem(position);


        // これをバックグラウンド処理にする
        /*try {
            Document document = Jsoup.connect(event.getEvent_url()).get();
            Elements elements = document.select("meta[itemprop=image]");
            String imageUrl = elements.select("content").first().toString();
            Picasso.with(getContext()).load(imageUrl).into(holder.listImage);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        holder.title.setText(event.getTitle());

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
