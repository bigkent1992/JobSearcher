package com.example.kent.jobsearcher.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.example.kent.jobsearcher.Model.Vacancy;
import com.example.kent.jobsearcher.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kent on 05.07.2017.
 */

public class FragmentVacancies extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args =  getArguments();
        List<Vacancy> list = args.getParcelableArrayList("vacancies");
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0; i<list.size(); i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("title", list.get(i).getTitle());
            hm.put("url", list.get(i).getUrl());
            hm.put("company",list.get(i).getCompanyName());
            aList.add(hm);
        }
        String[] from = { "title","url","company"};
        int[] to = { R.id.tv1,R.id.tv2,R.id.tv3};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.vacancies_list, from, to);

        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
