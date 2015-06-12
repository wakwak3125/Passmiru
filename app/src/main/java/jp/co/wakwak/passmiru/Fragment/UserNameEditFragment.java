package jp.co.wakwak.passmiru.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.BootstrapButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Bus.HideFragmentBus;
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

    @InjectView(R.id.submitButton)
    BootstrapButton mSubmitButton;
    @InjectView(R.id.editText)
    EditText mUserNameEdit;

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
        ButterKnife.inject(this, view);
        return view;

    }

    @OnClick(R.id.submitButton)
    public void Edit() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, mUserNameEdit.getText().toString());
        editor.apply();
        EventBus.getDefault().post(new HideFragmentBus(true));
        DestroyMySelf();
    }

    public void DestroyMySelf() {
        getFragmentManager().beginTransaction().remove(this).commit();
    }
}
