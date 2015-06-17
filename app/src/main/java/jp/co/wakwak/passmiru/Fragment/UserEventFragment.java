package jp.co.wakwak.passmiru.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Adapter.CreatedEventListAdapter;
import jp.co.wakwak.passmiru.Adapter.JoinEventListAdapter;
import jp.co.wakwak.passmiru.ApiManage.UserEventRequest;
import jp.co.wakwak.passmiru.ApiManage.UserInformationScraper;
import jp.co.wakwak.passmiru.Bus.HideFragmentBus;
import jp.co.wakwak.passmiru.Bus.UserInfoBus;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.Commons.CircleTransform;
import jp.co.wakwak.passmiru.Data.CreatedEvent;
import jp.co.wakwak.passmiru.Data.JoinEvent;
import jp.co.wakwak.passmiru.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserEventFragment extends Fragment implements TabHost.OnTabChangeListener {

    private static String TAG = UserEventFragment.class.getSimpleName();

    @InjectView(R.id.profileImage)
    ImageView mProfileImage;
    @InjectView(R.id.profileName)
    TextView mProfileName;
    @InjectView(R.id.profileDescription)
    TextView mProfileDescription;
    @InjectView(R.id.tabHost)
    FragmentTabHost mTabHost;
    @InjectView(R.id.coverImage)
    ImageView covetImageView;


    private UserInformationScraper userInformationScraper;

    private FragmentManager fm;
    private FragmentTransaction ft;
    private UserNameEditFragment userNameEditFragment;

    private UserEventRequest joinEventReq;
    private UserEventRequest createdEventReq;

    private String userName;

    private static final Context context = AppController.getContext();
    private static final String PREF_KEY = "USER_NAME";
    private static final String KEY_USER_NAME = "name";

    public UserEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        userName = sharedPreferences.getString(KEY_USER_NAME, null);

        ArrayList<JoinEvent> joinEvents = new ArrayList<JoinEvent>();
        ArrayList<CreatedEvent> createdEvents = new ArrayList<CreatedEvent>();

        JoinEventListAdapter joinEventListAdapter = new JoinEventListAdapter(context, joinEvents);
        CreatedEventListAdapter createdEventListAdapter = new CreatedEventListAdapter(context, createdEvents);

        joinEventReq       = new UserEventRequest(joinEventListAdapter);
        createdEventReq    = new UserEventRequest(createdEventListAdapter);

        if (userName == null || userName.isEmpty() || userName.equals("") || userName.equals(" ") || userName.equals("　")) {
            fm = getChildFragmentManager();
            ft = fm.beginTransaction();
            userNameEditFragment = new UserNameEditFragment();
            ft.add(R.id.container, userNameEditFragment);
            ft.commit();
        } else {
            HideEditFragment();
            userInformationScraper = new UserInformationScraper(userName);
        }
        EventBus.getDefault().register(this);
    }

    public void HideEditFragment(){
        fm = getChildFragmentManager();
        userNameEditFragment = new UserNameEditFragment();
        ft = fm.beginTransaction().hide(userNameEditFragment);
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_userevent, container, false);
        ButterKnife.inject(this, view);

        Picasso.with(AppController.getContext()).load(R.drawable.coverimageblur).into(covetImageView);

        mTabHost.setup(AppController.getContext(), getChildFragmentManager(), android.R.id.tabcontent);

        TabHost.TabSpec joinTab = mTabHost.newTabSpec("joinTab").setIndicator("参加");
        mTabHost.addTab(joinTab, JoinEventListFragment.class, null);

        TabHost.TabSpec createdTab = mTabHost.newTabSpec("createdTab").setIndicator("企画");
        mTabHost.addTab(createdTab, CreatedEventListFragment.class, null);

        try {
            userInformationScraper.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onTabChanged(String tabId) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String id);
    }


    public void onEvent(UserInfoBus infoBus) {
        if (infoBus.isSuccess()) {
            mProfileName.setText(infoBus.getUserName());
            mProfileDescription.setText(infoBus.getProfileDescription());
            Picasso.with(AppController.getContext()).load(infoBus.getProfileImgUrl()).transform(new CircleTransform()).into(mProfileImage);
        } else {
            Toast.makeText(AppController.getContext(), "取得できませんでした…", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEvent(HideFragmentBus hideFragmentBus) {
        if (hideFragmentBus.isSuccess()) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
            userName = sharedPreferences.getString(KEY_USER_NAME, null);
            joinEventReq.getUserEvent();
            createdEventReq.getCreatedEvent();
            userInformationScraper = new UserInformationScraper(userName);
            userInformationScraper.execute();
            HideEditFragment();
        }
    }
}