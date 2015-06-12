package jp.co.wakwak.passmiru.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Adapter.JoinEventListAdapter;
import jp.co.wakwak.passmiru.ApiManage.EventDetailRequest;
import jp.co.wakwak.passmiru.ApiManage.UserEventRequest;
import jp.co.wakwak.passmiru.Bus.HideFragmentBus;
import jp.co.wakwak.passmiru.Bus.RelodeJoinListBus;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.Data.JoinEvent;
import jp.co.wakwak.passmiru.R;

public class JoinEventListFragment extends ListFragment {

    private OnFragmentInteractionListener   mListener;

    private UserEventRequest                userEventRequest;
    private EventDetailRequest              eventDetailRequest;

    private ArrayList<JoinEvent>            joinEvents;
    private JoinEventListAdapter            joinEventListAdapter;

    private Context                         context         = AppController.getContext();

    private static final String             PREF_KEY        = "USER_NAME";
    private static final String             KEY_USER_NAME   = "name";

    private static final String             ARGS_LIST_TYPE  = "listType";
    private static final String             MSG_CREATED     = "参加イベントがありません";

    public JoinEventListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.join_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // ユーザー作成イベントリストの初期化
        joinEvents              = new ArrayList<JoinEvent>();

        // アダプターの初期化
        joinEventListAdapter    = new JoinEventListAdapter(context, joinEvents);

        // アダプターをセット
        setListAdapter(joinEventListAdapter);

        // ユーザー情報に基づくイベントリクエストの初期化
        userEventRequest        = new UserEventRequest(joinEventListAdapter);

        // イベントの詳細情報取得リクエストの初期化
        eventDetailRequest      = new EventDetailRequest();

        // ユーザーが参加したイベントのリクエスト
        SharedPreferences preferences = getActivity().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        String userName = preferences.getString(KEY_USER_NAME, null);

        if (userName == null || userName.isEmpty() || userName.equals("")) {
            // 何もしない
            System.out.println("USER NAME IS NULL OR EMPTY.");
        } else {
            userEventRequest.getUserEvent();
        }

        // リストが空っぽだった場合の処理
        if (joinEventListAdapter.isEmpty()){
            ShowEmptyView();
        } else {
            HideEmptyView();
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    public void onEvent(HideFragmentBus hideFragmentBus) {
        if (hideFragmentBus.isSuccess()){
            userEventRequest.getUserEvent();
            joinEventListAdapter.clear();
            joinEventListAdapter.notifyDataSetChanged();
        }

        if (joinEventListAdapter.isEmpty()){
            ShowEmptyView();
        } else {
            HideEmptyView();
        }
    }

    public void ShowEmptyView() {
        if (joinEventListAdapter.isEmpty()){
            EmptyFragment emptyFragment = new EmptyFragment();
            Bundle args = new Bundle();
            args.putString(ARGS_LIST_TYPE, MSG_CREATED);
            emptyFragment.setArguments(args);
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, emptyFragment);
            ft.commit();
        }
    }

    public void HideEmptyView() {
        if (!joinEventListAdapter.isEmpty()) {
            EmptyFragment emptyFragment = new EmptyFragment();
            FragmentManager     fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(emptyFragment);
            ft.commit();
        }
    }
}
