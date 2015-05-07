package jp.co.wakwak.passmiru.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.ApiManage.UserEventRequest;
import jp.co.wakwak.passmiru.Bus.UserEventBus;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserEventFragment extends Fragment {

    private static String TAG = UserEventFragment.class.getSimpleName();

    private UserEventRequest userEventRequest;

    @InjectView(R.id.title)
    TextView mTitle;


    public UserEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_userevent, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userEventRequest = new UserEventRequest();
        userEventRequest.getUserEvent();
    }

    public void onEvent(UserEventBus userEventBus){
        if (userEventBus.isSuccess()){
            mTitle.setText(userEventBus.getEventTitle());
        } else {
            Toast.makeText(AppController.getmContext(), "取得できませんでした…", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String id);
    }

}
