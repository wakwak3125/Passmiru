package jp.co.wakwak.passmiru.Fragment;

import android.app.Activity;
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
import jp.co.wakwak.passmiru.ApiManage.EventDetailRequest;
import jp.co.wakwak.passmiru.ApiManage.EventSearchRequest;
import jp.co.wakwak.passmiru.Bus.ResultListShowBus;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.Data.SearchResult;
import jp.co.wakwak.passmiru.R;

public class SearchResultListFragment extends ListFragment implements AbsListView.OnScrollListener {

    private static final String TAG = SearchResultListFragment.class.getSimpleName();
    private static final String KEY_WORD = "KEY_WORD";

    private String mKeyword;
    private OnFragmentInteractionListener mListener;

    private EventSearchRequest searchRequest;
    private EventDetailRequest eventDetailRequest;

    private ArrayList<SearchResult> results;
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
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        results = new ArrayList<SearchResult>();
        adapter = new SearchResultListAdapter(getActivity(), results);
        setListAdapter(adapter);

        setListShown(false);

        getListView().addFooterView(mFooter, null, false);
        getListView().setOnScrollListener(this);

        searchRequest = new EventSearchRequest(adapter);
        searchRequest.getSearchResult(1, 3, mKeyword);

        eventDetailRequest = new EventDetailRequest();
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

    public void onEvent(ResultListShowBus event) {

        if (event.isSuccess()) {
            setListShown(true);
        } else {
            Toast.makeText(AppController.getContext(), "取得できませんでした…", Toast.LENGTH_SHORT).show();
        }
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

}
