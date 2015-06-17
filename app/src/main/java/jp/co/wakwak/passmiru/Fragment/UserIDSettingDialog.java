package jp.co.wakwak.passmiru.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Bus.HideFragmentBus;
import jp.co.wakwak.passmiru.Bus.PrefcSetResultBus;
import jp.co.wakwak.passmiru.Commons.AppController;
import jp.co.wakwak.passmiru.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserIDSettingDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = UserIDSettingDialog.class.getSimpleName();

    private static final String PREF_KEY = "USER_NAME";
    private static final String KEY_USER_NAME = "name";

    private static final String DIALOG_TITLE = "ユーザーIDの設定";
    private static final String POSITIVE_BUTTON = "OK";
    private static final String NEGATIVE_BUTTON = "戻る";

    private static final String HINT_TEXT = "IDを入力してください";
    private static final String EDIT_TEXT_WARN = "IDが空白です";

    private Context context = AppController.getContext();
    private UserNameEditFragment userNameEditFragment;

    @InjectView(R.id.editText2)
    EditText mUserNameEdit;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    ListView mPrefList;

    public UserIDSettingDialog() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.userid_edit_dialog, null, false);
        ButterKnife.inject(this, view);
        mUserNameEdit.setHint(HINT_TEXT);
        userNameEditFragment = new UserNameEditFragment();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setMessage(DIALOG_TITLE);
        builder.setPositiveButton(POSITIVE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String userId = mUserNameEdit.getText().toString();

                if (userId.equals("") || userId.isEmpty() || userId.contains(" ") || userId.length() == 0) {
                    Toast.makeText(context, EDIT_TEXT_WARN, Toast.LENGTH_SHORT).show();
                } else {
                    preferences = getActivity().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
                    editor = preferences.edit();
                    editor.clear();
                    editor.putString(KEY_USER_NAME, mUserNameEdit.getText().toString());
                    editor.apply();

                    EventBus.getDefault().post(new HideFragmentBus(true));
                    userNameEditFragment.DestroyMySelf();
                }

            }
        }).setNegativeButton(NEGATIVE_BUTTON, null);
        return builder.create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}

