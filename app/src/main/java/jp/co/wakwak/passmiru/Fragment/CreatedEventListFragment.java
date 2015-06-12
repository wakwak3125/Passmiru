package jp.co.wakwak.passmiru.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import jp.co.wakwak.passmiru.Adapter.CreatedEventListAdapter;
import jp.co.wakwak.passmiru.ApiManage.EventDetailRequest;
import jp.co.wakwak.passmiru.ApiManage.UserEventRequest;
import jp.co.wakwak.passmiru.Bus.HideFragmentBus;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.Data.CreatedEvent;
import jp.co.wakwak.passmiru.R;

public class CreatedEventListFragment extends ListFragment {

    private OnFragmentInteractionListener   mListener;

    private UserEventRequest                userEventRequest;
    private EventDetailRequest              eventDetailRequest;

    private ArrayList<CreatedEvent>         createdEvents;
    private CreatedEventListAdapter         createdEventListAdapter;

    private Context                         context     = AppController.getContext();

    private static final String             PREF_KEY = "USER_NAME";
    private static final String             KEY_USER_NAME = "name";

    public CreatedEventListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.created_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(createdEventListAdapter);
        // ユーザー作成イベントリストの初期化
        createdEvents           = new ArrayList<CreatedEvent>();
        // イベントの詳細情報取得リクエストの初期化
        eventDetailRequest      = new EventDetailRequest();
        // アダプターの初期化
        createdEventListAdapter = new CreatedEventListAdapter(context, createdEvents);
        // アダプターをセット
        setListAdapter(createdEventListAdapter);
        // ユーザー情報に基づくイベントリクエストの初期化
        userEventRequest        = new UserEventRequest(createdEventListAdapter);
        // ユーザーが作成したイベントのリクエストを実行

        SharedPreferences preferences = getActivity().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        String userName = preferences.getString(KEY_USER_NAME, null);

        if (userName == null || userName.isEmpty() || userName.equals("")) {
            // 何もしない
            System.out.println("USER NAME IS NULL OR EMPTY.");
        } else {
            userEventRequest.getCreatedEvent();
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
            createdEventListAdapter.clear();
            createdEventListAdapter.notifyDataSetChanged();
        }
    }

}
