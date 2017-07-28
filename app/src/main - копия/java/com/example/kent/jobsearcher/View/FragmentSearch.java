package com.example.kent.jobsearcher.View;

import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.Toast;

import com.example.kent.jobsearcher.MainActivity;
import com.example.kent.jobsearcher.Model.Provider;
import com.example.kent.jobsearcher.Model.TutByStrategy;
import com.example.kent.jobsearcher.Model.Vacancy;
import com.example.kent.jobsearcher.OnSearchCompleted;
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

    private String makeSearchString() {
        StringBuilder builder = new StringBuilder();
        String key_words = etKey_words.getText().toString();
        String city = etCity.getText().toString();
        String exp = spExperience.getSelectedItem().toString();
        String period = spPeriod.getSelectedItem().toString();
        if (TextUtils.isEmpty(key_words)) {
            etKey_words.setError("Это поле не может быть пустым");
            return null;
        }
        builder.append(key_words);

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
        builder.append("&page=0");
        return builder.toString();
    }

    @Override
    public void onClick(View v) {
        String searchString = makeSearchString();
        if (searchString == null) return;
        Toast.makeText(getActivity(),searchString, Toast.LENGTH_LONG).show();
        TutByStrategy strategy = new TutByStrategy((OnSearchCompleted) getActivity());
        Provider provider = new Provider(strategy);
        provider.getAllVacancies(searchString);
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
