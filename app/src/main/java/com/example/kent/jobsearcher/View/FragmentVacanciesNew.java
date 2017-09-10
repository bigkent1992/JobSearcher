package com.example.kent.jobsearcher.View;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kent.jobsearcher.CardClick;
import com.example.kent.jobsearcher.ListAdapter;
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
 * Created by Kent on 12.08.2017.
 */

public class FragmentVacanciesNew extends Fragment implements UpdateList, CardClick{
    private static final String LOG_TAG = "MyLog";
    private ListAdapter adapter;
    private String name, nextPage, url, count;
    int lastVisibleItem, totalItemCount, visibleItemCount;
    boolean isLoading = false;
    ProgressDialog dialog;
    int  visibleThreshold = 3;
    boolean userScrolled = false;
    List<Vacancy> list;
    Toolbar toolbar;
    RecyclerView recyclerView;
   // final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) adapter.vacancies);
     //   outState.putParcelable("myState", layoutManager.onSaveInstanceState());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "FragmentVacanciesNewCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "FragmentVacanciesNewDestroy");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("log", "onCreateView");
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.list_view, null);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

       // RecyclerView recyclerView = new RecyclerView(new ContextThemeWrapper(getActivity(), R.style.ScrollbarRecyclerView));
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
     //   if (savedInstanceState != null)
        //    layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable("myState"));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            name = args.getString("name");
            nextPage = args.getString("nextPage");
            url = args.getString("url");
            list = args.getParcelableArrayList("vacancies");
           // count = args.getString("count");
            adapter = new ListAdapter(list);
            adapter.setCardClick(this);
        }
       // }
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
               // visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                if (totalItemCount < 20) return;
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
        if (nextPage.equals("")) return;
       // dialog = new ProgressDialog(getActivity());
        //dialog.show();
        adapter.vacancies.add(null);
        adapter.notifyItemInserted(adapter.vacancies.size()-1);
        boolean isNextPage = false;
        String searchString;
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
    }

    @Override
    public void loadMoreToList(List<Vacancy> list, String nextPage) {
       // if (dialog.isShowing()) dialog.dismiss();
        adapter.vacancies.remove(adapter.vacancies.size()-1);
        adapter.vacancies.addAll(list);
        adapter.notifyDataSetChanged();
        this.nextPage = nextPage;
        isLoading = false;
        toolbar.setSubtitle("Показано: "+recyclerView.getLayoutManager().getItemCount());
    }

    @Override
    public void onCardClick(Vacancy vacancy) {
        TutByDetails details = new TutByDetails(new OnShowDetails() {
            @Override
            public void OnSearchCompleted(Fragment fragment) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).addToBackStack(null).commit();
            }
        });
        details.searchExecute(vacancy.getUrl());
    }
}
