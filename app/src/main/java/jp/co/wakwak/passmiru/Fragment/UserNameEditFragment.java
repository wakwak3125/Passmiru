package jp.co.wakwak.passmiru.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserNameEditFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private static final String PREF_KEY = "USER_NAME";
    private static final String KEY_USER_NAME = "name";

    private Context context = AppController.getContext();


    public UserNameEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_name_edit, container, false);
        return view;

    }

}
