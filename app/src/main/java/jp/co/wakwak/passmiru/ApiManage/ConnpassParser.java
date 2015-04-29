package jp.co.wakwak.passmiru.ApiManage;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.co.wakwak.passmiru.Data.Event;

/**
 * Created by RyoSakaguchi on 15/04/29.
 */
public class ConnpassParser {

    final static String TAG = ConnpassParser.class.getSimpleName();

    private static ConnpassParser sInstance;

    public static ConnpassParser getInstance() {
        if (sInstance == null) {
            sInstance = new ConnpassParser();
        }
        return sInstance;
    }

    public List<Event> EventParser(JSONObject object) throws JSONException {
        ArrayList<Event> eventArrayList = new ArrayList<>();
        JSONArray jsonArray = object.getJSONArray("events");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int event_id = jsonObject.getInt("event_id");
            String title = jsonObject.getString("title");
            Event event = new Event(event_id, title);
            eventArrayList.add(event);
            Log.d(TAG, "id = " + event.getId() + " title = " + event.getTitle());
        }
        return eventArrayList;
    }
}
