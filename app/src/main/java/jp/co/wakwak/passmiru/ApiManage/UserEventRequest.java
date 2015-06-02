package jp.co.wakwak.passmiru.ApiManage;

import android.os.AsyncTask;

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

import jp.co.wakwak.passmiru.Adapter.CreatedEventListAdapter;
import jp.co.wakwak.passmiru.Adapter.JoinEventListAdapter;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.Data.CreatedEvent;
import jp.co.wakwak.passmiru.Data.UserEvent;

public class UserEventRequest {
    final static String TAG = UserEventRequest.class.getSimpleName();

    private JoinEventListAdapter joinEventListAdapter;
    private CreatedEventListAdapter createdEventListAdapter;

    private UserEvent userEvent;
    private CreatedEvent createdEvent;

    private ArrayList<UserEvent> userEventsArrayList;
    private ArrayList<CreatedEvent> createdEvents;

    public UserEventRequest(JoinEventListAdapter joinEventListAdapter) {
        this.joinEventListAdapter = joinEventListAdapter;
    }

    public UserEventRequest(CreatedEventListAdapter createdEventListAdapter) {
        this.createdEventListAdapter = createdEventListAdapter;
    }

    // ユーザーが参加したイベントのリクエスト
    public void getUserEvent() {
        userEventsArrayList = new ArrayList<UserEvent>();
        String url = "http://connpass.com/api/v1/event/?nickname=wakwak3125";
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

                                userEvent = new UserEvent();
                                userEvent.setEvent_id(event_id);
                                userEvent.setTitle(title);
                                userEvent.setEvent_url(event_url);
                                userEvent.setLimit(limit);
                                userEvent.setAccepted(accepted);
                                userEvent.setOwner_nickname(owner_nickname);
                                userEvent.setUpdated_at(updated_at);

                                JoinEventHtmlParseTask task = new JoinEventHtmlParseTask(userEvent, event_url, joinEventListAdapter);
                                task.execute();
                                userEventsArrayList.add(userEvent);
                                // EventBus.getDefault().post(new UserEventBus(true, eventId, eventTitle));
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

    // ユーザーが企画したイベントのリクエスト
    public void getCreatedEvent() {
        String url = "http://connpass.com/api/v1/event/?owner_nickname=wakwak3125";
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
        private UserEvent event;
        private String eventUrl;
        private JoinEventListAdapter adapter;

        public JoinEventHtmlParseTask(UserEvent event, String eventUrl, JoinEventListAdapter adapter) {
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


