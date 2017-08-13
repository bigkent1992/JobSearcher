package com.example.kent.jobsearcher.Model;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.example.kent.jobsearcher.OnShowList;
import com.example.kent.jobsearcher.UpdateList;
import com.example.kent.jobsearcher.View.FragmentVacancies;
import com.example.kent.jobsearcher.View.FragmentVacanciesNew;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kent on 11.07.2017.
 */

public class PracaByStrategy implements Strategy {
    //private static final String URL = "https://praca.by/search/vacancies/?search[query]=";
    private static final String URL = "https://praca.by/";
    private static final String REFERRER = "https://hh.ru/search/vacancy?text=java+%D0%BA%D0%B8%D0%B5%D0%B2";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0";
    // Context context;
    private OnShowList listener;
    private Fragment fragment;
    private String searchString;
    private String nextPage;
    private UpdateList updateListener;

    public PracaByStrategy(OnShowList listener) {
        this.listener = listener;
    }

    public void setUpdateListener(UpdateList updateListener) {
        this.updateListener = updateListener;
    }

    @Override
    public void searchExecute(String searchString) {
        this.searchString = searchString;
        PracaByTask task = new PracaByTask();
        task.execute(searchString);
    }

    private class PracaByTask extends AsyncTask<String,String,List<Vacancy>> {
        String count = "";

        @Override
        protected List<Vacancy> doInBackground(String... params) {
            List<Vacancy> list = new ArrayList<>();
            Document document;
            int i = 0;
            // while (i < 1) {
            try {
                // document = Jsoup.connect(String.format(URL, params[0]))
                document = Jsoup.connect(URL + params[0])
                        .referrer(REFERRER)
                        .userAgent(USER_AGENT)
                        .get();
                if (document == null)
                    return Collections.emptyList();
                String stringCount = document.getElementsByClass("search-list__count__value").text();
                checkCount(stringCount);
                //Elements elements = document.getElementsByAttributeValue("data-qa","div.vacancy-serp__vacancy");
                Elements elements = document.getElementsByClass("vac-small__column vac-small__column_2");
                if (elements.size() > 0) {
                    for (Element element : elements) {
                        Vacancy vacancy = new Vacancy();
                        vacancy.setTitle(element.getElementsByClass("vac-small__title").text());
                        vacancy.setUrl(element.getElementsByClass("vac-small__title").attr("href"));
                        vacancy.setCompanyName(element.getElementsByClass("vac-small__organization").text());
                        list.add(vacancy);
                    }
                }
                Elements paginations = document.getElementsByClass("pagination__item");
                if (paginations.size() > 0) {
                    for (int j = 1;j < paginations.size();j++) {
                        if (j == paginations.size() - 1)
                            nextPage = paginations.get(j).select("a").attr("href");
                    }
                } else
                    nextPage = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            // }
            return list;
        }

        void checkCount(String string) {
            if (!string.equals("0")) {
                //publishProgress(string);
                count = string;
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            listener.OnSearchCompleted(values[0]);
        }

        @Override
        protected void onPostExecute(List<Vacancy> list) {
            super.onPostExecute(list);
            if (fragment == null) {
                //fragment = new FragmentVacancies();
                fragment = new FragmentVacanciesNew();
                setUpdateListener((UpdateList) fragment);
                Bundle args = new Bundle();
                if (!count.equals(""))
                    args.putString("count", count);
                args.putString("name", "praca.by");
                args.putString("url", searchString);
                args.putString("nextPage", nextPage);
                args.putParcelableArrayList("vacancies", (ArrayList<? extends Parcelable>) list);
                fragment.setArguments(args);
                listener.OnSearchCompleted(fragment);
            } else {
                updateListener.loadMoreToList(list, nextPage);
            }
        }
    }
}
