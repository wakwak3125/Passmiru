package jp.co.wakwak.passmiru.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Adapter.EventListAdapter;
import jp.co.wakwak.passmiru.ApiManage.ConnpassParser;
import jp.co.wakwak.passmiru.ApiManage.ConnpassRequests;
import jp.co.wakwak.passmiru.Bus.ShowListEvent;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.Data.Event;
import jp.co.wakwak.passmiru.R;

public class EventListFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;
    private ConnpassRequests connpassRequests;
    private ConnpassParser connpassParser;
    private EventListAdapter eventListAdapter;
    private List<Event> eventList;

    static final int INTERNAL_PROGRESS_CONTAINER_ID = 0x00ff0002;
    static final int INTERNAL_LIST_CONTAINER_ID = 0x00ff0003;

    public EventListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.event_list, container, false);
        ProgressBar pBar = (ProgressBar) view.findViewById(android.R.id.progress);
        LinearLayout pframe = (LinearLayout) pBar.getParent();
        pframe.setId(INTERNAL_PROGRESS_CONTAINER_ID);

        ListView listview = (ListView) view.findViewById(android.R.id.list);
        listview.setItemsCanFocus(false);
        FrameLayout frameLayout = (FrameLayout) listview.getParent();
        frameLayout.setId(INTERNAL_LIST_CONTAINER_ID);

        return view;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventListAdapter = new EventListAdapter(getActivity());
        ListView list = (ListView)getView().findViewById(android.R.id.list);
        list.setAdapter(eventListAdapter);
        ConnpassRequests.getInstance().
                getEvents(
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    List<Event> events = ConnpassParser.getInstance().EventParser(response);
                                    eventListAdapter.swap(events);
                                    setListShown(true);
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
    }

    public void onEvent(ShowListEvent event) {

        if (event.isSuccess()) {
            setListShown(true);
        } else {
            Toast.makeText(AppController.getmContext(), "取得できませんでした…", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String id);
    }
}
