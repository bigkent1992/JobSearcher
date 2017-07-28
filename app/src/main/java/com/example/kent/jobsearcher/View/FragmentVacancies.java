package com.example.kent.jobsearcher.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.example.kent.jobsearcher.MainActivity;
import com.example.kent.jobsearcher.Model.TutByDetails;
import com.example.kent.jobsearcher.Model.Vacancy;
import com.example.kent.jobsearcher.OnShowDetails;
import com.example.kent.jobsearcher.R;
import com.example.kent.jobsearcher.UpdateList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kent on 05.07.2017.
 */

public class FragmentVacancies extends ListFragment implements AbsListView.OnScrollListener, UpdateList, AdapterView.OnItemClickListener {
    boolean loadingMore = false;
    boolean userScrolled = false;
    String url;
    String name;
    SimpleAdapter adapter;
    List<HashMap<String,String>> data;
    private String nextPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("log", "onCreateView");
        Bundle args = getArguments();
        name = args.getString("name");
        nextPage = args.getString("nextPage");
        url = args.getString("url");
        List<Vacancy> list = args.getParcelableArrayList("vacancies");
        data = new ArrayList<>();

        for(int i=0; i<list.size(); i++){
            HashMap<String, String> map = new HashMap<String,String>();
            map.put("title", list.get(i).getTitle());
            map.put("url", list.get(i).getUrl());
            map.put("company",list.get(i).getCompanyName());
            data.add(map);
        }
        String[] from = { "title","url","company"};
        int[] to = { R.id.tv1,R.id.tv2,R.id.tv3};
        adapter = new SimpleAdapter(getActivity(), data, R.layout.vacancies_list, from, to);

        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnScrollListener(this);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> map = (HashMap<String, String>) adapter.getItem(position);
        String url = map.get("url");
        TutByDetails details = new TutByDetails(new OnShowDetails() {
            @Override
            public void OnSearchCompleted(Fragment fragment) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).addToBackStack(null).commit();
               // Fragment fragment1 = getActivity().getSupportFragmentManager().findFragmentById(R.id.main_frame);
               // ViewPager pager = fragment1.
            }
        });
        details.searchExecute(url);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_TOUCH_SCROLL){
            userScrolled = true;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastInScreen = firstVisibleItem + visibleItemCount;
        if((lastInScreen == totalItemCount) && !(loadingMore) && userScrolled) {
           // int page = Integer.parseInt(url.substring(url.lastIndexOf("=") + 1, url.length())) + 1;
           // String searchString = url.substring(0, url.lastIndexOf("=")+ 1) + String.valueOf(page);
            loadURL();
        }
    }

    private void loadURL() {
        boolean isNextPage = false;
        String searchString = null;
        loadingMore = true;
        searchString = nextPage;
        if (name.equals("rabota.by"))
            isNextPage = true;
       /* switch (name) {
            case "tut.by":
                searchString = "https://jobs.tut.by" + pageNext;
                break;
            case "rabota.by":
                searchString = "http://rabota.by" + pageNext;
                break;
        }*/
        ((MainActivity)getActivity()).startSearching(searchString, name, isNextPage);
       // Strategy strategy;
       // Provider provider;
        //if (searchString.startsWith("https://jobs.tut.by")) {
            //loadingMore = true;
            //((MainActivity)getActivity()).startSearching(searchString, name);
            //strategy = new TutByStrategy((OnShowList) getActivity());
            //provider = new Provider();
           // provider.setStrategy(strategy);
           // provider.getAllVacancies(searchString);
       // }
    }

    @Override
    public void loadMoreToList(List<Vacancy> list, String nextPage) {
        for(int i=0; i<list.size(); i++){
            HashMap<String, String> hm = new HashMap<>();
            hm.put("title", list.get(i).getTitle());
            hm.put("url", list.get(i).getUrl());
            hm.put("company",list.get(i).getCompanyName());
            data.add(hm);
        }
        adapter.notifyDataSetChanged();
        this.nextPage = nextPage;
        loadingMore = false;
    }

}
