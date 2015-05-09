package jp.co.wakwak.passmiru.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Adapter.EventListAdapter;
import jp.co.wakwak.passmiru.ApiManage.EventDetailRequest;
import jp.co.wakwak.passmiru.ApiManage.EventsRequest;
import jp.co.wakwak.passmiru.Bus.EventDetailBus;
import jp.co.wakwak.passmiru.Bus.ListShowBus;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.Data.Event;
import jp.co.wakwak.passmiru.EventDetailActivity;
import jp.co.wakwak.passmiru.R;

public class EventListFragment extends ListFragment implements AbsListView.OnScrollListener {

    final static String TAG = EventListFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private EventsRequest eventsRequest;
    private EventDetailRequest eventDetailRequest;

    private ArrayList<Event> events;
    private EventListAdapter adapter;
    private ListView mListView;
    private View mFooter;

    private int start;

    private boolean loading = true;
    private int previousTotal = 0;

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

        mFooter = inflater.inflate(R.layout.listfooter, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated");

        events = new ArrayList<Event>();

        adapter = new EventListAdapter(getActivity(), events);
        setListAdapter(adapter);

        setListShown(false);

        getListView().addFooterView(mFooter);
        getListView().setOnScrollListener(this);

        eventsRequest = new EventsRequest(adapter);
        eventsRequest.getEvents(1, 3);

        eventDetailRequest = new EventDetailRequest();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        // 初回アイテム追加時のメソッドコール防止(暫定)
        if (adapter == null || totalItemCount == 1) {
            return;
        }

        if (loading) {
            if (totalItemCount > previousTotal) {
                Log.d(TAG, "totalItemCount   = " + totalItemCount);
                Log.d(TAG, "previousTotal = " + previousTotal);
                previousTotal = totalItemCount;
                loading = false;
            }
        }
        if (!loading && totalItemCount == visibleItemCount + firstVisibleItem) {
            Log.d("onScroll", "Loading......");
            int start = adapter.getCount() + 1;
            eventsRequest = new EventsRequest(adapter);
            eventsRequest.getEvents(start, 3);
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

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
        Event event = (Event)l.getItemAtPosition(position);
        int eventID = event.getEvent_id();
        eventDetailRequest.getEventDetail(eventID);

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String id);
    }

    public void onEvent(ListShowBus event) {

        if (event.isSuccess()) {
            setListShown(true);
        } else {
            Toast.makeText(AppController.getmContext(), "取得できませんでした…", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEvent(EventDetailBus detailBus) {
        if (detailBus.isSuccess()) {
            String description = detailBus.getDescription();

            Intent intent = new Intent(AppController.getmContext(), EventDetailActivity.class);
            intent.putExtra("description", description);
            startActivity(intent);

        } else {
            Toast.makeText(AppController.getmContext(), "取得できませんでした…", Toast.LENGTH_SHORT).show();
        }
    }

}
