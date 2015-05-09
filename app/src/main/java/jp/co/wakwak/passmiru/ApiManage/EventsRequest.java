package jp.co.wakwak.passmiru.ApiManage;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public EventsRequest(EventListAdapter adapter) {
        this.adapter = adapter;
    }

    // 引数のstartは検索の出力開始位置(例:1であれば1件目から出力する)
    // orderには1,2,3のどれかを渡す。説明はリファレンスページで確認。
    public void getEvents(int start, int order) {

        events = new ArrayList<Event>();
        String url = "http://connpass.com/api/v1/event/?start=" + start + "&order=" + order + "&count=10";

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray connpassEvents = response.getJSONArray("events");
                            for(int i = 0; i < connpassEvents.length(); i++){
                                JSONObject connpassEvent = connpassEvents.getJSONObject(i);

                                int event_id = connpassEvent.getInt("event_id");
                                String title = connpassEvent.getString("title");
                                String event_url = connpassEvent.getString("event_url");

                                // これやると絶対落ちるで。(あたりまえ)
                                /*try {
                                    Document document = Jsoup.connect(event_url).get();
                                    Elements elements = document.select("meta[itemprop=image]");
                                    String imageUrl = elements.select("content").first().toString();
                                    Log.d(TAG, "imageUrl = " + imageUrl);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/

                                Event event = new Event();
                                event.setEvent_id(event_id);
                                event.setTitle(title);
                                event.setEvent_url(event_url);

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
                        Toast.makeText(AppController.getmContext(), "取得できませんでした", Toast.LENGTH_SHORT).show();
                    }
                });
        AppController.getInstance().addToRequestQueue(request);
    }
}
