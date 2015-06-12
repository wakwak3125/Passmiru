package jp.co.wakwak.passmiru.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import jp.co.wakwak.passmiru.Fragment.EventListFragment;
import jp.co.wakwak.passmiru.Fragment.UserEventFragment;

/**
 * Created by RyoSakaguchi on 15/04/29.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = {"新着イベント", "マイページ"};

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
                return new UserEventFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void destroyAllItem(ViewPager pager) {
        for (int i = 0; i < getCount() - 1; i++) {

            try {
                Object object = this.instantiateItem(pager, i);
                if (object != null) {
                    destroyItem(pager, i, object);
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

        if (position <= getCount()) {
            FragmentManager fm = ((Fragment) object).getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove((Fragment) object);
            ft.commit();
        }
    }

}
