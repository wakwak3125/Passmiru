package jp.co.wakwak.passmiru.ApiManage;

import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Adapter.EventListAdapter;
import jp.co.wakwak.passmiru.Bus.ListShowBus;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.Data.Event;

/**
 * Created by RyoSakaguchi on 15/05/06.
 */
public class EventsRequest {

    final static String TAG = EventsRequest.class.getSimpleName();

    private EventListAdapter adapter;
    private ArrayList<Event> events;
    private Event event;

    public EventsRequest(EventListAdapter adapter) {
        this.adapter = adapter;
    }
    // 引数のstartは検索の出力開始位置(例:1であれば1件目から出力する)
    // orderには1,2,3のどれかを渡す。説明はリファレンスページで確認。
    public void getEvents(int start, int order, String keyWord) {

        events = new ArrayList<Event>();
        String url = "http://connpass.com/api/v1/event/?keyword=" + keyWord + "&start=" + start + "&order=" + order + "&count=10";

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray connpassEvents = response.getJSONArray("events");
                            for (int i = 0; i < connpassEvents.length(); i++) {
                                JSONObject connpassEvent = connpassEvents.getJSONObject(i);

                                int event_id = connpassEvent.getInt("event_id");
                                String title = connpassEvent.getString("title");
                                String event_url = connpassEvent.getString("event_url");
                                String limit = connpassEvent.getString("limit");
                                String accepted = connpassEvent.getString("accepted");
                                String owner_nickname = connpassEvent.getString("owner_nickname");
                                String updated_at = connpassEvent.getString("updated_at");

                                event = new Event();
                                event.setEvent_id(event_id);
                                event.setTitle(title);
                                event.setEvent_url(event_url);
                                event.setLimit(limit);
                                event.setAccepted(accepted);
                                event.setOwner_nickname(owner_nickname);
                                event.setUpdated_at(updated_at);

                                HtmlParseTask task = new HtmlParseTask(event, event_url, adapter);
                                task.execute();
                                events.add(event);
                            }
                            adapter.addAll(events);
                            EventBus.getDefault().post(new ListShowBus(true));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AppController.getContext(), "取得できませんでした", Toast.LENGTH_SHORT).show();
                    }
                });
        AppController.getInstance().addToRequestQueue(request);
    }

    // イベントのURLからイベントイメージURLを取得するクラス
    private class HtmlParseTask extends AsyncTask<Void, Void, String> {
        private Event event;
        private String eventUrl;
        private EventListAdapter adapter;

        public HtmlParseTask(Event event, String eventUrl, EventListAdapter adapter) {
            this.event = event;
            this.eventUrl = eventUrl;
            this.adapter = adapter;
        }
        @Override
        protected String doInBackground(Void... params) {
            String imgUrl = null;
            try {
                Document document = Jsoup.connect(eventUrl).get();
                Elements elements = document.select("meta[itemprop=image]");
                imgUrl = elements.attr("content");
                event.setImgUrl(imgUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return imgUrl;
        }
        @Override
        protected void onPostExecute(String imgUrl) {
            super.onPostExecute(imgUrl);
            adapter.notifyDataSetChanged();
        }
    }
}
