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
import jp.co.wakwak.passmiru.Adapter.SearchResultListAdapter;
import jp.co.wakwak.passmiru.ApiManage.EventSearchRequest;
import jp.co.wakwak.passmiru.ApiManage.SearchResultDetailRequest;
import jp.co.wakwak.passmiru.Bus.ResultListShowBus;
import jp.co.wakwak.passmiru.Bus.SearchResultBus;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.Data.SearchResult;
import jp.co.wakwak.passmiru.EventDetailActivity;
import jp.co.wakwak.passmiru.R;

public class SearchResultListFragment extends ListFragment implements AbsListView.OnScrollListener {

    private static final String TAG = SearchResultListFragment.class.getSimpleName();
    private static final String KEY_WORD = "KEY_WORD";

    private String mKeyword;
    private OnFragmentInteractionListener mListener;

    private EventSearchRequest searchRequest;
    private SearchResultDetailRequest detailRequest;

    private SearchResultListAdapter adapter;
    private ListView mListView;

    static final int INTERNAL_PROGRESS_CONTAINER_ID = 0x00ff0002;
    static final int INTERNAL_LIST_CONTAINER_ID = 0x00ff0003;

    private View mFooter;
    private boolean loading = true;
    private int previousTotal = 0;

    public SearchResultListFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        // EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        if (getArguments() != null) {
            mKeyword = getArguments().getString(KEY_WORD);
        } else {
            Log.d(TAG, "検索単語が設定されていません");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.result_list, container, false);
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

        ArrayList<SearchResult> results = new ArrayList<SearchResult>();
        adapter = new SearchResultListAdapter(getActivity(), results);
        setListAdapter(adapter);

        setListShown(false);

        getListView().addFooterView(mFooter, null, false);
        getListView().setOnScrollListener(this);

        searchRequest = new EventSearchRequest(adapter);
        searchRequest.getSearchResult(1, 3, mKeyword);

        detailRequest = new SearchResultDetailRequest();
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
        SearchResult result = (SearchResult)l.getItemAtPosition(position);
        int eventID = result.getEvent_id();
        String imgUrl = result.getImgUrl();
        detailRequest.getEventDetail(eventID, imgUrl);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

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
            searchRequest = new EventSearchRequest(adapter);
            searchRequest.getSearchResult(start, 3, mKeyword);
            loading = true;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    public void onEvent(ResultListShowBus event) {

        if (event.isSuccess()) {
            setListShown(true);
        } else {
            Toast.makeText(AppController.getContext(), "取得できませんでした…", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEvent(SearchResultBus resultBus) {
        if (resultBus.isSuccess()) {
            String description = resultBus.getDescription();
            String imgUrl = resultBus.getImgUrl();
            String title = resultBus.getEventTitle();
            String updated_at = resultBus.getUpdated_at();
            String catchMsg = resultBus.getCatchMsg();
            String eventPlace = resultBus.getEventPlace();
            String lat = resultBus.getLatitude();
            String lon = resultBus.getLongitude();
            String startedAt = resultBus.getStartedAt();
            String address = resultBus.getAddress();
            String ownerNickName = resultBus.getOwnerNickname();
            String ownerDisplayName = resultBus.getOwnerDisplayName();
            String hashTag = resultBus.getHashTag();

            Intent intent = new Intent(AppController.getContext(), EventDetailActivity.class);
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

            startActivity(intent);

        } else {
            Toast.makeText(AppController.getContext(), "取得できませんでした…", Toast.LENGTH_SHORT).show();
        }
    }

}
