package com.example.kent.jobsearcher.View;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kent.jobsearcher.Model.Vacancy;
import com.example.kent.jobsearcher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kent on 09.07.2017.
 */

public class FragmentPage extends Fragment implements ViewPager.OnPageChangeListener {
    public ViewPager mViewPager;
    private ViewPagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private Integer pages;
    Bundle args;
    public Fragment fragmentTutBy = null;
    public Fragment fragmentPracaBy = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        args = getArguments();
       // pages = args.getInt("pages");

        View view = inflater.inflate(R.layout.vacancies_pager, null);
        //pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(2);
        //mViewPager.setAdapter(pagerAdapter);
        setupViewPager(mViewPager);
        //mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addPage(fragments.get(0), titles.get(0));
        pagerAdapter.addPage(fragments.get(1), titles.get(1));
        pagerAdapter.addPage(fragments.get(2), titles.get(2));
        viewPager.setAdapter(pagerAdapter);
    }

    public void fill(List<String> titles, List<Fragment> fragments) {
        for (int i = 0; i < titles.size(); i++) {
            this.titles.add(titles.get(i));
            this.fragments.add(fragments.get(i));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Fragment page = pagerAdapter.getItem(position);
        String count = page.getArguments().getString("count");
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView tvCount = (TextView) toolbar.findViewById(R.id.tvCount);
        tvCount.setText(count);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> pageList = new ArrayList<>();
        private final List<String> pageTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = pageList.get(position);
            /*Bundle bundle;
            switch (position) {
                case 0:
                    list = args.getParcelableArrayList("v_tut.by");
                    fragment = new FragmentVacancies();
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("vacancies", (ArrayList<? extends Parcelable>) list);
                    fragment.setArguments(bundle);
                case 1:
                    list = args.getParcelableArrayList("v_praca.by");
                    fragment = new FragmentVacancies();
                    bundle = new Bundle();
                    bundle.putParcelableArrayList("vacancies", (ArrayList<? extends Parcelable>) list);
                    fragment.setArguments(bundle);
            }*/
            return fragment;

            //getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).addToBackStack("one").commit();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitleList.get(position);
        }

        public void addPage(Fragment fragment, String title) {
            pageList.add(fragment);
            pageTitleList.add(title);
        }
    }
}
