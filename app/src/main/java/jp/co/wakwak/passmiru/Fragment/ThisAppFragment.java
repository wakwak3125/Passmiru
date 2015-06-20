package jp.co.wakwak.passmiru.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.co.wakwak.passmiru.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThisAppFragment extends DialogFragment {


    @InjectView(R.id.twitterIcon)
    FontAwesomeText mTwitterIcon;
    @InjectView(R.id.facebookIcon)
    FontAwesomeText mFacebookIcon;
    @InjectView(R.id.textView4)
    TextView mPassmiru;
    @InjectView(R.id.twitterLink)
    TextView mTwitterLink;
    @InjectView(R.id.fbLink)
    TextView mFbLink;

    private static final String TWITTER_URL     = "https://twitter.com/wakwak3125";
    private static final String FACEBOOK_URL    = "https://www.facebook.com/wakwak3125";

    public ThisAppFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_this_app, null, false);
        ButterKnife.inject(this, view);

        MovementMethod mMethodTwit = LinkMovementMethod.getInstance();
        mTwitterLink.setMovementMethod(mMethodTwit);

        MovementMethod mMethodFb = LinkMovementMethod.getInstance();
        mFbLink.setMovementMethod(mMethodFb);

        CharSequence twitterLink = Html.fromHtml("<a href=" + TWITTER_URL + ">@wakwak3125</a>");
        mTwitterLink.setLinkTextColor(getResources().getColor(android.R.color.white));
        mTwitterLink.setText(twitterLink);

        CharSequence facebookLink = Html.fromHtml("<a href=" + FACEBOOK_URL + ">Ryo Sakaguchi</a>");
        mFbLink.setLinkTextColor(getResources().getColor(android.R.color.white));
        mFbLink.setText(facebookLink);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Dialog dialog = getDialog();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 1.0);  // 画面サイズの0.8倍の大きさに指定
        int dialogHeight = (int) (metrics.heightPixels * 1.0);  // 画面サイズの0.8倍の大きさに指定

        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
