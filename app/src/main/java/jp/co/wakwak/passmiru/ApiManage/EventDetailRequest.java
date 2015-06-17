package jp.co.wakwak.passmiru.ApiManage;

import android.text.format.DateUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Bus.CreatedEventDetailBus;
import jp.co.wakwak.passmiru.Bus.EventDetailBus;
import jp.co.wakwak.passmiru.Bus.SearchResultBus;
import jp.co.wakwak.passmiru.Bus.UserEventDetailBus;
import jp.co.wakwak.passmiru.Commons.AppController;

public class EventDetailRequest {
    final static String TAG = EventDetailRequest.class.getSimpleName();

    public void getEventDetail(int event_id, final String imgUrl, final int calledBy) {

        String url = "http://connpass.com/api/v1/event/?event_id=" + event_id;

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "RESPONSE = " + response);

                        try {
                            JSONArray userEvents = response.getJSONArray("events");
                            for (int i = 0; i < userEvents.length(); i++) {
                                JSONObject event        = userEvents.getJSONObject(i);
                                int eventId             = event.getInt("event_id");
                                String eventTitle       = event.getString("title");
                                String description      = event.getString("description");
                                String updatedAt        = event.getString("updated_at");
                                String catchMsg         = event.getString("catch");
                                String eventPlace       = event.getString("place");
                                String lat              = event.getString("lat");
                                String lon              = event.getString("lon");
                                String startedAt        = event.getString("started_at");
                                String address          = event.getString("address");
                                String ownerNickname    = event.getString("owner_nickname");
                                String ownerDisplayName = event.getString("owner_display_name");
                                String hashTag          = event.getString("hash_tag");
                                String eventType        = event.getString("event_type");



                                // TODO: 日付のフォーマット処理を書く。
                                Log.i(TAG, startedAt);
                                Date date;
                                FastDateFormat fastDateFormat = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT;
                                try {
                                    String ptrn1 = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern();
                                    date = org.apache.commons.lang3.time.DateUtils.parseDate(startedAt, ptrn1);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd (EEE) HH:mm", Locale.JAPANESE);
                                    startedAt = simpleDateFormat.format(date) + "〜";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (calledBy == 1) {
                                    // 新着イベントから読んだ時
                                    EventBus.getDefault().post(new EventDetailBus(
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
                                            hashTag,
                                            eventType));
                                } else if (calledBy == 2) {
                                    // 検索結果から読んだ時
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
                                            hashTag,
                                            eventType));
                                } else if (calledBy == 3) {
                                    // ユーザーの参加イベントから読んだ時
                                    EventBus.getDefault().post(new UserEventDetailBus(
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
                                            hashTag,
                                            eventType));
                                } else if (calledBy == 4) {
                                    // ユーザーの企画したイベントから読んだ時
                                    EventBus.getDefault().post(new CreatedEventDetailBus(
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
                                            hashTag,
                                            eventType));
                                }
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
