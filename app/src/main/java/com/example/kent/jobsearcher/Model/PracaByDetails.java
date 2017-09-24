package com.example.kent.jobsearcher.Model;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.example.kent.jobsearcher.OnShowDetails;
import com.example.kent.jobsearcher.OnShowList;
import com.example.kent.jobsearcher.UpdateList;
import com.example.kent.jobsearcher.View.FragmentDetails;
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
 * Created by Kent on 24.09.2017.
 */

public class PracaByDetails implements Strategy {
    private static final String URL = "https://praca.by/search/vacancies/";
    private static final String REFERRER = "https://hh.ru/search/vacancy?text=java+%D0%BA%D0%B8%D0%B5%D0%B2";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0";
    // Context context;
    private OnShowDetails listener;
    private Fragment fragment;
    private String searchString;
    private String nextPage;
    private UpdateList updateListener;

    public PracaByDetails(OnShowDetails listener) {
        this.listener = listener;
    }

    @Override
    public void searchExecute(String searchString) {
        this.searchString = searchString;
        PracaByTask  task = new PracaByTask();
        task.execute(searchString);
    }

    private class PracaByTask extends AsyncTask<String,String,Vacancy> {
        String count = "";

        @Override
        protected Vacancy doInBackground(String... params) {
            //List<Vacancy> list = new ArrayList<>();
            Vacancy vacancy = null;
            Document document;
            int i = 0;
            // while (i < 1) {
            try {
                // document = Jsoup.connect(String.format(URL, params[0]))
                document = Jsoup.connect(params[0])
                        .referrer(REFERRER)
                        .userAgent(USER_AGENT)
                        .get();
                if (document == null)
                    return null;
                vacancy = new Vacancy();
                vacancy.setSite("PRACA.BY");
                vacancy.setCompanyName(document.getElementsByClass("org-info__name").text());
                vacancy.setTitle(document.getElementsByClass("vacancy__title").text());
                vacancy.setSalary(document.getElementsByClass("vacancy__salary").text());
                vacancy.setCity(document.getElementsByClass("vacancy__city").text());
                vacancy.setExperience(document.getElementsByClass("vacancy__experience").text());
                vacancy.setMain_text(document.getElementsByClass("description wysiwyg-st").html());

            } catch (IOException e) {
                e.printStackTrace();
            }
            // }
            return vacancy;
        }

        @Override
        protected void onPostExecute(Vacancy vacancy) {
            super.onPostExecute(vacancy);
                fragment = new FragmentDetails();
                Bundle args = new Bundle();
                args.putString("name", "praca.by");
                args.putString("url", searchString);
                args.putParcelable("vacancy", vacancy);
                fragment.setArguments(args);
                listener.OnSearchCompleted(fragment);
        }
    }
}
