package jp.co.wakwak.passmiru.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.co.wakwak.passmiru.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmptyFragment extends Fragment {

    private static final String ARGS_LIST_TYPE  = "listType";

    @InjectView(R.id.noEventMsg)
    TextView mNoEventMsg;

    public EmptyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        ButterKnife.inject(this, view);
        String msg = getArguments().getString(ARGS_LIST_TYPE, null);
        mNoEventMsg.setText(msg);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
