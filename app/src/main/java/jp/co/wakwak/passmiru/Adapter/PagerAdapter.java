package jp.co.wakwak.passmiru.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import jp.co.wakwak.passmiru.Fragment.EventListFragment;
import jp.co.wakwak.passmiru.Fragment.ProfileFragment;

/**
 * Created by RyoSakaguchi on 15/04/29.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = {"LIST", "PROFILE"};

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new EventListFragment();

            case 1:
                return new ProfileFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }
}
