package jp.co.wakwak.passmiru.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Adapter.EventListAdapter;
import jp.co.wakwak.passmiru.ApiManage.EventDetailRequest;
import jp.co.wakwak.passmiru.ApiManage.EventsRequest;
import jp.co.wakwak.passmiru.Bus.EventDetailBus;
import jp.co.wakwak.passmiru.Bus.ListShowBus;
import jp.co.wakwak.passmiru.Bus.PrefcSetResultBus;
import jp.co.wakwak.passmiru.Bus.SwipeFinishBus;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.Data.Event;
import jp.co.wakwak.passmiru.EventDetailActivity;
import jp.co.wakwak.passmiru.R;

@SuppressWarnings("ResourceType")
public class EventListFragment extends ListFragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

    final static String TAG = EventListFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private Context context = AppController.getContext();

    private EventsRequest eventsRequest;
    private EventDetailRequest eventDetailRequest;

    private EventListAdapter adapter;
    private ListView mListView;
    private View mFooter;

    private SwipeRefreshLayout refreshLayout;
    private Handler handler = new Handler();

    private int start;

    private boolean loading = true;
    private int previousTotal = 0;

    static final int INTERNAL_PROGRESS_CONTAINER_ID = 0x00ff0002;
    static final int INTERNAL_LIST_CONTAINER_ID = 0x00ff0003;

    @InjectView(R.id.locSettingFAB)
    FloatingActionButton mLocSettingFAB;

    public EventListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_list, container, false);
        ButterKnife.inject(this, view);

        mLocSettingFAB.setVisibility(View.GONE);

        ProgressBar pBar = (ProgressBar) view.findViewById(android.R.id.progress);

        LinearLayout pframe = (LinearLayout) pBar.getParent();
        pframe.setId(INTERNAL_PROGRESS_CONTAINER_ID);

        ListView listview = (ListView) view.findViewById(android.R.id.list);
        listview.setItemsCanFocus(false);

        refreshLayout = (SwipeRefreshLayout)listview.getParent();
        refreshLayout.setId(INTERNAL_LIST_CONTAINER_ID);

        mFooter = inflater.inflate(R.layout.listfooter, container, false);

        return view;
    }

    @OnClick(R.id.locSettingFAB)
    public void LocationStting() {
        DialogFragment locSettingFragment = new LocationSettingDialog();
        locSettingFragment.show(getChildFragmentManager(),null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<Event> events = new ArrayList<Event>();
        adapter = new EventListAdapter(getActivity(), events);
        setListAdapter(adapter);

        setListShown(false);

        getListView().addFooterView(mFooter, null, false);
        getListView().setOnScrollListener(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light);

        eventsRequest = new EventsRequest(adapter);
        eventsRequest.getEvents(1, 3, 1);
        eventDetailRequest = new EventDetailRequest();
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh");
        handler.postDelayed(Refreshing, 500);
    }

    private void updateList(EventListAdapter adapter) {
        eventsRequest = new EventsRequest(adapter);
        eventsRequest.getEvents(1, 3, 2);
    }

    private void changePrefecture(EventListAdapter adapter) {
        eventsRequest = new EventsRequest(adapter);
        eventsRequest.getEvents(1, 3, 4);
    }

    private Runnable Refreshing = new Runnable() {
        @Override
        public void run() {
            updateList(adapter);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        // 初回アイテム追加時のメソッドコール防止(暫定)
        if (adapter == null || totalItemCount == 1) {
            return;
        }

        if (loading) {
            if (totalItemCount > previousTotal) {
                previousTotal = totalItemCount;
                loading = false;
            }
        }
        if (!loading && totalItemCount == visibleItemCount + firstVisibleItem) {
            Log.i("onScroll", "Loading......");
            int start = adapter.getCount() + 1;
            eventsRequest = new EventsRequest(adapter);
            eventsRequest.getEvents(start, 3, 3);
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
        Event event     = (Event)l.getItemAtPosition(position);
        int eventID     = event.getEvent_id();
        String imgUrl   = event.getImgUrl();
        eventDetailRequest.getEventDetail(eventID, imgUrl, 1);

        Log.d(TAG, "TITLE = " + event.getTitle());

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String id);
    }

    // ↓EventBus用のメソッド達
    public void onEvent(SwipeFinishBus finishBus) {
        if (finishBus.isSuccess()) {
            refreshLayout.setRefreshing(false);
            Log.d(TAG, "setRefreshing(false)");
        } else {
            Toast.makeText(AppController.getContext(), "取得できませんでした…", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEvent(ListShowBus event) {
        if (event.isSuccess()) {
            mLocSettingFAB.setVisibility(View.VISIBLE);
            setListShown(true);
        } else {
            Toast.makeText(AppController.getContext(), "取得できませんでした…", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEvent(PrefcSetResultBus prefcSetResultBus) {
        if (prefcSetResultBus.isSuccess()) {
            setListShown(false);
            mLocSettingFAB.setVisibility(View.GONE);
            changePrefecture(adapter);
        }
    }

    public void onEvent(EventDetailBus detailBus) {
        if (detailBus.isSuccess()) {
            String eventId          = String.valueOf(detailBus.getEventId());
            String description      = detailBus.getDescription();
            String imgUrl           = detailBus.getImgUrl();
            String title            = detailBus.getEventTitle();
            String updated_at       = detailBus.getUpdated_at();
            String catchMsg         = detailBus.getCatchMsg();
            String eventPlace       = detailBus.getEventPlace();
            String lat              = detailBus.getLatitude();
            String lon              = detailBus.getLongitude();
            String startedAt        = detailBus.getStartedAt();
            String address          = detailBus.getAddress();
            String ownerNickName    = detailBus.getOwnerNickname();
            String ownerDisplayName = detailBus.getOwnerDisplayName();
            String hashTag          = detailBus.getHashTag();
            String eventType        = detailBus.getEventType();

            Intent intent = new Intent(AppController.getContext(), EventDetailActivity.class);
            intent.putExtra("eventID", eventId);
            intent.putExtra("description", description);
            intent.putExtra("imgUrl", imgUrl);
            intent.putExtra("title", title);
            intent.putExtra("updated_at", updated_at);
            intent.putExtra("catch", catchMsg);
            intent.putExtra("eventPlace", eventPlace);
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            intent.putExtra("startedAt", startedAt);
            intent.putExtra("address", address);
            intent.putExtra("ownerNickName", ownerNickName);
            intent.putExtra("ownerDisplayName", ownerDisplayName);
            intent.putExtra("hashTag", hashTag);
            intent.putExtra("eventType", eventType);
            startActivity(intent);

        } else {
            Toast.makeText(AppController.getContext(), "取得できませんでした…", Toast.LENGTH_SHORT).show();
        }
    }

}
