package jp.co.wakwak.passmiru.ApiManage;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Bus.SearchResultBus;
import jp.co.wakwak.passmiru.Commons.AppController;

public class SearchResultDetailRequest {
    final static String TAG = SearchResultDetailRequest.class.getSimpleName();

    public void getEventDetail(int event_id, final String imgUrl) {

        String url = "http://connpass.com/api/v1/event/?event_id=" + event_id;

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "RESPONSE = " + response);

                        try {
                            JSONArray userEvents = response.getJSONArray("events");
                            for (int i = 0; i < userEvents.length(); i++) {
                                JSONObject event = userEvents.getJSONObject(i);
                                int eventId = event.getInt("event_id");
                                String eventTitle = event.getString("title");
                                String description = event.getString("description");
                                String updatedAt = event.getString("updated_at");
                                String catchMsg = event.getString("catch");
                                String eventPlace = event.getString("place");
                                String lat = event.getString("lat");
                                String lon = event.getString("lon");
                                String startedAt = event.getString("started_at");
                                String address = event.getString("address");
                                String ownerNickname = event.getString("owner_nickname");
                                String ownerDisplayName = event.getString("owner_display_name");
                                String hashTag = event.getString("hash_tag");

                                EventBus.getDefault().post(new SearchResultBus(
                                        true,
                                        eventId,
                                        eventTitle,
                                        description,
                                        imgUrl,
                                        updatedAt,
                                        catchMsg,
                                        eventPlace,
                                        lat,
                                        lon,
                                        startedAt,
                                        address,
                                        ownerNickname,
                                        ownerDisplayName,
                                        hashTag));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        AppController.getInstance().addToRequestQueue(request);
    }
}
