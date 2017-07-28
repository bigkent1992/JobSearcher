package com.example.kent.jobsearcher.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.example.kent.jobsearcher.OnSearchCompleted;
import com.example.kent.jobsearcher.R;
import com.example.kent.jobsearcher.View.FragmentVacancies;

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

    private static final String URL = "https://jobs.tut.by/search/vacancy?text=";
    private static final String REFERRER = "https://hh.ru/search/vacancy?text=java+%D0%BA%D0%B8%D0%B5%D0%B2";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0";
   // Context context;
   private OnSearchCompleted listener;

    public TutByStrategy(OnSearchCompleted listener) {
        this.listener = listener;
    }


    @Override
    public void searchExecute(String searchString) {
        TutByTask task = new TutByTask();
        task.execute(searchString);
    }


    private class TutByTask extends AsyncTask<String,String,List<Vacancy>> {
        /*private Context mContext;
        public TutByTask(Context context) {
            mContext = context;
        }*/

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
                String count = document.getElementsByAttributeValue("data-qa", "vacancy-serp__found").text();
                publishProgress(count);
                //Elements elements = document.getElementsByAttributeValue("data-qa","div.vacancy-serp__vacancy");
                Elements elements = document.getElementsByClass("search-result-description");
                if (elements.size() > 0) {
                    for (Element element : elements) {
                        Vacancy vacancy = new Vacancy();
                        vacancy.setTitle(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").text());
                        vacancy.setUrl(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").attr("href"));
                        vacancy.setCompanyName(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text());
                        list.add(vacancy);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // }
            return list;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            listener.OnSearchCompleted(values[0]);
        }

        @Override
        protected void onPostExecute(List<Vacancy> list) {
            super.onPostExecute(list);
            listener.OnSearchCompleted(list);
        }
    }
}
