package com.example.kent.jobsearcher.View;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kent.jobsearcher.Model.Vacancy;
import com.example.kent.jobsearcher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kent on 09.07.2017.
 */

public class FragmentPage extends Fragment {
    private ViewPager mViewPager;
    private PagerAdapter pagerAdapter;
    private List<Vacancy> list;
    private Integer pages;
    Bundle args;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        args = getArguments();
        pages = args.getInt("pages");

        View view = inflater.inflate(R.layout.vacancies_pager,null);
        pagerAdapter = new PagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            list = args.getParcelableArrayList("vacancies");
            FragmentVacancies fragment = new FragmentVacancies();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("vacancies", (ArrayList<? extends Parcelable>) list);
            fragment.setArguments(bundle);
            return fragment;
            //getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).addToBackStack("one").commit();
        }

        @Override
        public int getCount() {
            return pages;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + (position + 1);
        }
    }
}
