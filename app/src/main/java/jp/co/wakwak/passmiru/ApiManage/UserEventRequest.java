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
import jp.co.wakwak.passmiru.Bus.UserEventBus;
import jp.co.wakwak.passmiru.Commons.AppController;

public class UserEventRequest {
    final static String TAG = UserEventRequest.class.getSimpleName();

    public void getUserEvent() {

        String url = "http://connpass.com/api/v1/event/?nickname=wakwak3125";

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "RESPONSE = " + response);

                        try {
                            JSONArray userEvents = response.getJSONArray("events");
                            for(int i = 0; i < userEvents.length(); i++){
                                JSONObject event = userEvents.getJSONObject(i);
                                int eventId = event.getInt("event_id");
                                String eventTitle = event.getString("title");
                                EventBus.getDefault().post(new UserEventBus(true, eventId, eventTitle));
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
