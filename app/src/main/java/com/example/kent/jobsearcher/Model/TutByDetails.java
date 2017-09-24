package com.example.kent.jobsearcher.Model;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.kent.jobsearcher.OnShowDetails;
import com.example.kent.jobsearcher.UpdateList;
import com.example.kent.jobsearcher.View.FragmentDetails;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24.07.2017.
 */

public class TutByDetails implements Strategy {
    //private static final String URL = "https://jobs.tut.by/search/vacancy?text=";
    private static final String URL = "https://jobs.tut.by";
    private static final String REFERRER = "https://hh.ru";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0";
    // Context context;
    private OnShowDetails listener;
    private Fragment fragment;
    private String searchString;
    private String nextPage;
    private UpdateList updateListener;

    public TutByDetails(OnShowDetails listener) {
        this.listener = listener;
    }


    @Override
    public void searchExecute(String searchString) {
        this.searchString = searchString;
        TutByTask task = new TutByTask();
        task.execute(searchString);
    }

    private class TutByTask extends AsyncTask<String,String,Vacancy> {
        String count = "";

        @Override
        protected Vacancy doInBackground(String... params) {
            List<String> list;
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
                //if (document == null)<td class="l-content-colum-1 b-v-info-content"><div class="l-paddings"> з/п не указана</div></td>
                //    return Collections.emptyList();
                //String stringCount = document.getElementsByAttributeValue("data-qa", "vacancy-serp__found").text();
               // checkCount(stringCount);
                //Elements elements = document.getElementsByAttributeValue("data-qa","div.vacancy-serp__vacancy");
                vacancy = new Vacancy();
                vacancy.setSite("TUT.BY");
                vacancy.setTitle(document.getElementsByClass("title b-vacancy-title").text());
                vacancy.setCompanyName(document.getElementsByClass("companyname").select("a").text());
                String salary = document.getElementsByClass("l-content-colum-1 b-v-info-content").addClass("l-paddings").text();
                if (salary == null)
                    vacancy.setSalary("Не указана");
                else
                    vacancy.setSalary(salary);
                vacancy.setCity(document.getElementsByClass("l-content-colum-2 b-v-info-content").addClass("l-paddings").text());
                vacancy.setExperience(document.getElementsByClass("l-content-colum-3 b-v-info-content").addClass("l-paddings").text());
                vacancy.setMain_text(document.getElementsByClass("b-vacancy-desc-wrapper").html());
                Elements elements = document.getElementsByAttributeValue("data-qa", "skills-element");
               // if (document.select("h3.b-subtitle").text().equals("Ключевые навыки")) {


                    if (elements.size() > 0) {
                        list = new ArrayList<>();
                        for (Element element : elements) {
                            list.add(element.getElementsByAttributeValue("data-qa", "bloko-tag__text").text());
                        }
                        vacancy.setKey_skills(list);
                }

                       // vacancy.setUrl(document.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").attr("href"));
                        //vacancy.setCompanyName(document.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text());
                       // list.add(vacancy);

                //nextPage = document.getElementsByAttributeValue("data-qa", "pager-next").attr("href");

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
                args.putString("name", "tut.by");
                args.putString("url", searchString);
                args.putParcelable("vacancy", vacancy);
                fragment.setArguments(args);
                listener.OnSearchCompleted(fragment);
        }
    }
}
