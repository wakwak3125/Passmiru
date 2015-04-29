package jp.co.wakwak.passmiru.ApiManage;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import jp.co.wakwak.passmiru.Commons.AppController;

/**
 * Created by RyoSakaguchi on 15/04/29.
 */
public class ConnpassRequests {

    private static ConnpassRequests sInstance;

    public static ConnpassRequests getInstance() {
        if (sInstance == null) {
            sInstance = new ConnpassRequests();
        }
        return sInstance;
    }

    final static String url = "http://connpass.com/api/v1/event/"; // 全てのイベント
    public void getEvents(Response.Listener<JSONObject> listener,
                          Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(url, null, listener, errorListener);
        AppController.getInstance().addToRequestQueue(request);
    }
}
