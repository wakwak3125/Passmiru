package jp.co.wakwak.passmiru;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.games.event.EventBuffer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import jp.co.wakwak.passmiru.Adapter.EventListAdapter;
import jp.co.wakwak.passmiru.Adapter.PagerAdapter;
import jp.co.wakwak.passmiru.ApiManage.EventsRequest;
import jp.co.wakwak.passmiru.Bus.HideFragmentBus;
import jp.co.wakwak.passmiru.Data.Event;
import jp.co.wakwak.passmiru.Fragment.CreatedEventListFragment;
import jp.co.wakwak.passmiru.Fragment.EventListFragment;
import jp.co.wakwak.passmiru.Fragment.JoinEventListFragment;
import jp.co.wakwak.passmiru.Fragment.LocationSettingDialog;
import jp.co.wakwak.passmiru.Fragment.SearchResultListFragment;
import jp.co.wakwak.passmiru.Fragment.ThisAppFragment;
import jp.co.wakwak.passmiru.Fragment.UserEventFragment;
import jp.co.wakwak.passmiru.Fragment.UserIDSettingDialog;


public class MainActivity extends AppCompatActivity
        implements
        EventListFragment.OnFragmentInteractionListener,
        UserEventFragment.OnFragmentInteractionListener,
        SearchResultListFragment.OnFragmentInteractionListener,
        JoinEventListFragment.OnFragmentInteractionListener,
        CreatedEventListFragment.OnFragmentInteractionListener{

    private static String TAG = MainActivity.class.getSimpleName();

    private PagerAdapter pagerAdapter;
    private EventsRequest eventsRequest;
    private EventListAdapter adapter;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment registerFragment;

    private static final String KEY_WORD = "KEY_WORD";

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.pager)
    ViewPager pager;
    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("passmiru");

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        pager.setAdapter(pagerAdapter);
        tabStrip.setDividerColor(getResources().getColor(android.R.color.transparent));
        tabStrip.setTextColor(getResources().getColorStateList(R.color.tabcolors));
        tabStrip.setBackgroundColor(getResources().getColor(R.color.primary));
        tabStrip.setViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView mSearchView = (SearchView) toolbar.getMenu().findItem(R.id.menu_search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String keyWord = query.replace(" ", ",");

                Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                intent.putExtra(KEY_WORD, keyWord);
                startActivity(intent);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case (R.id.action_settings):
                DialogFragment userIDSetDialog = new UserIDSettingDialog();
                userIDSetDialog.show(getSupportFragmentManager(),null);
                return true;

            case (R.id.about_this_app):
                DialogFragment thisAppFragment = new ThisAppFragment();
                thisAppFragment.show(getSupportFragmentManager(),null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    /*public void onEvent(HideFragmentBus hideFragmentBus) {
        if (hideFragmentBus.isSuccess()) {
            pagerAdapter.destroyAllItem(pager);
            pagerAdapter.notifyDataSetChanged();
            pager.setAdapter(pagerAdapter);
        }
    }*/
}
