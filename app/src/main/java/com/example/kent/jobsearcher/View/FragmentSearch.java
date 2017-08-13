package com.example.kent.jobsearcher.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kent.jobsearcher.MainActivity;
import com.example.kent.jobsearcher.Model.Strategy;
import com.example.kent.jobsearcher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kent on 06.07.2017.
 */

public class FragmentSearch extends Fragment implements View.OnClickListener {
    TextView etKey_words, etCity;
    Spinner spPeriod, spExperience;
    Button btSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.vacancies_search, null);
        etKey_words = (TextView) view.findViewById(R.id.etKey_words);
        etCity = (TextView) view.findViewById(R.id.etCity);
        spPeriod = (Spinner) view.findViewById(R.id.spPeriod);
        spExperience = (Spinner) view.findViewById(R.id.spExperience);
        btSearch = (Button) view.findViewById(R.id.btSearch);

        ArrayAdapter<String> adapterExp = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.experience));
        adapterExp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExperience.setAdapter(adapterExp);

        ArrayAdapter<String> adapterPer = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.period));
        adapterPer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPeriod.setAdapter(adapterPer);

        btSearch.setOnClickListener(this);
        return view;
    }

    private String makeSearchTutBy() {
        StringBuilder builder = new StringBuilder();
        String key_words = etKey_words.getText().toString();
        String city = etCity.getText().toString();
        String exp = spExperience.getSelectedItem().toString();
        String period = spPeriod.getSelectedItem().toString();
        if (TextUtils.isEmpty(key_words)) {
            etKey_words.setError("Это поле не может быть пустым");
            return null;
        }
        builder.append("/search/vacancy?text=").append(key_words);

        if (!TextUtils.isEmpty(city))
            builder.append("+").append(city);

        switch (spExperience.getSelectedItemPosition()) {
            case 0: exp = "doesNotMatter";
                break;
            case 1: exp = "noExperience";
                break;
            case 2: exp = "between1And3";
                break;
        }
        builder.append("&experience=").append(exp);

        switch (spPeriod.getSelectedItemPosition()) {
            case 0: period = "";
                break;
            case 1: period = "7";
                break;
            case 2: period = "1";
                break;
        }
        builder.append("&search_period=").append(period);
        builder.append("&items_on_page=20&page=0");
        return builder.toString();
    }

    private String makeSearchPracaBy() {
        StringBuilder builder = new StringBuilder();
        String key_words = etKey_words.getText().toString();
        String city = etCity.getText().toString();
        String exp = spExperience.getSelectedItem().toString();
        String period = spPeriod.getSelectedItem().toString();
        if (TextUtils.isEmpty(key_words)) {
            etKey_words.setError("Это поле не может быть пустым");
            return null;
        }
        builder.append("search/vacancies/?search[query]=").append(key_words);

        if (!TextUtils.isEmpty(city))
            builder.append("+").append(city);

        switch (spExperience.getSelectedItemPosition()) {
            case 0: exp = "[from-5]=1";
                break;
            case 1: exp = "[no-matter]=1";
                break;
            case 2: exp = "[from-2]=1";
                break;
        }
        builder.append("&search[experience_vac]").append(exp);

        switch (spPeriod.getSelectedItemPosition()) {
            case 0: period = "30";
                break;
            case 1: period = "7";
                break;
            case 2: period = "1";
                break;
        }
        builder.append("&upped_period=").append(period);
        builder.append("&page=1");
        return builder.toString();
    }

    private String makeSearchRabotaBy() {
        StringBuilder builder = new StringBuilder();
        String key_words = etKey_words.getText().toString();
        String city = etCity.getText().toString();
        String exp = spExperience.getSelectedItem().toString();
        String period = spPeriod.getSelectedItem().toString();
        if (TextUtils.isEmpty(key_words)) {
            etKey_words.setError("Это поле не может быть пустым");
            return null;
        }
        builder.append("search[tags]=").append(key_words);

        if (!TextUtils.isEmpty(city))
            builder.append("+").append(city);

        switch (spExperience.getSelectedItemPosition()) {
            case 0: exp = "";
                break;
            case 1: exp = "&search[experience][1]=1";
                break;
            case 2: exp = "&search[experience][3]=3&search[experience][4]=4&search[experience][5]=5";
                break;
        }
        builder.append(exp);

        switch (spPeriod.getSelectedItemPosition()) {
            case 0: period = "31";
                break;
            case 1: period = "7";
                break;
            case 2: period = "1";
                break;
        }
        builder.append("&search[period]=").append(period);
        //builder.append("&self=searchvacancy&search[what]=vacancy&submitButton=Найти вакансии");
        builder.append("&submitButton=Найти вакансии");
        return builder.toString();
    }

    //search[tags]=Java&search[key_words]=1&search[what]=vacancy&submitButton=Найти вакансии
    @Override
    public void onClick(View v) {

/*        String uri = Uri.parse("http://rabota.by/search/")
                .buildUpon()
               // .appendQueryParameter("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0")
               // .appendQueryParameter("Content-Type", "application/x-www-form-urlencoded")
               // .appendQueryParameter("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
                .appendQueryParameter("search[tags]", "Java")
                .appendQueryParameter("search[key_words]", "1")
                .appendQueryParameter("search[what]", "vacancy")
                .appendQueryParameter("submitButton", "Найти вакансии")
                .build().toString();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);*/
        MainActivity activity = (MainActivity) getActivity();
        activity.pageFragment = null;
        activity.fragments.clear();
        activity.tutByStrategy = null;
        activity.pracaByStrategy = null;
        activity.rabotaByStrategy = null;
        List<String> names = new ArrayList<>();
        names.add("tut.by");
        names.add("praca.by");
        names.add("rabota.by");
        for (String name : names) {
            String searchString = "";
            Strategy strategy = null;
           // Provider provider = new Provider();
            switch (name) {
                case "tut.by":
                    searchString = makeSearchTutBy();
                    //strategy = new TutByStrategy((OnShowList) getActivity());
                    break;
                case "praca.by":
                    searchString = makeSearchPracaBy();
                    //strategy = new PracaByStrategy((OnShowList) getActivity());
                    break;
                case "rabota.by":
                    searchString = makeSearchRabotaBy();
                    //strategy = new RabotaByStrategy((OnShowList) getActivity());
                    break;
            }
            activity.startSearching(searchString, name, false);
            //provider.setStrategy(strategy);
            //provider.getAllVacancies(searchString);
        }
    }

   /* @Override
    public void OnShowList(List<Vacancy> list) {
        if (list.isEmpty()) {
            Toast.makeText(getActivity(), "Вакансий нет", Toast.LENGTH_LONG).show();
            return;
        }
        FragmentVacancies fragment = new FragmentVacancies();
        Bundle args = new Bundle();
        args.putParcelableArrayList("vacancies", (ArrayList<? extends Parcelable>) list);
        fragment.setArguments(args);
        getChildFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
    }*/
}
