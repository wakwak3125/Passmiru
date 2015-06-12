package jp.co.wakwak.passmiru.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Bus.PrefcSetResultBus;
import jp.co.wakwak.passmiru.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationSettingDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = LocationSettingDialog.class.getSimpleName();

    private static final String DIALOG_TITLE = "検索地域の設定";
    private static final String POSITIVE_BUTTON = "更新";
    private static final String NEGATIVE_BUTTON = "戻る";

    private static final String USER_PREF = "place_name";
    private static final String PREF_LIST = "prefecture";
    private static final String BOOLEAN_LIST = "booleans";

    private ArrayList<String> checkedPref;
    private ArrayAdapter<String> adapter;
    private SharedPreferences preferences;


    String[] prefList = {"北海道", "青森県", "岩手県", "宮城県", "秋田県",
            "山形県", "福島県", "茨城県", "栃木県", "群馬県", "埼玉県", "千葉県",
            "東京都", "神奈川県", "新潟県", "富山県", "石川県", "福井県", "山梨県",
            "長野県", "岐阜県", "静岡県", "愛知県", "三重県", "滋賀県", "京都府", "大阪府",
            "兵庫県", "奈良県", "和歌山県", "鳥取県", "島根県", "岡山県", "広島県", "山口県",
            "徳島県", "香川県", "愛媛県", "高知県", "福岡県", "佐賀県", "長崎県", "熊本県", "大分県",
            "宮崎県", "鹿児島県", "沖縄県"};

    ListView mPrefList;


    public LocationSettingDialog() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        preferences = getActivity().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        final SharedPreferences.Editor editor1 = preferences.edit();

        CheckedTextView checkedTextView = (CheckedTextView) getActivity().findViewById(android.R.id.text1);

        mPrefList = new ListView(getActivity());
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, prefList);
        mPrefList.setAdapter(adapter);

        mPrefList.setItemsCanFocus(false);
        mPrefList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        mPrefList.setOnItemClickListener(this);
        builder.setView(mPrefList);
        builder.setTitle(DIALOG_TITLE);
        builder.setPositiveButton(POSITIVE_BUTTON,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SparseBooleanArray checked = mPrefList.getCheckedItemPositions();
                        checkedPref = new ArrayList<String>();
                        editor.clear();
                        for (int i = 0; i < checked.size(); i++) {
                            if (checked.valueAt(i)) {
                                int key = checked.keyAt(i);
                                String checkedItem = String.valueOf(mPrefList.getItemAtPosition(key));
                                checkedPref.add(checkedItem);
                            }
                        }
                        String prefs = checkedPref.toString();
                        prefs = prefs.substring(1, prefs.length() - 1);
                        prefs = prefs.replace(" ","");
                        Log.i(TAG, prefs);
                        editor.putString(PREF_LIST, prefs);
                        editor.apply();
                        EventBus.getDefault().post(new PrefcSetResultBus(true));
                    }
                }).
                setNegativeButton(NEGATIVE_BUTTON,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        return builder.create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

