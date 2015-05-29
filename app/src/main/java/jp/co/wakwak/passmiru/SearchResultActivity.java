package jp.co.wakwak.passmiru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.co.wakwak.passmiru.Fragment.SearchResultListFragment;

public class SearchResultActivity extends AppCompatActivity implements SearchResultListFragment.OnFragmentInteractionListener {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.container)
    FrameLayout mContainer;

    private FragmentManager fm;
    private FragmentTransaction ft;
    private SearchResultListFragment searchResultListFragment;
    private static final String KEY_WORD = "KEY_WORD";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("検索結果");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        intent = getIntent();
        String mKeyword = intent.getStringExtra(KEY_WORD);

        fm = getSupportFragmentManager();
        ListFragment listFragment = new SearchResultListFragment();

        // 取得した検索単語をfragmentに渡す処理
        Bundle args = new Bundle();
        args.putString(KEY_WORD, mKeyword);
        listFragment.setArguments(args);

        ft = fm.beginTransaction();
        ft.replace(R.id.container, listFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
