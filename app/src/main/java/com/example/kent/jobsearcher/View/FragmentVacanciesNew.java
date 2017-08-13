package com.example.kent.jobsearcher.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kent.jobsearcher.ListAdapter;
import com.example.kent.jobsearcher.MainActivity;
import com.example.kent.jobsearcher.Model.Vacancy;
import com.example.kent.jobsearcher.R;
import com.example.kent.jobsearcher.UpdateList;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Kent on 12.08.2017.
 */

public class FragmentVacanciesNew extends Fragment implements UpdateList {
    private ListAdapter adapter;
    private String name, nextPage, url;
    private int lastVisibleItem, totalItemCount, visibleItemCount;
    boolean isLoading = false;
    ProgressDialog dialog;
    int  visibleThreshold = 4;
    boolean userScrolled = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("log", "onCreateView");
        Bundle args = getArguments();
        name = args.getString("name");
        nextPage = args.getString("nextPage");
        url = args.getString("url");
        List<Vacancy> list = args.getParcelableArrayList("vacancies");
        View view = inflater.inflate(R.layout.list_view, null);


       // RecyclerView recyclerView = new RecyclerView(new ContextThemeWrapper(getActivity(), R.style.ScrollbarRecyclerView));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ListAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                   // if (onLoadMoreListener != null) {
                     //   onLoadMoreListener.onLoadMore();
                        loadURL();
                  //  }
                   // isLoading = true;
                }
            }
        });
        return view;
    }

    private void loadURL() {
        if (nextPage == null) return;
       // dialog = new ProgressDialog(getActivity());
        //dialog.show();
        adapter.vacancies.add(null);
        adapter.notifyItemInserted(adapter.vacancies.size()-1);
        boolean isNextPage = false;
        String searchString = null;
        isLoading = true;
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
       // if (dialog.isShowing()) dialog.dismiss();
        adapter.vacancies.remove(adapter.vacancies.size()-1);
        adapter.vacancies.addAll(list);
        adapter.notifyDataSetChanged();
        this.nextPage = nextPage;
        isLoading = false;
    }
}
