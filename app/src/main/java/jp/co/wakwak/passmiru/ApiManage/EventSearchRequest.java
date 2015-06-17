package jp.co.wakwak.passmiru.ApiManage;

import android.os.AsyncTask;
import android.util.Log;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Adapter.SearchResultListAdapter;
import jp.co.wakwak.passmiru.Bus.ResultListShowBus;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.Data.SearchResult;

public class EventSearchRequest {
    // TODO:Edit Search Request.
    final static String TAG = EventSearchRequest.class.getSimpleName();

    private SearchResultListAdapter searchResultListAdapter;
    private ArrayList<SearchResult> results;
    private SearchResult result;

    public EventSearchRequest(SearchResultListAdapter searchResultListAdapter) {
        this.searchResultListAdapter = searchResultListAdapter;
    }

    public void getSearchResult(int start, int order, String keyWord) {

        results = new ArrayList<SearchResult>();
        final String url = "http://connpass.com/api/v1/event/?keyword=" + keyWord + "&start=" + start + "&order=" + order + "&count=10";

        final JsonObjectRequest searchResultRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray searchResults = response.getJSONArray("events");
                            for (int i = 0; i < searchResults.length(); i++) {
                                JSONObject searchResult = searchResults.getJSONObject(i);

                                int event_id = searchResult.getInt("event_id");
                                String title = searchResult.getString("title");
                                String event_url = searchResult.getString("event_url");
                                String limit = searchResult.getString("limit");
                                String accepted = searchResult.getString("accepted");
                                String owner_nickname = searchResult.getString("owner_nickname");
                                String updated_at = searchResult.getString("updated_at");

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE);
                                Date date = null;

                                try {
                                    date = sdf.parse(updated_at);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                updated_at = sdf.format(date);

                                result = new SearchResult();
                                result.setEvent_id(event_id);
                                result.setTitle(title);
                                result.setEvent_url(event_url);
                                result.setLimit(limit);
                                result.setAccepted(accepted);
                                result.setOwner_nickname(owner_nickname);
                                result.setUpdated_at(updated_at);

                                HtmlParseTask task = new HtmlParseTask(result, event_url, searchResultListAdapter);
                                task.execute();
                                results.add(result);
                            }
                            searchResultListAdapter.addAll(results);
                            EventBus.getDefault().post(new ResultListShowBus(true));
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
        AppController.getInstance().addToRequestQueue(searchResultRequest);
    }
    // イベントのURLからイベントイメージURLを取得するクラス
    private class HtmlParseTask extends AsyncTask<Void, Void, String> {
        private SearchResult searchResult;
        private String eventUrl;
        private SearchResultListAdapter searchResultListAdapter;

        public HtmlParseTask(SearchResult searchResult, String eventUrl, SearchResultListAdapter searchResultListAdapter) {
            this.searchResult = searchResult;
            this.eventUrl = eventUrl;
            this.searchResultListAdapter = searchResultListAdapter;
        }

        @Override
        protected String doInBackground(Void... params) {
            String imgUrl = null;
            try {
                Document document = Jsoup.connect(eventUrl).get();
                Elements elements = document.select("meta[itemprop=image]");
                imgUrl = elements.attr("content");
                searchResult.setImgUrl(imgUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return imgUrl;
        }

        @Override
        protected void onPostExecute(String imgUrl) {
            super.onPostExecute(imgUrl);
            searchResultListAdapter.notifyDataSetChanged();
        }
    }

}
