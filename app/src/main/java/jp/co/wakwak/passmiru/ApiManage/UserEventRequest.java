package jp.co.wakwak.passmiru.ApiManage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import jp.co.wakwak.passmiru.Adapter.CreatedEventListAdapter;
import jp.co.wakwak.passmiru.Adapter.JoinEventListAdapter;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.Data.CreatedEvent;
import jp.co.wakwak.passmiru.Data.JoinEvent;

public class UserEventRequest {
    final static String TAG = UserEventRequest.class.getSimpleName();

    private JoinEventListAdapter joinEventListAdapter;
    private CreatedEventListAdapter createdEventListAdapter;

    private JoinEvent joinEvent;
    private CreatedEvent createdEvent;

    private ArrayList<JoinEvent> joinEventsArrayList;
    private ArrayList<CreatedEvent> createdEvents;

    private SharedPreferences sharedPreferences;
    private static Context context = AppController.getContext();

    private String userName;
    private static final String PREF_KEY = "USER_NAME";
    private static final String KEY_USER_NAME = "name";

    public UserEventRequest(JoinEventListAdapter joinEventListAdapter) {
        this.joinEventListAdapter = joinEventListAdapter;
    }

    public UserEventRequest(CreatedEventListAdapter createdEventListAdapter) {
        this.createdEventListAdapter = createdEventListAdapter;
    }

    // ユーザーが参加したイベントのリクエスト
    public void getUserEvent() {
        sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        userName = sharedPreferences.getString(KEY_USER_NAME, null);
        joinEventsArrayList = new ArrayList<JoinEvent>();
        String url = "http://connpass.com/api/v1/event/?nickname=" + userName;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray userEvents = response.getJSONArray("events");
                            for (int i = 0; i < userEvents.length(); i++) {
                                JSONObject event = userEvents.getJSONObject(i);
                                int event_id = event.getInt("event_id");
                                String title = event.getString("title");
                                String event_url = event.getString("event_url");
                                String limit = event.getString("limit");
                                String accepted = event.getString("accepted");
                                String owner_nickname = event.getString("owner_nickname");
                                String updated_at = event.getString("updated_at");

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE);
                                Date date = null;

                                try {
                                    date = sdf.parse(updated_at);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                updated_at = sdf.format(date);

                                joinEvent = new JoinEvent();
                                joinEvent.setEvent_id(event_id);
                                joinEvent.setTitle(title);
                                joinEvent.setEvent_url(event_url);
                                joinEvent.setLimit(limit);
                                joinEvent.setAccepted(accepted);
                                joinEvent.setOwner_nickname(owner_nickname);
                                joinEvent.setUpdated_at(updated_at);

                                JoinEventHtmlParseTask task = new JoinEventHtmlParseTask(joinEvent, event_url, joinEventListAdapter);
                                task.execute();
                                joinEventsArrayList.add(joinEvent);
                            }
                            joinEventListAdapter.addAll(joinEventsArrayList);
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

    // ユーザーが企画したイベントのリクエスト
    public void getCreatedEvent() {
        sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        userName = sharedPreferences.getString(KEY_USER_NAME, null);
        String url = "http://connpass.com/api/v1/event/?owner_nickname=" + userName;
        createdEvents = new ArrayList<CreatedEvent>();
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray userEvents = response.getJSONArray("events");
                            for (int i = 0; i < userEvents.length(); i++) {
                                JSONObject event = userEvents.getJSONObject(i);
                                int event_id = event.getInt("event_id");
                                String title = event.getString("title");
                                String event_url = event.getString("event_url");
                                String limit = event.getString("limit");
                                String accepted = event.getString("accepted");
                                String owner_nickname = event.getString("owner_nickname");
                                String updated_at = event.getString("updated_at");

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE);
                                Date date = null;

                                try {
                                    date = sdf.parse(updated_at);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                updated_at = sdf.format(date);

                                createdEvent = new CreatedEvent();
                                createdEvent.setEvent_id(event_id);
                                createdEvent.setTitle(title);
                                createdEvent.setEvent_url(event_url);
                                createdEvent.setLimit(limit);
                                createdEvent.setAccepted(accepted);
                                createdEvent.setOwner_nickname(owner_nickname);
                                createdEvent.setUpdated_at(updated_at);

                                CreatedEventHtmlParseTask task = new CreatedEventHtmlParseTask(createdEvent, event_url, createdEventListAdapter);
                                task.execute();
                                createdEvents.add(createdEvent);
                            }
                            createdEventListAdapter.addAll(createdEvents);
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

    private class JoinEventHtmlParseTask extends AsyncTask<Void, Void, String> {
        private JoinEvent event;
        private String eventUrl;
        private JoinEventListAdapter adapter;

        public JoinEventHtmlParseTask(JoinEvent event, String eventUrl, JoinEventListAdapter adapter) {
            this.event = event;
            this.eventUrl = eventUrl;
            this.adapter = adapter;
        }

        @Override
        protected String doInBackground(Void... params) {
            String imgUrl = null;
            try {
                Document document = Jsoup.connect(eventUrl).get();
                Elements elements = document.select("div.group_inner.event_header_area").select("a");
                imgUrl = elements.attr("href");
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

    private class CreatedEventHtmlParseTask extends AsyncTask<Void, Void, String> {
        private CreatedEvent event;
        private String eventUrl;
        private CreatedEventListAdapter adapter;

        public CreatedEventHtmlParseTask(CreatedEvent event, String eventUrl, CreatedEventListAdapter adapter) {
            this.event = event;
            this.eventUrl = eventUrl;
            this.adapter = adapter;
        }

        @Override
        protected String doInBackground(Void... params) {
            String imgUrl = null;
            try {
                Document document = Jsoup.connect(eventUrl).get();
                Elements elements = document.select("div.group_inner.event_header_area").select("a");
                imgUrl = elements.attr("href");
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


