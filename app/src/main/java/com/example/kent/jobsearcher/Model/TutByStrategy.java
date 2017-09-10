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
 * Created by Kent on 04.07.2017.
 */

public class TutByStrategy implements Strategy {

    //private static final String URL = "https://jobs.tut.by/search/vacancy?text=";
    private static final String URL = "https://jobs.tut.by";
    private static final String REFERRER = "https://hh.ru/search/vacancy?text=java+%D0%BA%D0%B8%D0%B5%D0%B2";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0";
   // Context context;
    private OnShowList listener;
    private Fragment fragment;
    private String searchString;
    private String nextPage;
    private UpdateList updateListener;

    public TutByStrategy(OnShowList listener) {
        this.listener = listener;
    }

    public void setUpdateListener(UpdateList updateListener) {
        this.updateListener = updateListener;
    }

    @Override
    public void searchExecute(String searchString) {
        this.searchString = searchString;
        TutByTask task = new TutByTask();
        task.execute(searchString);
    }


    private class TutByTask extends AsyncTask<String,String,List<Vacancy>> {
        /*private Context mContext;
        public TutByTask(Context context) {
            mContext = context;
        }*/
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
                String stringCount = document.getElementsByAttributeValue("data-qa", "vacancy-serp__found").text();
                checkCount(stringCount);
                //Elements elements = document.getElementsByAttributeValue("data-qa","div.vacancy-serp__vacancy");
                Elements elements = document.getElementsByClass("search-result-description");
                if (elements.size() > 0) {
                    for (Element element : elements) {
                        Vacancy vacancy = new Vacancy();
                        vacancy.setTitle(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").text());
                        vacancy.setUrl(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").attr("href"));
                        vacancy.setSalary(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text());
                        vacancy.setCompanyName(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text());
                        vacancy.setAddress(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text());
                        vacancy.setDate(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-date").text());
                        list.add(vacancy);
                    }
                }
                nextPage = document.getElementsByAttributeValue("data-qa", "pager-next").attr("href");

            } catch (IOException e) {
                e.printStackTrace();
            }
            // }
            return list;
        }

        void checkCount(String string) {
          //  String count;
            if (!string.equals("")) {
                String[] strings = string.split(" ");
                count = strings[1].replaceAll("\\s+", "");
                publishProgress(count);
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
                args.putString("name", "tut.by");
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
