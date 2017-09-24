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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kent on 24.09.2017.
 */

public class RabotaByDetails implements Strategy {
    private static final String URL = "https://rabota.by/";
    private static final String URL_NEXT_PAGE = "https://rabota.by";
    private static final String REFERRER = "https://rabota.by/search/";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String ACCEPT_LANGUAGE = "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3";
    // Context context;
    private OnShowDetails listener;
    private Fragment fragment;
    private String searchString;
    private String nextPage;
    private UpdateList updateListener;
    private boolean isNextPage;

    public RabotaByDetails(OnShowDetails listener) {
        this.listener = listener;
    }

    @Override
    public void searchExecute(String searchString) {
        this.searchString = searchString;
        RabotaByTask task = new RabotaByTask();
        task.execute(searchString);
    }

    private class RabotaByTask extends AsyncTask<String,String,Vacancy> {
        String count = "";

        @Override
        protected Vacancy doInBackground(String... params) {
            List<Vacancy> list = new ArrayList<>();
            Document document;
            Vacancy vacancy = null;
            int i = 0;
            // while (i < 1) {
            //String urlParameters = "search[tags]=Java&search[key_words]=1&search[what]=vacancy&submitButton=Найти вакансии";
            String urlParameters = params[0];
            StringBuilder response = new StringBuilder();
            try {
                document = Jsoup.connect(URL + params[0])
                        .referrer(REFERRER)
                        .userAgent(USER_AGENT)
                        .get();
                if (document == null)
                    return null;

                vacancy = new Vacancy();
                vacancy.setSite("RABOTA.BY");
                vacancy.setTitle(document.getElementsByClass("appointment-title").text());
                vacancy.setCompanyName(document.getElementsByClass("listOrgName").text());
                vacancy.setSalary(document.getElementsByClass("comment view-comment salary").text());
                vacancy.setCity(document.getElementsByClass("adv-point view-adv-point").first().select("span").last().text());
                vacancy.setExperience(document.getElementsByClass("adv-point view-adv-point viewExperience").select("span").last().text());
                //vacancy.setDate(document.getElementsByClass("italic").text());
                StringBuilder builder = new StringBuilder();
                Elements elements = document.getElementsByClass("adv-full-b view-adv-full-b");
                for (int j = 0;j < elements.size()-1;j++) {
                    builder.append(elements.get(j).html());
                }
                vacancy.setMain_text(builder.toString());

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
                args.putString("name", "rabota.by");
                args.putString("url", searchString);
                args.putParcelable("vacancy", vacancy);
                fragment.setArguments(args);
                listener.OnSearchCompleted(fragment);
        }
    }
}
