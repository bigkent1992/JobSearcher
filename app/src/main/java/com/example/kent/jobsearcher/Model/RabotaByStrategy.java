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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kent on 13.07.2017.
 */

public class RabotaByStrategy implements Strategy {
    private static final String URL = "http://rabota.by/search/";
    private static final String URL_NEXT_PAGE = "http://rabota.by";
    private static final String REFERRER = "http://rabota.by/search/";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String ACCEPT_LANGUAGE = "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3";
    // Context context;
    private OnShowList listener;
    private Fragment fragment;
    private String searchString;
    private String nextPage;
    private UpdateList updateListener;
    private boolean isNextPage;

    public RabotaByStrategy(OnShowList listener) {
        this.listener = listener;
    }

    public void setNextPage(boolean nextPage) {
        isNextPage = nextPage;
    }

    public void setUpdateListener(UpdateList updateListener) {
        this.updateListener = updateListener;
    }

    @Override
    public void searchExecute(String searchString) {
        this.searchString = searchString;
        RabotaByTask task = new RabotaByTask();
        task.execute(searchString);
    }

    private class RabotaByTask extends AsyncTask<String,String,List<Vacancy>> {
        String count = "";

        @Override
        protected List<Vacancy> doInBackground(String... params) {
            List<Vacancy> list = new ArrayList<>();
            Document document;
            int i = 0;
            // while (i < 1) {
            //String urlParameters = "search[tags]=Java&search[key_words]=1&search[what]=vacancy&submitButton=Найти вакансии";
            String urlParameters = params[0];
            StringBuilder response = new StringBuilder();
            try {
                if (!isNextPage) {
                    URL url = new URL(URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Referer", REFERRER);
                    conn.setRequestProperty("User-Agent", USER_AGENT);
                    conn.setRequestProperty("Content-Type", CONTENT_TYPE);
                    conn.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);
                    conn.setDoOutput(true);

                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(urlParameters);
                    writer.flush();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        writer.close();
                    } else {
                        return list;
                    }
                    document = Jsoup.parse(response.toString());
                } else {
                    document = Jsoup.connect(URL_NEXT_PAGE + params[0])
                            .referrer(REFERRER)
                            .userAgent(USER_AGENT)
                            .get();
                    if (document == null)
                        return list;
                }

                String stringCount = document.getElementsByClass("found shown-block").text();
                checkCount(stringCount);//.equals("")?"0":stringCount);
                nextPage = document.getElementsByClass("flip_pages").select("a").attr("href");
                // if (nextPage != null)
                //  searchString = nextPage;
                //  Elements elements = document.getElementsByAttributeValue("data-qa","div.vacancy-serp__vacancy");
                Elements elements = document.getElementsByClass("useful_area");
                if (elements.size() > 0) {
                    for (Element element : elements) {
                        Vacancy vacancy = new Vacancy();
                        vacancy.setTitle(element.getElementsByClass("statistics_view_short").text());
                        vacancy.setUrl(element.getElementsByClass("statistics_view_short").attr("href"));
                        vacancy.setCompanyName(element.getElementsByClass("org_name").text());
                        list.add(vacancy);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // }
            return list;
        }

        void checkCount(String string) {
            //tring count;
            if (!string.equals("")) {
                String[] strings = string.split("\\s+");
                count = strings[1];//.replaceAll("\\s+", "");
               // publishProgress(count);
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
                args.putString("name", "rabota.by");
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
