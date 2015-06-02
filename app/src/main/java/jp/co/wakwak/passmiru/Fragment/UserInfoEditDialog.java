package jp.co.wakwak.passmiru.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.Toast;

import jp.co.wakwak.passmiru.Commons.AppController;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoEditDialog extends DialogFragment {

    private Context context = AppController.getContext();

    public UserInfoEditDialog() {
        // Required empty public constructor
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info_edit_dialog, container, false);
    }
*/
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final EditText editText = new EditText(AppController.getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("connpassのアカウントを入力してください")
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, editText.getText().toString(),Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
